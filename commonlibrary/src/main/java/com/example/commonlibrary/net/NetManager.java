package com.example.commonlibrary.net;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.net.db.DaoSession;
import com.example.commonlibrary.net.db.NewFileInfo;
import com.example.commonlibrary.net.db.NewFileInfoDao;
import com.example.commonlibrary.net.model.DownloadStatus;
import com.example.commonlibrary.utils.AppUtil;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.FileUtil;
import com.example.commonlibrary.utils.NetUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by COOTEK on 2017/8/3.
 */

public class NetManager {
    private static NetManager instance;
    //    由于每个下载请求要监听进度，因此要添加拦截器，所以要保持不同的retrofit
    private Map<String, Retrofit> stringRetrofitMap;
    private Map<String, CompositeDisposable> compositeDisposableMap;
    private DaoSession daoSession;
    private Map<String, NewFileInfo> newFileInfoMap;

    public static NetManager getInstance() {
        if (instance == null) {
            synchronized (NetManager.class) {
                instance = new NetManager();
            }
        }
        return instance;
    }

    private NetManager() {
        stringRetrofitMap = new HashMap<>();
        daoSession = BaseApplication.getAppComponent().getDaoSesion();
        compositeDisposableMap = new HashMap<>();
        newFileInfoMap = new HashMap<>();
    }


