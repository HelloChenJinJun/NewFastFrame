package com.example.chat.bean.post;

import com.example.chat.bean.User;
import com.example.commonlibrary.baseadapter.baseitem.MultipleItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/30     14:34
 * QQ:         1981367757
 */

public class PublicPostBean extends BmobObject implements MultipleItem {
    private Integer msgType;
    private String content;
    private User author;
    //    User
    private BmobRelation likes;
    //    User
    private BmobRelation share;
    private Integer likeCount;
    private Integer commentCount;
    private Integer shareCount;

    private List<String> likeList;


    public List<String> getLikeList() {
        if (likeList == null) {
            likeList=new ArrayList<>();
        }
        return likeList;
    }

    public void setLikeList(List<String> likeList) {
        this.likeList = likeList;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof PublicPostBean && ((PublicPostBean) obj).getObjectId().equals(getObjectId());
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


    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public Integer getLikeCount() {
        if (likeCount == null) {
            return 0;
        }
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getCommentCount() {
        if (commentCount == null) {
            return 0;
        }
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getShareCount() {
        if (shareCount == null) {
            return 0;
        }
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
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
