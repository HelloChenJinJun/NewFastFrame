package com.example.chat.bean.post;

import java.io.Serializable;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/3     14:48
 * QQ:         1981367757
 */

public class CommentDetailBean implements Serializable{
    private String content;
    private String replyContent;
    private String publicId;
    private String replyAvatar;
    private String replyName;

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }



    public String getReplyAvatar() {
        return replyAvatar;
    }

    public void setReplyAvatar(String replyAvatar) {
        this.replyAvatar = replyAvatar;
    }

    public String getReplyName() {
        return replyName;
    }

    public void setReplyName(String replyName) {
        this.replyName = replyName;
    }
}
