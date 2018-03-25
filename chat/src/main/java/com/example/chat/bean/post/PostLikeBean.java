package com.example.chat.bean.post;

import com.example.chat.bean.User;

import cn.bmob.v3.BmobObject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/25     18:02
 * QQ:         1981367757
 */

public class PostLikeBean extends BmobObject {
    private User user;
    private PublicPostBean publicPostBean;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PublicPostBean getPublicPostBean() {
        return publicPostBean;
    }

    public void setPublicPostBean(PublicPostBean publicPostBean) {
        this.publicPostBean = publicPostBean;
    }
}
