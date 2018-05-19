package com.example.chat.bean;

import com.example.chat.bean.post.PostDataBean;
import com.example.chat.bean.post.PublicCommentBean;
import com.example.chat.bean.post.PublicPostBean;

import cn.bmob.v3.BmobObject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/5/18     20:28
 */

public class CommentNotifyBean extends BmobObject{

    private PublicCommentBean publicCommentBean;

    private User user;
    private Integer readStatus;


    public PublicCommentBean getPublicCommentBean() {
        return publicCommentBean;
    }

    public void setPublicCommentBean(PublicCommentBean publicCommentBean) {
        this.publicCommentBean = publicCommentBean;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }
}
