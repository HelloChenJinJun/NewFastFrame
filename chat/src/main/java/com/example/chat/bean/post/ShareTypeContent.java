package com.example.chat.bean.post;

import com.example.chat.bean.PublicPostBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/9     12:08
 * QQ:         1981367757
 */

public class ShareTypeContent  {
//    文章内容
    private PostDataBean postDataBean;
//    文章ID
    private String pid;
//    文章点赞个数
    private Integer likeCount;
//    文章评论个数
    private Integer commentCount;
//    文章转发个数
    private Integer shareCount;


//    文章创建时间
    private String createAt;

//    用户ID
    private String uid;
//   用户头像
    private String avatar;
//    用户昵称
    private String nick;
//    用户性别
    private boolean sex;


//    用户定位
    private String address;


    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public PostDataBean getPostDataBean() {
        return postDataBean;
    }

    public void setPostDataBean(PostDataBean postDataBean) {
        this.postDataBean = postDataBean;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
