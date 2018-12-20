package com.snew.video.bean;

import java.io.Serializable;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/20     17:17
 */
public class CommonVideoBean implements Serializable {
    private String id;
    private String url;
    private String image;
    private int videoType;

    //    现在播放的集数,默认为第一集
    private int playNumber = 1;


    public int getPlayNumber() {
        return playNumber;
    }

    public void setPlayNumber(int playNumber) {
        this.playNumber = playNumber;
    }

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getVideoType() {
        return videoType;
    }

    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }
}
