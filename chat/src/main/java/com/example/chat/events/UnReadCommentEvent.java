package com.example.chat.events;

import java.util.ArrayList;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/5/19     13:31
 */

public class UnReadCommentEvent {

    private ArrayList<String>  unReadCommentListId;

    public UnReadCommentEvent(ArrayList<String> unReadCommentListId) {
        this.unReadCommentListId = unReadCommentListId;
    }

    public ArrayList<String> getUnReadCommentListId() {
        return unReadCommentListId;
    }
}
