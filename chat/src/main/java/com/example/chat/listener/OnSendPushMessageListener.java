package com.example.chat.listener;

import cn.bmob.v3.exception.BmobException;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/10/28      20:03
 * QQ:             1981367757
 */

public interface OnSendPushMessageListener {
        void onSuccess();

        void onFailed(BmobException e);

}
