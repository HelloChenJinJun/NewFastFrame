package com.example.chat.bean.post;

import com.example.commonlibrary.baseadapter.baseitem.MultipleItem;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/3     15:50
 * QQ:         1981367757
 */

public class CommentListDetailBean implements MultipleItem {

    public static final int TYPE_RIGHT = 1;
    public static final int TYPE_LEFT = 2;
    private String content;
    private long time;
    private String avatar;
    private String name;


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    private int msgType;

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int getItemViewType() {
        return getMsgType();
    }
}
