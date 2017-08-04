package com.example.commonlibrary.net;


import com.example.commonlibrary.net.db.NewFileInfo;

/**
 * 下载监听器
 */
public interface DownloadListener {
    /**
     * 开始下载
     * @param NewFileInfo
     */
    void onStart(NewFileInfo NewFileInfo);
    /**
     * 更新下载进度
     * @param NewFileInfo
     */
    void onUpdate(NewFileInfo NewFileInfo);
    /**
     * 停止下载
     * @param NewFileInfo
     */
    void onStop(NewFileInfo NewFileInfo);
    /**
     * 下载成功
     * @param NewFileInfo
     */
    void onComplete(NewFileInfo NewFileInfo);
    /**
     * 取消下载
     * @param NewFileInfo
     */
    void onCancel(NewFileInfo NewFileInfo);
    /**
     * 下载失败
     * @param NewFileInfo
     */
    void onError(NewFileInfo NewFileInfo, String errorMsg);
}
