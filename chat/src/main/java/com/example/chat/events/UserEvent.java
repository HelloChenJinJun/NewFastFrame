package com.example.chat.events;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/28     12:06
 * QQ:         1981367757
 */

public class UserEvent {
    private String uid;
    private int action;

    public static final int ACTION_DELETE=1;
    public static final int ACTION_ADD=2;

    public UserEvent(String uid, int action) {
        this.uid = uid;
        this.action = action;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
