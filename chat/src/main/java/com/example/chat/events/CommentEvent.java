package com.example.chat.events;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/3     23:12
 * QQ:         1981367757
 */

public class CommentEvent {
    public static final int TYPE_POST = 2;
    private String id;
    private int type;
    private int action;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public static final int TYPE_LIKE=0;
    public static final int TYPE_COMMENT=1;



    public static final int ACTION_ADD=0;
    public static final int ACTION_DELETE=1;



    public CommentEvent(String id, int type,int action) {
        this.id = id;
        this.type = type;
        this.action=action;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
