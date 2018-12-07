package com.example.commonlibrary.bean.chat;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/26     22:41
 * QQ:         1981367757
 * 单聊消息  9
 */
@Entity
public class ChatMessageEntity {

    @Id(autoincrement = true)
    private Long id;


    private String belongId;
    private String toId;
    private long createdTime;
    private int sendStatus;
    private int readStatus;
    private String content;
    private int contentType;
    /**
     * 消息标签  ADD / AGREE / READED 等
     */
    private int messageType;
    /**
     * 会话ID
     * 组成部分为 ： 发送者ID&接受者ID
     */

    private String conversationId;

    @Generated(hash = 1958620565)
    public ChatMessageEntity(Long id, String belongId, String toId,
                             long createdTime, int sendStatus, int readStatus, String content,
                             int contentType, int messageType, String conversationId) {
        this.id = id;
        this.belongId = belongId;
        this.toId = toId;
        this.createdTime = createdTime;
        this.sendStatus = sendStatus;
        this.readStatus = readStatus;
        this.content = content;
        this.contentType = contentType;
        this.messageType = messageType;
        this.conversationId = conversationId;
    }

    @Generated(hash = 1666122499)
    public ChatMessageEntity() {
    }

    public String getBelongId() {
        return belongId;
    }

    public void setBelongId(String belongId) {
        this.belongId = belongId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
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

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj!=null&&obj instanceof ChatMessageEntity){
            ChatMessageEntity item= (ChatMessageEntity) obj;
            return item.getConversationId().equals(getConversationId())
                    &&item.getCreatedTime()==getCreatedTime()
                    &&item.getMessageType()==getMessageType();
        }
            return false;
        }
}
