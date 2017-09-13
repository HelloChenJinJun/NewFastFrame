package com.example.commonlibrary.net.upload;


import com.example.commonlibrary.net.download.FileInfo;

/**
 * Created by COOTEK on 2017/8/4.
 */

public interface UpLoadListener  {
    /**
     * 开始上传
     * @param fileInfo
     */
    void onStart(FileInfo fileInfo);
    /**
     * 更新上传进度
     * @param fileInfo
     */
    void onUpdate(FileInfo fileInfo);
    /**
     * 停止上传
     * @param fileInfo
     */
    void onStop(FileInfo fileInfo);
    /**
     * 上传成功
     * @param fileInfo
     */
    void onComplete(FileInfo fileInfo);
    /**
     * 取消上传
     * @param fileInfo
     */
    void onCancel(FileInfo fileInfo);
    /**
     * 上传失败
     * @param fileInfo
     */
    void onError(FileInfo fileInfo, String errorMsg);

}
