package com.example.commonlibrary.net.service;

import android.os.Process;
import android.os.SystemClock;


import com.example.commonlibrary.net.DownloadListener;
import com.example.commonlibrary.net.db.NewFileDaoImpl;
import com.example.commonlibrary.net.db.NewFileInfo;
import com.example.commonlibrary.net.exception.DownloadException;
import com.example.commonlibrary.net.model.DownloadStatus;
import com.example.commonlibrary.net.model.FileUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.Locale;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 下载线程
 */
public class DownloadTask implements Runnable {
    private final static String TAG = "DownloadTask";

    private static final int MIN_PROCESS_STEP = 65535;
    private static final int MIN_PROCESS_TIME = 2000;
    private static final int CALC_SPEED_TIME = 500;
    private static final int BUFFER_SIZE = 1024 * 4;
    private final OkHttpClient mClient;
    private NewFileInfo mNewFileInfo;
    private int mRetryTimes;
    private final DownloadListener mListener;
    private boolean mIsResumeAvailable = false;
    private boolean mIsRunning = false;
    private boolean mIsCancel = false;
    private boolean mIsStop = false;
    private boolean mIsRetry = false;
    private int mLastUpdateBytes;
    private long mLastUpdateTime;
    private int mLastCalcBytes;
    private long mLastCalcTime;


    public DownloadTask(OkHttpClient client, NewFileInfo NewFileInfo, int retryTimes, DownloadListener listener) {
        this.mClient = client;
        this.mNewFileInfo = NewFileInfo;
        this.mRetryTimes = retryTimes;
        this.mListener = listener;
    }

    public void setRetry(boolean retry) {
        mIsRetry = retry;
    }

    @Override
    public void run() {
        mIsRunning = true;
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        // 1.检测是否存在未下载完的文件
        _checkIsResumeAvailable();

        Response response = null;
        Call call = null;
        try {
            // 2.构建请求
            Request.Builder requestBuilder = new Request.Builder().url(mNewFileInfo.getUrl());
            requestBuilder.addHeader("Range", String.format(Locale.ENGLISH, "bytes=%d-", mNewFileInfo.getLoadBytes()));
            // 目前没有指定cache，下载任务非普通REST请求，用户已经有了存储的地方
            requestBuilder.cacheControl(CacheControl.FORCE_NETWORK);

            // 3.初始化请求
            Request request = requestBuilder.build();
            call = mClient.newCall(request);

            // 4.执行请求
            response = call.execute();

            final boolean isSucceedStart = response.code() == HttpURLConnection.HTTP_OK;
            final boolean isSucceedResume = response.code() == HttpURLConnection.HTTP_PARTIAL;

            if (isSucceedResume || isSucceedStart) {
                int total = mNewFileInfo.getTotalBytes();
                final String transferEncoding = response.header("Transfer-Encoding");

                // 5.获取文件长度
                if (isSucceedStart || total <= 0) {
                    if (transferEncoding == null) {
                        total = (int) response.body().contentLength();
                    } else {
                        // if transfer not nil, ignore content-length
                        total = -1;
                    }
                    mNewFileInfo.setTotalBytes(total);
                }
                if (total < 0) {
                    throw new DownloadException("Get content length error!");
                }

                // 6.网络状态已连接
                _onConnected();
                // 7.开始获取数据
                _onDownload(response);
            } else {
                if (!_onRetry()) {
                    mListener.onError(mNewFileInfo, "Numeric status code is error!");
                    _updateDb();
                }
            }
        } catch (Throwable e) {
            if (e instanceof DownloadException) {
                if (_onRetry()) {
                    return;
                }
            }
            mListener.onError(mNewFileInfo, e.toString());
            _updateDb();
        } finally {
            if (response != null && response.body() != null) {
                response.body().close();
            }
            if (call != null) {
                call.cancel();
            }
        }
    }

    /**
     * 重试处理
     *
     * @return 是否进行处理
     */
    private boolean _onRetry() {
        mIsRunning = false;
        if (mRetryTimes > 0) {
            mRetryTimes--;
            DownloadTask runnable = new DownloadTask(mClient, mNewFileInfo, mRetryTimes, mListener);
            runnable.setRetry(true);
            DownloadThreadPool.getInstance().execute(runnable);
            return true;
        }
        return false;
    }

