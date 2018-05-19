package com.example.commonlibrary.bean.chat;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/5/17     23:25
 */
@Entity
public class SystemNotifyEntity {

    private String imageUrl;
    private String title;
    private String subTitle;
    private String contentUrl;


    @Generated(hash = 322112774)
    public SystemNotifyEntity(String imageUrl, String title, String subTitle,
            String contentUrl) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.subTitle = subTitle;
        this.contentUrl = contentUrl;
    }

    @Generated(hash = 1835862813)
    public SystemNotifyEntity() {
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
