package com.example.chat.listener;

import com.example.chat.bean.GroupTableMessage;

import cn.bmob.v3.exception.BmobException;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/31     11:05
 * QQ:         1981367757
 */

public interface OnCreateGroupTableListener {
    public void done(GroupTableMessage groupTableMessage, BmobException e);
}
