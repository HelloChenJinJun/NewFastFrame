package com.example.chat.events;

import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.GroupChatMessage;
import com.example.chat.bean.GroupTableMessage;

import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/27     10:59
 * QQ:         1981367757
 */

public class MessageInfoEvent {
    private int messageType;
    private List<ChatMessage>  chatMessageList;
    private List<GroupChatMessage>  groupChatMessageList;
    private List<GroupTableMessage>  groupTableMessageList;



    public static final int TYPE_PERSON=0;
    public static final int TYPE_GROUP_CHAT=1;
    public static final int TYPE_GROUP_TABLE=2;


    public MessageInfoEvent(int  messageType) {
        this.messageType=messageType;
    }


    public int getMessageType() {
        return messageType;
    }

    public List<ChatMessage> getChatMessageList() {
        return chatMessageList;
    }

    public void setChatMessageList(List<ChatMessage> chatMessageList) {
        this.chatMessageList = chatMessageList;
    }

    public List<GroupChatMessage> getGroupChatMessageList() {
        return groupChatMessageList;
    }

    public void setGroupChatMessageList(List<GroupChatMessage> groupChatMessageList) {
        this.groupChatMessageList = groupChatMessageList;
    }

    public List<GroupTableMessage> getGroupTableMessageList() {
        return groupTableMessageList;
    }

    public void setGroupTableMessageList(List<GroupTableMessage> groupTableMessageList) {
        this.groupTableMessageList = groupTableMessageList;
    }
}
