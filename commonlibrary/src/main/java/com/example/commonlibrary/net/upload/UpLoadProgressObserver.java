package com.example.commonlibrary.net.upload;

import com.example.commonlibrary.net.download.DownloadStatus;
import com.example.commonlibrary.net.download.FileDAOImpl;
import com.example.commonlibrary.net.download.FileInfo;

import io.reactivex.Flowable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by COOTEK on 2017/8/4.
 */

public class UpLoadProgressObserver implements UpLoadProgressListener, Observer<FileInfo> {


    private FileInfo fileInfo;
    private FileDAOImpl daoSession;
    private UpLoadListener listener;

    public UpLoadProgressObserver(FileInfo FileInfo, UpLoadListener listener) {
        this.fileInfo = FileInfo;
        this.listener = listener;
        daoSession =FileDAOImpl.getInstance();
    }

    @Override
    public void onUpdate(long hasUpLoadSize, final long totalUpLoadSize) {
        Flowable.just(hasUpLoadSize).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long aLong) throws Exception {
                fileInfo.setLoadBytes(aLong.intValue());
                fileInfo.setTotalBytes((int) totalUpLoadSize);
                if (fileInfo.getStatus() == DownloadStatus.STOP) {
                    daoSession.update(fileInfo);
                    listener.onStop(fileInfo);
                    return;
                }
                if (fileInfo.getStatus() == DownloadStatus.CANCEL) {
                    daoSession.update(fileInfo);
                    listener.onCancel(fileInfo);
                    return;
                }
                fileInfo.setStatus(DownloadStatus.DOWNLOADING);
                daoSession.update(fileInfo);
                listener.onUpdate(fileInfo);
            }
        });
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        fileInfo.setStatus(DownloadStatus.START);
        daoSession.update(fileInfo);
        listener.onStart(fileInfo);
    }

    @Override
    public void onNext(@NonNull FileInfo FileInfo) {

    }

    @Override
    public void onError(@NonNull Throwable e) {
        fileInfo.setStatus(DownloadStatus.ERROR);
        daoSession.update(fileInfo);
        listener.onError(fileInfo, e.getMessage());
    }

    @Override
    public void onComplete() {
        fileInfo.setStatus(DownloadStatus.COMPLETE);
        daoSession.update(fileInfo);
        listener.onStart(fileInfo);
    }
}
