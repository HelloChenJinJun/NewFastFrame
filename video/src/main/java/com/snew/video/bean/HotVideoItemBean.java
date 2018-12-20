package com.snew.video.bean;

import java.io.Serializable;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/14     19:55
 */
public class HotVideoItemBean implements Serializable {
    private String title;
    private String id;

    private String url;


    private int videoType;

    public int getVideoType() {
        return videoType;
    }

    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }

    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setId(String id) {
        this.id = id;
    }
}
