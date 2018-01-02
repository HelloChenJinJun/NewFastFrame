package com.example.chat.bean;

import com.example.commonlibrary.baseadapter.baseitem.MultipleItem;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/30     14:34
 * QQ:         1981367757
 */

public class PublicPostBean extends BmobObject implements MultipleItem {
    private int msgType;
    private String content;
    private User author;
//    User
    private BmobRelation likes;
//    User
    private BmobRelation share;
    private int likeCount;
    private int commentCount;
    private int shareCount;


    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public BmobRelation getShare() {
        return share;
    }

    public void setShare(BmobRelation share) {
        this.share = share;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }



    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



    public BmobRelation getLikes() {
        return likes;
    }

    public void setLikes(BmobRelation likes) {
        this.likes = likes;
    }

    @Override
    public int getItemViewType() {
        return getMsgType();
    }
}
