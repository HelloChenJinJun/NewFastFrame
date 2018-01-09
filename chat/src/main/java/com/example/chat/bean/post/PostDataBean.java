package com.example.chat.bean.post;

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
    private String voiceUrl;
//    分享的内容
    private ShareTypeContent shareContent;
//    分享的内容类型
    private int shareType;

//    定位
    private String location;


    public ShareTypeContent getShareContent() {
        return shareContent;
    }

    public void setShareContent(ShareTypeContent shareContent) {
        this.shareContent = shareContent;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getShareType() {
        return shareType;
    }

    public void setShareType(int shareType) {
        this.shareType = shareType;
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

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

}
