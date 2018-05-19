package com.example.chat.bean;

import cn.bmob.v3.BmobObject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/16     14:17
 * QQ:         1981367757
 */

public class SystemNotifyBean extends BmobObject{
    private String imageUrl;
    private String title;
    private String subTitle;
    private String contentUrl;
    private Integer readStatus;



    @Override
    public boolean equals(Object obj) {
        return obj!=null&&obj instanceof SystemNotifyBean&& ((SystemNotifyBean) obj).getTitle().equals(getTitle());
    }

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
}
