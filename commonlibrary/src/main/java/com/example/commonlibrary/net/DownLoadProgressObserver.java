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
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by COOTEK on 2017/8/4.
 */

class DownLoadProgressObserver implements Observer<NewFileInfo>, DownloadProgressListener {

    private NewFileInfo newFileInfo;
    private DownloadListener listener;
    private DaoSession daoSession;

    public DownLoadProgressObserver(NewFileInfo newFileInfo,DownloadListener listener) {
        this.newFileInfo = newFileInfo;
        this.listener=listener;
        daoSession=BaseApplication.getAppComponent().getDaoSesion();
    }




    @Override
    public void onSubscribe(@NonNull Disposable d) {
        newFileInfo.setStatus(DownloadStatus.START);
        listener.onStart(newFileInfo);
        daoSession.getNewFileInfoDao().update(newFileInfo);
    }

    @Override
    public void onNext(@NonNull NewFileInfo newFileInfo) {
        BaseApplication.getAppComponent().getDaoSesion().getNewFileInfoDao().insertOrReplace(newFileInfo);
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
        daoSession.update(newFileInfo);
        listener.onComplete(newFileInfo);
    }

    @Override
    public void update(long read, final long count, boolean done) {
        Flowable.just(read).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        newFileInfo.setLoadBytes(aLong.intValue());
                        newFileInfo.setTotalBytes((int) count);
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
}
