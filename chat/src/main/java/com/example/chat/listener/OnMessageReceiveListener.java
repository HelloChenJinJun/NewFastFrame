package com.example.chat.listener;


import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.GroupChatMessage;
import com.example.chat.bean.GroupTableMessage;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/10/29      9:31
 * QQ:             1981367757
 */

public interface OnMessageReceiveListener {
        /**
         * 聊天消息的到来
         *
         * @param message 单聊消息主体
         */
        void onNewChatMessageCome(ChatMessage message);


        void onNewGroupChatMessageCome(GroupChatMessage message);

        /**
         * 回执消息的监听
         *
         * @param chatMessage
         */
        void onAskReadMessageCome(ChatMessage chatMessage);

        /**
         * 网络状态的改变
         *
         * @param isConnected
         */
        void onNetWorkChanged(boolean isConnected);

        /**
         * 邀请消息通知
         *
         * @param chatMessage
         */
        void onAddFriendMessageCome(ChatMessage chatMessage);

        /**
         * 同意消息通知
         *
         * @param chatMessage
         */
        void onAgreeMessageCome(ChatMessage chatMessage);

        /**
         * 下线通知
         */
        void onOffline();

        /**
         * @param message 群结构消息
         */
        void onGroupTableMessageCome(GroupTableMessage message);
}
