package com.example.chat.events;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/28     14:51
 * QQ:         1981367757
 */

public class RecentEvent {


    public static final  int ACTION_ADD=1;
    public static final int ACTION_DELETE=2;
    public int action;


    public int getAction() {
        return action;
    }



    private String id;


    public RecentEvent(String id,int action) {
        this.id = id;
        this.action=action;
    }

    public String getId() {
        return id;
    }
}
