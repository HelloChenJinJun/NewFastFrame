package com.example.chat.listener;

import com.example.chat.bean.BaseMessage;

import cn.bmob.v3.exception.BmobException;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/10/10      19:56
 * QQ:             1981367757
 */
public interface OnReceiveListener {
        void onSuccess(BaseMessage baseMessage);

        void onFailed(BmobException e);

}
