package com.example.chat.events;

import com.example.chat.bean.post.PublicPostBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/4/4     16:29
 * QQ:         1981367757
 */

public class UpdatePostEvent {
    private PublicPostBean publicPostBean;

    public UpdatePostEvent(PublicPostBean publicPostBean) {
        this.publicPostBean = publicPostBean;
    }

    public PublicPostBean getPublicPostBean() {
        return publicPostBean;
    }
}
