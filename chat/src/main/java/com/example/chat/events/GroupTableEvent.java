package com.example.chat.events;

import com.example.chat.base.Constant;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/30     17:12
 * QQ:         1981367757
 */

public class GroupTableEvent {
    public static final int TYPE_GROUP_NAME=1;
    public static final int TYPE_GROUP_DESCRIPTION=2;
    public static final int TYPE_GROUP_NOTIFICATION=3;
    public static final int TYPE_GROUP_AVATAR=4;
    public static final int TYPE_GROUP_NUMBER=5;
    private String groupId;
    private int type;
    private String content;
    public static final int ACTION_ADD=1;
    public static final int ACTION_DELETE=2;
//    删除和添加,用于进群和退群
    private int action;
    private String uid;

    public GroupTableEvent(String groupId, int type,String content) {
        this.groupId = groupId;
        this.type = type;
        this.content=content;
    }

    public GroupTableEvent(String groupId, int type,int action,String uid) {
        this.groupId = groupId;
        this.type = type;
        this.action=action;
        this.uid=uid;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    public String getGroupId() {
        return groupId;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
