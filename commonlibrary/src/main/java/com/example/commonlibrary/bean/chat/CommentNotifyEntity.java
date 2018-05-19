package com.example.commonlibrary.bean.chat;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/5/18     18:22
 */
@Entity
public class CommentNotifyEntity {

    @Id
    private String commentId;
    private int readStatus;





    @Generated(hash = 544438365)
    public CommentNotifyEntity(String commentId, int readStatus) {
        this.commentId = commentId;
        this.readStatus = readStatus;
    }

    @Generated(hash = 696255289)
    public CommentNotifyEntity() {
    }





    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }
}
