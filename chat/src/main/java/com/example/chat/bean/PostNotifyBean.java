package com.example.chat.bean;

import com.example.chat.bean.post.PublicCommentBean;
import com.example.chat.bean.post.PublicPostBean;
import com.example.chat.manager.UserManager;

import cn.bmob.v3.BmobObject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/5/21     12:48
 */

public class PostNotifyBean extends BmobObject{
    private Integer type;


    private Integer readStatus;


    private PublicPostBean publicPostBean;


    private PublicCommentBean publicCommentBean;


    private User toUser;


    private User relatedUser;


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }

    public PublicPostBean getPublicPostBean() {
        return publicPostBean;
    }

    public void setPublicPostBean(PublicPostBean publicPostBean) {
        this.publicPostBean = publicPostBean;
    }

    public PublicCommentBean getPublicCommentBean() {
        return publicCommentBean;
    }

    public void setPublicCommentBean(PublicCommentBean publicCommentBean) {
        this.publicCommentBean = publicCommentBean;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public User getRelatedUser() {
        return relatedUser;
    }

    public void setRelatedUser(User relatedUser) {
        this.relatedUser = relatedUser;
    }
}
