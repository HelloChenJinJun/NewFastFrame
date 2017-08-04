package com.example.commonlibrary.net;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.net.db.DaoSession;
import com.example.commonlibrary.net.db.NewFileInfo;
import com.example.commonlibrary.net.model.DownloadStatus;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by COOTEK on 2017/8/4.
 */

public class UpLoadProgressObserver implements UpLoadProgressListener, Observer<NewFileInfo> {


    private NewFileInfo newFileInfo;
    private DaoSession daoSession;
    private UpLoadListener listener;

    public UpLoadProgressObserver(NewFileInfo newFileInfo, UpLoadListener listener) {
        this.newFileInfo = newFileInfo;
        this.listener = listener;
        daoSession = BaseApplication.getAppComponent().getDaoSesion();
    }

    @Override
    public void onUpdate(long hasUpLoadSize, final long totalUpLoadSize) {
        Flowable.just(hasUpLoadSize).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long aLong) throws Exception {
                newFileInfo.setLoadBytes(aLong.intValue());
                newFileInfo.setTotalBytes((int) totalUpLoadSize);
                if (newFileInfo.getStatus() == DownloadStatus.STOP) {
                    daoSession.getNewFileInfoDao().update(newFileInfo);
                    listener.onStop(newFileInfo);
                    return;
                }
                if (newFileInfo.getStatus() == DownloadStatus.CANCEL) {
                    daoSession.getNewFileInfoDao().update(newFileInfo);
                    listener.onCancel(newFileInfo);
                    return;
                }
                newFileInfo.setStatus(DownloadStatus.DOWNLOADING);
                daoSession.getNewFileInfoDao().update(newFileInfo);
                listener.onUpdate(newFileInfo);
            }
        });
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        newFileInfo.setStatus(DownloadStatus.START);
        daoSession.getNewFileInfoDao().update(newFileInfo);
        listener.onStart(newFileInfo);
    }

    @Override
    public void onNext(@NonNull NewFileInfo newFileInfo) {

    }

    @Override
    public void onError(@NonNull Throwable e) {
        newFileInfo.setStatus(DownloadStatus.ERROR);
        daoSession.getNewFileInfoDao().update(newFileInfo);
        listener.onError(newFileInfo, e.getMessage());
    }

    @Override
    public void onComplete() {
        newFileInfo.setStatus(DownloadStatus.COMPLETE);
        daoSession.getNewFileInfoDao().update(newFileInfo);
        listener.onStart(newFileInfo);
    }
}
