package com.example.chat.bean.post;

import com.example.chat.bean.User;

import cn.bmob.v3.BmobObject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/3     13:12
 * QQ:         1981367757
 */

public class PublicCommentBean extends BmobObject {

    private PublicPostBean post;
    private User user;
    private String content;

    private Integer sendStatus;

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public PublicPostBean getPost() {
        return post;
    }

    public void setPost(PublicPostBean post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public boolean equals(Object obj) {
        return obj!=null&&obj instanceof PublicCommentBean&& ((PublicCommentBean) obj).getObjectId().equals(getObjectId());
    }

    public void setCreatedTime(String createdTime) {
        setCreatedAt(createdTime);
    }

    public void setCreateTime(String time) {
        setCreatedAt(time);
    }
}
