package com.example.chat.bean.post;

import com.example.commonlibrary.baseadapter.baseitem.MultipleItem;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/3     17:06
 * QQ:         1981367757
 */

public class ReplyDetailContent implements MultipleItem {

    public static final int TYPE_RIGHT = 1;
    public static final int TYPE_LEFT = 2;

    private String content;
    private long time;
    private String uid;

    private int msgType;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public int getItemViewType() {
        return getMsgType();
    }
}
