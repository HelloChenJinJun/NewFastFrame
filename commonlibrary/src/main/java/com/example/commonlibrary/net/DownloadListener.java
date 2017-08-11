package com.example.commonlibrary.net;



/**
 * 下载监听器
 */
public interface DownloadListener {
    /**
     * 开始下载
     * @param FileInfo
     */
    void onStart(FileInfo FileInfo);
    /**
     * 更新下载进度
     * @param FileInfo
     */
    void onUpdate(FileInfo FileInfo);
    /**
     * 停止下载
     * @param FileInfo
     */
    void onStop(FileInfo FileInfo);
    /**
     * 下载成功
     * @param FileInfo
     */
    void onComplete(FileInfo FileInfo);
    /**
     * 取消下载
     * @param FileInfo
     */
    void onCancel(FileInfo FileInfo);
    /**
     * 下载失败
     * @param FileInfo
     */
    void onError(FileInfo FileInfo, String errorMsg);
}
