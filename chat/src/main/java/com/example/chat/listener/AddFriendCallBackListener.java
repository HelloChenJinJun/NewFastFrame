package com.example.chat.listener;

import com.example.chat.bean.User;

import cn.bmob.v3.exception.BmobException;

/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/25      11:00
 * QQ:             1981367757
 */
public interface AddFriendCallBackListener {
        void onSuccess(User user);

        void onFailed(BmobException e);
}
