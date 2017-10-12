package com.example.chat.listener;

import cn.bmob.v3.exception.BmobException;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/10/29      15:04
 * QQ:             1981367757
 */


/**
 * 发送文本消息的监听
 */

public interface OnSendMessageListener {
        void onSending();

        void onSuccess();

        void onFailed(BmobException e);
}
