package com.example.chat.listener;

import com.example.chat.bean.ChatMessage;

import cn.bmob.v3.exception.BmobException;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/6/3      11:20
 * QQ:             1981367757
 */

public interface OnSendTagMessageListener {
        public void onSuccess(ChatMessage chatMessage);
        public void onFailed(BmobException e);


}
