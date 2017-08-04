package com.example.commonlibrary.net;

import com.example.commonlibrary.net.db.NewFileInfo;

/**
 * Created by COOTEK on 2017/8/4.
 */

public interface UpLoadListener  {
    /**
     * 开始上传
     * @param NewFileInfo
     */
    void onStart(NewFileInfo NewFileInfo);
    /**
     * 更新上传进度
     * @param NewFileInfo
     */
    void onUpdate(NewFileInfo NewFileInfo);
    /**
     * 停止上传
     * @param NewFileInfo
     */
    void onStop(NewFileInfo NewFileInfo);
    /**
     * 上传成功
     * @param NewFileInfo
     */
    void onComplete(NewFileInfo NewFileInfo);
    /**
     * 取消上传
     * @param NewFileInfo
     */
    void onCancel(NewFileInfo NewFileInfo);
    /**
     * 上传失败
     * @param NewFileInfo
     */
    void onError(NewFileInfo NewFileInfo, String errorMsg);

}
