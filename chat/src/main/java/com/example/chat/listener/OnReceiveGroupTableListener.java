package com.example.chat.listener;


import com.example.chat.bean.GroupTableMessage;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/15      23:14
 * QQ:             1981367757
 */

public interface OnReceiveGroupTableListener {
        void onSuccess(GroupTableMessage message);

        void onFailed(int i, String errorMsg);
}
