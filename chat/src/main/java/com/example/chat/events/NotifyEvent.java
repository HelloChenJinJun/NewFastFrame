package com.example.chat.events;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/11     14:01
 * QQ:         1981367757
 */

public class NotifyEvent {

    public static final int TYPE_NOTIFY_POST=0;

    private int type;


    public NotifyEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
