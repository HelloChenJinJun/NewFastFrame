package com.example.chat.bean;

import com.example.chat.manager.UserManager;
import com.example.commonlibrary.baseadapter.baseitem.MultipleItem;

import cn.bmob.v3.BmobObject;

/**
 * 项目名称:    SecondhandMarket
 * 创建人:      李晨
 * 创建时间:    2018/4/26     16:58
 * QQ:         1981367757
 */

public class ChatBean extends BmobObject implements MultipleItem {


    public static final int TYPE_LEFT = 0;
    public static final int TYPE_RIGHT = 1;

    private String content;
    private String toId;
    private String uid;


    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public int getItemViewType() {
        return uid != null && uid.equals(UserManager.getInstance().getCurrentUserObjectId()) ? TYPE_RIGHT :
                TYPE_LEFT;
    }
}
