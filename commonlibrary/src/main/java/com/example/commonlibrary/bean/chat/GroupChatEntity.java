package com.example.commonlibrary.bean.chat;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/26     22:33
 * QQ:         1981367757
 */
@Entity
public class GroupChatEntity {
    @Id(autoincrement = true)
    private Long id;

    private String groupId;
    private String belongId;
    private long createdTime;
    private int sendStatus;
    private int readStatus;
    private String content;
    private int contentType;

    @Generated(hash = 379243717)
    public GroupChatEntity(Long id, String groupId, String belongId,
            long createdTime, int sendStatus, int readStatus, String content,
            int contentType) {
        this.id = id;
        this.groupId = groupId;
        this.belongId = belongId;
        this.createdTime = createdTime;
        this.sendStatus = sendStatus;
        this.readStatus = readStatus;
        this.content = content;
        this.contentType = contentType;
    }

    @Generated(hash = 742152800)
    public GroupChatEntity() {
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

  

    public String getBelongId() {
        return belongId;
    }

    public void setBelongId(String belongId) {
        this.belongId = belongId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }


    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public int getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(int sendStatus) {
        this.sendStatus = sendStatus;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
