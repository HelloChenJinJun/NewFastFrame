package com.example.chat.bean.post;

import com.example.commonlibrary.bean.chat.PublicPostEntity;

import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/30     16:35
 * QQ:         1981367757
 */

public class PostDataBean {
    private List<String> imageList;
    private String content;
//    分享的内容PublicPostEntity的json内容
    private String shareContent;
//    定位
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }




    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }
}
