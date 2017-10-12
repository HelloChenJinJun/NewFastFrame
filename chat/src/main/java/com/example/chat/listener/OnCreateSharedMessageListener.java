package com.example.chat.listener;


import com.example.chat.bean.SharedMessage;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/20      10:58
 * QQ:             1981367757
 */

public interface OnCreateSharedMessageListener {
        void onSuccess(SharedMessage message);

        void onFailed(int errorId, String errorMsg);
}
