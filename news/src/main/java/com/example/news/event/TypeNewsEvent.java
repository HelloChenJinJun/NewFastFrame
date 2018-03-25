package com.example.news.event;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/29      23:29
 * QQ:             1981367757
 */

public class TypeNewsEvent {
    public static final  int DELETE=0;
    public static final int ADD=1;


    private int type;


    private String typeId;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public TypeNewsEvent(int type) {
        this.type = type;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
