package com.example.chat.events;

import com.example.chat.bean.post.PublicPostBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/29     11:48
 */
public class DeletePostEvent {
    private PublicPostBean mPublicPostBean;

    public DeletePostEvent(PublicPostBean publicPostBean) {
        mPublicPostBean = publicPostBean;
    }


    public PublicPostBean getPublicPostBean() {
        return mPublicPostBean;
    }
}
