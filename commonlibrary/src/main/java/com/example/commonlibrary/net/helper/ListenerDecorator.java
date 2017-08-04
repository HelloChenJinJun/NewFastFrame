package com.example.commonlibrary.net.helper;


import com.example.commonlibrary.net.DownloadListener;
import com.example.commonlibrary.net.db.NewFileInfo;
import com.example.commonlibrary.net.model.DownloadStatus;
import com.example.commonlibrary.net.service.DownloadThreadPool;

/**
 * 封装下载监听器
 */
public final class ListenerDecorator implements DownloadListener {

    private final DownloadListener mListener;
    private final boolean mIsUiThread;

    public ListenerDecorator(DownloadListener listener, boolean isUiThread) {
        this.mListener = listener;
        this.mIsUiThread = isUiThread;
    }


    @Override
    public void onStart(final NewFileInfo NewFileInfo) {
        NewFileInfo.setStatus(DownloadStatus.START);
        if (mIsUiThread) {
            MainHandler.getInstance().runInMainThread(new Runnable() {
                @Override
                public void run() {
                    mListener.onStart(NewFileInfo);
                }
            });
        } else {
            mListener.onStart(NewFileInfo);
        }
    }

    @Override
    public void onUpdate(final NewFileInfo NewFileInfo) {
        NewFileInfo.setStatus(DownloadStatus.DOWNLOADING);
        if (mIsUiThread) {
            MainHandler.getInstance().runInMainThread(new Runnable() {
                @Override
                public void run() {
                    mListener.onUpdate(NewFileInfo);
                }
            });
        } else {
            mListener.onUpdate(NewFileInfo);
        }
    }

    @Override
    public void onStop(final NewFileInfo NewFileInfo) {
        NewFileInfo.setStatus(DownloadStatus.STOP);
        if (mIsUiThread) {
            MainHandler.getInstance().runInMainThread(new Runnable() {
                @Override
                public void run() {
                    mListener.onStop(NewFileInfo);
                }
            });
        } else {
            mListener.onStop(NewFileInfo);
        }
    }

    @Override
    public void onComplete(final NewFileInfo NewFileInfo) {
        NewFileInfo.setStatus(DownloadStatus.COMPLETE);
        if (mIsUiThread) {
            MainHandler.getInstance().runInMainThread(new Runnable() {
                @Override
                public void run() {
                    mListener.onComplete(NewFileInfo);
                }
            });
        } else {
            mListener.onComplete(NewFileInfo);
        }
        DownloadThreadPool.getInstance().cancel(NewFileInfo.getUrl(), false);
    }

    @Override
    public void onCancel(final NewFileInfo NewFileInfo) {
        NewFileInfo.setStatus(DownloadStatus.CANCEL);
        if (mIsUiThread) {
            MainHandler.getInstance().runInMainThread(new Runnable() {
                @Override
                public void run() {
                    mListener.onCancel(NewFileInfo);
                }
            });
        } else {
            mListener.onCancel(NewFileInfo);
        }
        DownloadThreadPool.getInstance().cancel(NewFileInfo.getUrl(), true);
    }

    @Override
    public void onError(final NewFileInfo NewFileInfo, final String errorMsg) {
        NewFileInfo.setStatus(DownloadStatus.ERROR);
        if (mIsUiThread) {
            MainHandler.getInstance().runInMainThread(new Runnable() {
                @Override
                public void run() {
                    mListener.onError(NewFileInfo, errorMsg);
                }
            });
        } else {
            mListener.onError(NewFileInfo, errorMsg);
        }
        DownloadThreadPool.getInstance().cancel(NewFileInfo.getUrl(), false);
    }
}
