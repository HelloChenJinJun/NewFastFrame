package com.example.chat.listener;


import com.example.chat.bean.SharedMessage;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/11      18:46
 * QQ:             1981367757
 */

public interface OnShareMessageReceivedListener {

        void onAddLiker(String id, String uid);

        void onDeleteLiker(String id, String uid);

        void onDeleteCommentMessage(String id, String content);

        void onAddCommentMessage(String id, String content);

        void onAddShareMessage(SharedMessage sharedMessage);

        void onDeleteShareMessage(String id);
}
