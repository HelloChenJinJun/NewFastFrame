package com.example.commonlibrary.bean.chat;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/26     22:09
 * QQ:         1981367757
 * <p>
 * 11个字段
 */
@Entity
public class GroupTableEntity {
    /**
     * 群名
     */
    private String groupName;
    /**
     * 群描述
     */
    private String groupDescription;
    /**
     * 群ID
     */
    @Id
    private String groupId;
    /**
     * 创建者ID
     */
    private String creatorId;
    /**
     * 创建时间
     */
    private long createdTime;
    /**
     * 群组成员
     */
    @Convert(columnType = String.class, converter = PublicPostEntity.StringConverter.class)
    private List<String> groupNumber;
    /**
     * 群头像
     */
    private String groupAvatar;

    /**
     * 读取状态
     */
    private int readStatus;
    /**
     * 发送状态
     */
    private int sendStatus;


    //    接受者ID
    private String toId;

    //群通知
    private String notification;



    private Boolean isRemind;



//    群通知
    public Boolean getRemind() {
        return isRemind;
    }

    public void setRemind(Boolean remind) {
        isRemind = remind;
    }

    @Generated(hash = 511628014)
    public GroupTableEntity(String groupName, String groupDescription, String groupId,
                            String creatorId, long createdTime, List<String> groupNumber, String groupAvatar,
                            int readStatus, int sendStatus, String toId, String notification,
                            Boolean isRemind) {
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.groupId = groupId;
        this.creatorId = creatorId;
        this.createdTime = createdTime;
        this.groupNumber = groupNumber;
        this.groupAvatar = groupAvatar;
        this.readStatus = readStatus;
        this.sendStatus = sendStatus;
        this.toId = toId;
        this.notification = notification;
        this.isRemind = isRemind;
    }

    @Generated(hash = 1107809041)
    public GroupTableEntity() {
    }


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public List<String> getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(List<String> groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getGroupAvatar() {
        return groupAvatar;
    }

    public void setGroupAvatar(String groupAvatar) {
        this.groupAvatar = groupAvatar;
    }


    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }

    public int getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(int sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public Boolean getIsRemind() {
        return this.isRemind;
    }

    public void setIsRemind(Boolean isRemind) {
        this.isRemind = isRemind;
    }
}
