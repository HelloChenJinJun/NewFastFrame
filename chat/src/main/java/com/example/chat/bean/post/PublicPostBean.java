package com.example.chat.bean.post;

import com.example.chat.base.Constant;
import com.example.chat.bean.User;
import com.example.commonlibrary.baseadapter.baseitem.MultipleItem;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

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
    private Integer likeCount;
    private Integer commentCount;
    private Integer shareCount;
    private List<String> likeList;
//    定位信息 是LocationEvent的json
    private String location;
    //    上传是否成功
    private Integer sendStatus;


    public Integer getSendStatus() {
        if (sendStatus == null) {
            return Constant.SEND_STATUS_SUCCESS;
        }
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

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


    @Override
    public int getItemViewType() {
        return getMsgType();
    }

    public void setCreateTime(String time) {
        setCreatedAt(time);
    }
}
