package com.example.commonlibrary.net.download;

import com.example.commonlibrary.utils.CommonLogger;

import io.reactivex.Flowable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by COOTEK on 2017/8/4.
 */

 public class DownLoadProgressObserver implements Observer<FileInfo>, DownloadProgressListener {

    private FileInfo fileInfo;
    private DownloadListener listener;
    private FileDAOImpl fileDAO;

    public DownLoadProgressObserver(FileInfo FileInfo, DownloadListener listener) {
        this.fileInfo = FileInfo;
        this.listener = listener;
        fileDAO = FileDAOImpl.getInstance();
    }


    @Override
    public void onSubscribe(@NonNull Disposable d) {
        if (fileInfo == null) {
            CommonLogger.e("onSubscribe出错");
        }
        fileInfo.setStatus(DownloadStatus.START);
        listener.onStart(fileInfo);
        fileDAO.update(fileInfo);
    }

    @Override
    public void onNext(@NonNull FileInfo fileInfo) {
        fileDAO.update(fileInfo);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (fileInfo == null) {
            CommonLogger.e("onError出错");
        }
        fileInfo.setStatus(DownloadStatus.ERROR);
        fileDAO.update(fileInfo);
        listener.onError(fileInfo, e.getMessage());
    }

    @Override
    public void onComplete() {
        if (fileInfo == null) {
            CommonLogger.e("onComplete出错");
        }
        fileInfo.setStatus(DownloadStatus.COMPLETE);
        fileDAO.update(fileInfo);
        listener.onComplete(fileInfo);
    }

    @Override
    public void update(long read, final long count, boolean done) {
        Flowable.just(read).observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
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
                    if (fileInfo == null) {
                        CommonLogger.e("下载这里出错");
                    }
                    fileInfo.setStatus(DownloadStatus.DOWNLOADING);
                    fileDAO.update(fileInfo);
                    listener.onUpdate(fileInfo);
                });

    }
}