    /**
     * 开始下载
     *
     * @param response 应答
     */
    private void _onDownload(Response response) throws Throwable {
        InputStream inputStream = null;
        final RandomAccessFile accessFile = FileUtils.getRandomAccessFile(
                mNewFileInfo.getPath() + mNewFileInfo.getName() + ".tmp",
                mNewFileInfo.getLoadBytes(), mNewFileInfo.getTotalBytes());
        int loadBytes = mNewFileInfo.getLoadBytes();
        try {
            // 1.获取数据流
            inputStream = response.body().byteStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, BUFFER_SIZE);
            byte[] buff = new byte[BUFFER_SIZE];
            do {
                // 2.读数据
                int byteCount = bufferedInputStream.read(buff);
                if (byteCount == -1) {
                    break;
                }
                // 3.写文件
                accessFile.write(buff, 0, byteCount);
                // 4.累加下载量
                loadBytes += byteCount;

                // 5.检测文件是否被其他操作
                if (accessFile.length() < loadBytes) {
                    mIsRunning = false;
                    throw new RuntimeException(
                            FileUtils.formatString("the file was changed by others when" +
                                    " downloading. %d %d", accessFile.length(), loadBytes));
                } else {
                    mNewFileInfo.setLoadBytes(loadBytes);
                    _onProcess();
                }

                // 6.检测停止状态
                if (mIsCancel || mIsStop) {
                    // callback on paused
                    mIsRunning = false;
                    if (mIsCancel) {
                        mListener.onCancel(mNewFileInfo);
                    } else {
                        mListener.onStop(mNewFileInfo);
                    }
                    _updateDb();
                    return;
                }
            } while (mIsRunning);

            // 7.处理 transfer encoding = chunked 的情况
            if (mNewFileInfo.getTotalBytes() == -1) {
                mNewFileInfo.setTotalBytes(loadBytes);
            }

            // 8.判断下载成功还是失败
            if (mNewFileInfo.getLoadBytes() == mNewFileInfo.getTotalBytes()) {
                _onComplete();
            } else {
                throw new DownloadException(FileUtils.formatString(
                        "Unfinished: load[%d] is not equal total[%d]!",
                        mNewFileInfo.getLoadBytes(), mNewFileInfo.getTotalBytes()));
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (accessFile != null) {
                accessFile.close();
            }
        }
    }

    /**
     * 下载完成
     */
    private void _onComplete() {
        // 重命名文件
        File tmpFile = new File(mNewFileInfo.getPath(), mNewFileInfo.getName() + ".tmp");
        File appFile = new File(mNewFileInfo.getPath(), mNewFileInfo.getName());
        tmpFile.renameTo(appFile);
        mListener.onComplete(mNewFileInfo);
//        // 下载完就从数据库删除
//        NewFileDaoImpl.getInstance().delete(mNewFileInfo.getUrl());
        _updateDb();
    }

    /**
     * 更新进度
     */
    private void _onProcess() {
        if (mNewFileInfo.getLoadBytes() == mNewFileInfo.getTotalBytes()) {
            // 下载完成
            mIsRunning = false;
            return;
        }

        long now = SystemClock.uptimeMillis();
        if (mLastCalcTime == 0) {
            mLastCalcTime = now;
            mLastCalcBytes = mNewFileInfo.getLoadBytes();
        }

        long diffBytes;
        long diffTimes = now - mLastCalcTime;
        if (diffTimes > CALC_SPEED_TIME) {
            diffBytes = mNewFileInfo.getLoadBytes() - mLastCalcBytes;
            int speed = (int) (diffBytes * 1000 / diffTimes);
            mNewFileInfo.setSpeed(speed);
            mLastCalcTime = now;
            mLastCalcBytes = mNewFileInfo.getLoadBytes();
        }
        mListener.onUpdate(mNewFileInfo);

        diffBytes = mNewFileInfo.getLoadBytes() - mLastUpdateBytes;
        diffTimes = now - mLastUpdateTime;
        if (diffBytes > MIN_PROCESS_STEP && diffTimes > MIN_PROCESS_TIME) {
            _updateDb();
            mLastUpdateBytes = mNewFileInfo.getLoadBytes();
            mLastUpdateTime = now;
        }


    }

    /**
     * 连接已建立，准备下载
     */
    private void _onConnected() {
        mIsRunning = true;
        if (!mIsRetry) {
            // 如果为异常重试则不进行回调
            mListener.onStart(mNewFileInfo);
        }
        if (!mIsResumeAvailable) {
            // 插入数据库
            mIsResumeAvailable = true;
            DownloadThreadPool.getInstance().update(new Runnable() {
                @Override
                public void run() {
                    NewFileDaoImpl.getInstance().insert(mNewFileInfo);
                }
            });
        }
    }

    /**
     * 检测是否存在资源
     */
    private void _checkIsResumeAvailable() {
        NewFileInfo info = NewFileDaoImpl.getInstance().query(mNewFileInfo.getUrl());
        if (info != null) {
            mIsResumeAvailable = true;
            if (info.getStatus() != DownloadStatus.COMPLETE && info.getLoadBytes() != info.getTotalBytes()) {
                mNewFileInfo = info;
            }
        }
    }

    /**
     * 判断是否正在执行
     *
     * @return
     */
    public boolean isRunning() {
        return mIsRunning;
    }

    /**
     * 线程的标识
     *
     * @return
     */
    public String tag() {
        return mNewFileInfo.getUrl();
    }

    /**
     * 取消
     */
    public void cancel() {
        mIsCancel = true;
    }

    /**
     * 停止
     */
    public void stop() {
        mIsStop = true;
    }

    /**
     * 更新数据库
     */
    private void _updateDb() {
        DownloadThreadPool.getInstance().update(new Runnable() {
            @Override
            public void run() {
                NewFileDaoImpl.getInstance().update(mNewFileInfo);
            }
        });
    }
}