    public void upLoad(final String url, String key, final File file, UpLoadListener listener) {
        List<NewFileInfo> list = daoSession.getNewFileInfoDao().queryBuilder().where(NewFileInfoDao.Properties.Url.eq(file.getAbsolutePath())).list();
        final NewFileInfo info;
        if (list.size() == 0) {
            info = new NewFileInfo(file.getAbsolutePath(), file.getName(), 0, 0, 0, DownloadStatus.NORMAL, getDownLoadCacheDir());
        } else {
            info = list.get(0);
        }
        newFileInfoMap.put(file.getAbsolutePath(), info);
        Retrofit retrofit = BaseApplication.getAppComponent().getRetrofit();
        UpLoadProgressObserver upLoadProgressObserver = new UpLoadProgressObserver(info, listener);
        RequestBody requestBody = RequestBody.create(NetUtil.guessMimeType(file.getName()), file);
        UpLoadRequestBody upLoadRequestBody = new UpLoadRequestBody(upLoadProgressObserver, requestBody);
        MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), upLoadRequestBody);
        retrofit.create(UpLoadApi.class).upLoad(url, part).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .retryWhen(new RetryWhenNetworkException())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        addSubscription(disposable, file.getAbsolutePath());
                    }
                }).map(new Function<Response, NewFileInfo>() {
            @Override
            public NewFileInfo apply(@NonNull Response response) throws Exception {
                return info;
            }
        }).subscribe(upLoadProgressObserver);
    }


    public void upLoad(String url, Map<String, File> stringFileMap, List<UpLoadListener> listener) {
        if (url == null || stringFileMap == null || stringFileMap.size() == 0) {
            return;
        }
        if (stringFileMap.size() != listener.size()) {
            CommonLogger.e("设置的监听器和上传的文件的数据 不一致");
        }
        int temp = -1;
        for (Map.Entry<String, File> entry :
                stringFileMap.entrySet()) {
            temp++;
            upLoad(url, entry.getKey(), entry.getValue(), listener.get(temp));
        }
    }


    public void downLoad(final String url, DownloadListener listener) {
        if (url == null) {
            return;
        }
        NewFileInfo info;
        List<NewFileInfo> list = daoSession.getNewFileInfoDao().queryBuilder().where(NewFileInfoDao.Properties.Url.eq(url)).list();
        if (list.size() > 0) {
            info = list.get(0);
        } else {
            info = new NewFileInfo(url, FileUtil.clipFileName(url), 0, 0, 0, DownloadStatus.NORMAL, getDownLoadCacheDir());
            daoSession.getNewFileInfoDao().insert(info);
        }
        newFileInfoMap.put(url, info);
        Retrofit retrofit;
        DownLoadProgressObserver downLoadProgressObserver = new DownLoadProgressObserver(info, listener);
        if (stringRetrofitMap.containsKey(url)) {
            retrofit = stringRetrofitMap.get(url);
        } else {
            OkHttpClient.Builder builder = BaseApplication.getAppComponent().getOkHttpClientBuilder();
            builder.addInterceptor(new DownLoadInterceptor(downLoadProgressObserver));
            retrofit = new Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(BaseApplication.getAppComponent().getGson()))
                    .client(builder.build()).baseUrl(AppUtil.getBasUrl(url)).build();
            stringRetrofitMap.put(url, retrofit);
        }
        retrofit.create(DownLoadApi.class).downLoad("bytes=" + info.getLoadBytes() + "-", url).subscribeOn(Schedulers.io()).map(new Function<Response, NewFileInfo>() {
            @Override
            public NewFileInfo apply(@NonNull Response response) throws Exception {
                return writeCaches(response.body(), url);
            }
        }).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).retryWhen(new RetryWhenNetworkException()).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(@NonNull Disposable disposable) throws Exception {
                addSubscription(disposable, url);
            }
        }).subscribe(downLoadProgressObserver);
    }

    private void addSubscription(Disposable disposable, String url) {
        if (compositeDisposableMap.get(url) != null) {
            compositeDisposableMap.get(url).add(disposable);
        } else {
            //一次性容器,可以持有多个并提供 添加和移除。
            CompositeDisposable disposables = new CompositeDisposable();
            disposables.add(disposable);
            compositeDisposableMap.put(url, disposables);
        }
    }


    public void unSubscrible(String key) {
        if (compositeDisposableMap == null) {
            return;
        }
        if (!compositeDisposableMap.containsKey(key)) {
            return;
        }
        if (compositeDisposableMap.get(key) != null) {
            compositeDisposableMap.get(key).dispose();
        }
        compositeDisposableMap.remove(key);
    }


    private String getDownLoadCacheDir() {
        return BaseApplication.getAppComponent().getCacheFile().getAbsolutePath();
    }


    public void clearAllCache() {
        if (compositeDisposableMap != null) {
            compositeDisposableMap.clear();
        }
        if (stringRetrofitMap != null) {
            stringRetrofitMap.clear();
        }
    }


    /**
     * 写入文件
     */
    private NewFileInfo writeCaches(ResponseBody responseBody, String url) {
        NewFileInfo info = daoSession.getNewFileInfoDao().queryBuilder().where(NewFileInfoDao.Properties.Url.eq(url)).build().list().get(0);
        try {
            RandomAccessFile randomAccessFile = null;
            FileChannel channelOut = null;
            InputStream inputStream = null;
            try {
                info.setStatus(DownloadStatus.DOWNLOADING);
                File file = new File(info.getPath(), info.getName());
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                if (!file.exists()) {
                    file.createNewFile();
                }
                long allLength = 0 == info.getTotalBytes() ? responseBody.contentLength() : info.getLoadBytes() + responseBody
                        .contentLength();

                inputStream = responseBody.byteStream();
                randomAccessFile = new RandomAccessFile(file, "rwd");
                channelOut = randomAccessFile.getChannel();
                MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE,
                        info.getLoadBytes(), allLength - info.getLoadBytes());
                byte[] buffer = new byte[1024 * 4];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    mappedBuffer.put(buffer, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (channelOut != null) {
                    channelOut.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }


    public void stop(String url) {
        if (url == null) {
            return;
        }
        NewFileInfo info;
        info = newFileInfoMap.get(url);
        if (info != null) {
            info.setStatus(DownloadStatus.STOP);
            unSubscrible(url);
        }
        daoSession.getNewFileInfoDao().update(info);
    }

    public void cancel(String url) {
        if (url == null) {
            return;
        }
        NewFileInfo newFileInfo = newFileInfoMap.get(url);
        if (newFileInfo != null) {
            newFileInfo.setStatus(DownloadStatus.CANCEL);
            unSubscrible(url);
        }
        daoSession.getNewFileInfoDao().update(newFileInfo);
    }
}
