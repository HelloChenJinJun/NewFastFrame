package com.example.chat.listener;

import com.example.chat.bean.BaseMessage;

import cn.bmob.v3.exception.BmobException;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/10/16      20:17
 * QQ:             1981367757
 */
public interface SendFileListener {
        void onProgress(int progress);

        void onStart(BaseMessage message);

        void onSuccess();

        void onFailed(BmobException e);
}
