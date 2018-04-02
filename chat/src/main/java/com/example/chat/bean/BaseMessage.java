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
        private Integer contentType;

       

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
        private Long createTime;

        public Integer getContentType() {
                return contentType;
        }

        public void setContentType(Integer contentType) {
                this.contentType = contentType;
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

        public Long getCreateTime() {
                return createTime;
        }

        public void setCreateTime(Long createTime) {
                this.createTime = createTime;
        }

        @Override
        public int getItemViewType() {
                if (getBelongId().equals(UserManager.getInstance().getCurrentUserObjectId())) {
                        if (getContentType().equals(Constant.TAG_MSG_TYPE_TEXT)) {
                                return ChatMessageAdapter.TYPE_SEND_TEXT;
                        } else if (getContentType().equals(Constant.TAG_MSG_TYPE_IMAGE)) {
                                return ChatMessageAdapter.TYPE_SEND_IMAGE;
                        } else if (getContentType().equals(Constant.TAG_MSG_TYPE_LOCATION)) {
                                return ChatMessageAdapter.TYPE_SEND_LOCATION;
                        } else if (getContentType().equals(Constant.TAG_MSG_TYPE_VOICE)){
                                return ChatMessageAdapter.TYPE_SEND_VOICE;
                        }else {
                                return ChatMessageAdapter.TYPE_SEND_VIDEO;
                        }
                } else {
                        if (getContentType().equals(Constant.TAG_MSG_TYPE_TEXT)) {
                                return ChatMessageAdapter.TYPE_RECEIVER_TEXT;
                        } else if (getContentType().equals(Constant.TAG_MSG_TYPE_VOICE)) {
                                return ChatMessageAdapter.TYPE_RECEIVER_VOICE;
                        } else if (getContentType().equals(Constant.TAG_MSG_TYPE_LOCATION)) {
                                return ChatMessageAdapter.TYPE_RECEIVER_LOCATION;
                        } else if (getContentType().equals(Constant.TAG_MSG_TYPE_IMAGE)){
                                return ChatMessageAdapter.TYPE_RECEIVER_IMAGE;
                        }else {
                                return ChatMessageAdapter.TYPE_RECEIVER_VIDEO;
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


        @Override
        public String toString() {
                return "BaseMessage{" +
                        "content='" + content + '\'' +
                        ", belongId='" + belongId + '\'' +
                        ", contentType=" + contentType +
                        ", readStatus=" + readStatus +
                        ", sendStatus=" + sendStatus +
                        ", createTime=" + createTime +
                        '}';
        }
}
