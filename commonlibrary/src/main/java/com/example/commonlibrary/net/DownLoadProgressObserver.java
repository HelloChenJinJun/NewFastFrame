package com.example.commonlibrary.net;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.DownloadStatus;

import io.reactivex.Flowable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by COOTEK on 2017/8/4.
 */

class DownLoadProgressObserver implements Observer<FileInfo>, DownloadProgressListener {

    private FileInfo fileInfo;
    private DownloadListener listener;
    private FileDAOImpl fileDAO;

    public DownLoadProgressObserver(FileInfo FileInfo,DownloadListener listener) {
        this.fileInfo = FileInfo;
        this.listener=listener;
        fileDAO=FileDAOImpl.getInstance();
    }




    @Override
    public void onSubscribe(@NonNull Disposable d) {
        fileInfo.setStatus(DownloadStatus.START);
        listener.onStart(fileInfo);
        fileDAO.update(fileInfo);
    }

    @Override
    public void onNext(@NonNull FileInfo fileInfo) {
        fileDAO.insert(fileInfo);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        fileInfo.setStatus(DownloadStatus.ERROR);
        fileDAO.update(fileInfo);
        listener.onError(fileInfo, e.getMessage());
    }

    @Override
    public void onComplete() {
        fileInfo.setStatus(DownloadStatus.COMPLETE);
        fileDAO.update(fileInfo);
        listener.onComplete(fileInfo);
    }

    @Override
    public void update(long read, final long count, boolean done) {
        Flowable.just(read).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        fileInfo.setLoadBytes(aLong.intValue());
                        fileInfo.setTotalBytes((int) count);
                        if (fileInfo.getStatus() == DownloadStatus.STOP) {
                            fileDAO.update(fileInfo);
                            listener.onStop(fileInfo);
                            return;
                        }
                        if (fileInfo.getStatus() == DownloadStatus.CANCEL) {
                            fileDAO.update(fileInfo);
                            listener.onCancel(fileInfo);
                            return;
                        }
                        fileInfo.setStatus(DownloadStatus.DOWNLOADING);
                        fileDAO.update(fileInfo);
                        listener.onUpdate(fileInfo);
                    }
                });

    }
}
