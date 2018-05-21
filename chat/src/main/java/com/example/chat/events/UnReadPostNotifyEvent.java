package com.example.chat.events;

import com.example.chat.bean.PostNotifyBean;

import java.util.ArrayList;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/5/19     13:31
 */

public class UnReadPostNotifyEvent {
    private PostNotifyBean postNotifyBean;
    public UnReadPostNotifyEvent(PostNotifyBean postNotifyBean) {
        this.postNotifyBean=postNotifyBean;
    }


    public PostNotifyBean getPostNotifyBean() {
        return postNotifyBean;
    }
}
