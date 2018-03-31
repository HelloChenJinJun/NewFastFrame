package com.example.chat.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/16      16:33
 * QQ:             1981367757
 */

public class GroupTableMessage extends BmobObject {

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
        private List<String> groupNumber;
        /**
         * 群头像
         */
        private String groupAvatar;

        /**
         * 去读状态
         */
        private Integer readStatus;
        /**
         * 发送状态
         */
        private Integer sendStatus;

        private String toId;


        private String notification;

//        消息通知提醒
        private Boolean isRemind;


        public Boolean getRemind() {
                return isRemind;
        }

        public void setRemind(Boolean remind) {
                isRemind = remind;
        }

        public String getNotification() {
                return notification;
        }

        public void setNotification(String notification) {
                this.notification = notification;
        }



        public String getToId() {
                return toId;
        }

        public void setToId(String toId) {
                this.toId = toId;
        }

        public Integer getSendStatus() {
                return sendStatus;
        }

        public void setSendStatus(Integer sendStatus) {
                this.sendStatus = sendStatus;
        }

        public Integer getReadStatus() {
                return readStatus;
        }

        public void setReadStatus(Integer readStatus) {
                this.readStatus = readStatus;
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
}
