package com.example.commonlibrary.net.db;

import com.example.commonlibrary.net.entity.FileInfo;

/**
 * Created by COOTEK on 2017/8/3.
 */

public interface NewFileDAO {
    /**
     * 插入线程信息
     * @param info
     * @return void
     */
    void insert(NewFileInfo info);
    /**
     * 删除线程信息
     * @param url
     * @return void
     */
    void delete(String url);
    /**
     * 更新线程下载进度
     * @param info
     */
    void update(NewFileInfo info);
    /**
     * 查询文件的线程信息
     * @param url
     * @return
     */
    NewFileInfo query(String url);
    /**
     * 线程信息是否存在
     * @param url
     * @return boolean
     */
    boolean isExists(String url);
}
