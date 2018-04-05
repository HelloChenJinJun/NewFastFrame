package com.example.commonlibrary.bean.chat;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;


/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/25     23:12
 * QQ:         1981367757
 */
@Entity
public class PostCommentEntity {
    //    评论ID
    @Id
    private String cid;
    //    关联的帖子ID
    private String pid;
    //    写评论作者ID
    private String uid;
    //    评论内容
    private String content;

    private long createdTime;

    private long updatedTime;

    private int sendStatus;



    @Generated(hash = 549348361)
    public PostCommentEntity(String cid, String pid, String uid, String content,
            long createdTime, long updatedTime, int sendStatus) {
        this.cid = cid;
        this.pid = pid;
        this.uid = uid;
        this.content = content;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
        this.sendStatus = sendStatus;
    }


    public int getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(int sendStatus) {
        this.sendStatus = sendStatus;
    }

    @Generated(hash = 699674789)
    public PostCommentEntity() {
    }


    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }
}
