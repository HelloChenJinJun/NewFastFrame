package com.example.commonlibrary.rxbus.event;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/10     14:46
 */
public class SystemNotificationEvent {
    private String action;

    public SystemNotificationEvent(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
