package com.example.chat.bean;


import com.example.chat.adapter.ChatMessageAdapter;
import com.example.chat.base.Constant;
import com.example.chat.manager.UserManager;
import com.example.commonlibrary.baseadapter.baseitem.MultipleItem;

import cn.bmob.v3.BmobObject;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/16      22:21
 * QQ:             1981367757
 */

public class BaseMessage extends BmobObject implements MultipleItem {

        /**
         * 会话类型:群聊或单聊
         */
        private String conversationType;


        /**
         * 发送者头像
         */
        private String belongAvatar;
        /**
         * 发送者昵称
         */
        private String belongNick;
        /**
         * 发送者用户名
         */
        private String belongUserName;

        /**
         * 发送的消息内容
         */
        private String content;
        /**
         * 发送方的ID
         */
        private String belongId;

        /**
         * 消息的类型
         */
        private Integer msgType;

        /**
         * 消息读取的状态
         */
        private Integer readStatus;

        /**
         * 消息发送的状态
         */
        private Integer sendStatus;

        /**
         * 消息创建的时间
         */
        private String createTime;

        public String getBelongAvatar() {
                return belongAvatar;
        }

        public void setBelongAvatar(String belongAvatar) {
                this.belongAvatar = belongAvatar;
        }

        public String getBelongNick() {
                return belongNick;
        }

        public void setBelongNick(String belongNick) {
                this.belongNick = belongNick;
        }

        public String getBelongUserName() {
                return belongUserName;
        }

        public void setBelongUserName(String belongUserName) {
                this.belongUserName = belongUserName;
        }

        public String getContent() {
                return content;
        }

        public void setContent(String content) {
                this.content = content;
        }

        public String getBelongId() {
                return belongId;
        }

        public void setBelongId(String belongId) {
                this.belongId = belongId;
        }

        public Integer getMsgType() {
                return msgType;
        }

        public void setMsgType(Integer msgType) {
                this.msgType = msgType;
        }

        public Integer getReadStatus() {
                return readStatus;
        }

        public void setReadStatus(Integer readStatus) {
                this.readStatus = readStatus;
        }

        public Integer getSendStatus() {
                return sendStatus;
        }

        public void setSendStatus(Integer sendStatus) {
                this.sendStatus = sendStatus;
        }

        public String getCreateTime() {
                return createTime;
        }

        public void setCreateTime(String createTime) {
                this.createTime = createTime;
        }

        public String getConversationType() {
                return conversationType;
        }

        public void setConversationType(String conversationType) {
                this.conversationType = conversationType;
        }


        @Override
        public int getItemViewType() {
                if (getBelongId().equals(UserManager.getInstance().getCurrentUserObjectId())) {
                        if (getMsgType().equals(Constant.TAG_MSG_TYPE_TEXT)) {
                                return ChatMessageAdapter.TYPE_SEND_TEXT;
                        } else if (getMsgType().equals(Constant.TAG_MSG_TYPE_IMAGE)) {
                                return ChatMessageAdapter.TYPE_SEND_IMAGE;
                        } else if (getMsgType().equals(Constant.TAG_MSG_TYPE_LOCATION)) {
                                return ChatMessageAdapter.TYPE_SEND_LOCATION;
                        } else {
                                return ChatMessageAdapter.TYPE_SEND_VOICE;
                        }
                } else {
                        if (getMsgType().equals(Constant.TAG_MSG_TYPE_TEXT)) {
                                return ChatMessageAdapter.TYPE_RECEIVER_TEXT;
                        } else if (getMsgType().equals(Constant.TAG_MSG_TYPE_VOICE)) {
                                return ChatMessageAdapter.TYPE_RECEIVER_VOICE;
                        } else if (getMsgType().equals(Constant.TAG_MSG_TYPE_LOCATION)) {
                                return ChatMessageAdapter.TYPE_RECEIVER_LOCATION;
                        } else {
                                return ChatMessageAdapter.TYPE_RECEIVER_IMAGE;
                        }
                }
        }


        @Override
        public boolean equals(Object obj) {
                if (obj instanceof BaseMessage) {
                        BaseMessage baseMessage = (BaseMessage) obj;
                        return baseMessage.getCreateTime().equals(getCreateTime()) && baseMessage.getBelongId().equals(getBelongId());
                }
                return false;
        }
}
