package com.example.chat.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/5/22     23:39
 */

public class SkinBean extends BmobObject{
    private String url;

    private String title;


    private List<String> imageList;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }
}
