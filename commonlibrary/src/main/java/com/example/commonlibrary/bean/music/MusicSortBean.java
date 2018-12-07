package com.example.commonlibrary.bean.music;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/6     17:51
 */
@Entity
public class MusicSortBean {


    @Id
    private String url;

    private long playTime;

    @Generated(hash = 1678024472)
    public MusicSortBean(String url, long playTime) {
        this.url = url;
        this.playTime = playTime;
    }

    @Generated(hash = 593954311)
    public MusicSortBean() {
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getPlayTime() {
        return this.playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

}
