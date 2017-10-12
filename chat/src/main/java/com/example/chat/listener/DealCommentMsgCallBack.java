package com.example.chat.listener;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/3      16:57
 * QQ:             1981367757
 */

public interface DealCommentMsgCallBack {


        void onSuccess(String shareMessageId, String content, int position);

        void onFailed(String shareMessageId, String content, int position, int errorId, String errorMsg);

}
