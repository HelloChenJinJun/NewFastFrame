package com.example.chat.listener;

import com.example.chat.bean.BaseMessage;
import com.example.chat.bean.post.PublicPostBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/29     11:10
 * QQ:         1981367757
 */

public interface OnCreateChatMessageListener {
    public void onSuccess(BaseMessage baseMessage);
    public void onFailed(String errorMsg,BaseMessage baseMessage);
}
