package com.example.chat.listener;

import com.example.chat.bean.post.PublicPostBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/31     11:10
 * QQ:         1981367757
 */

public interface OnCreatePublicPostListener {
    public void onSuccess(PublicPostBean publicPostBean);
    public void onFailed(String errorMsg,int errorCode,PublicPostBean publicPostBean);
}
