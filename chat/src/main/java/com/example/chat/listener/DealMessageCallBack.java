package com.example.chat.listener;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/3      10:27
 * QQ:             1981367757
 */
public interface DealMessageCallBack {
        void onSuccess(String id);

        void onFailed(String id, int errorId, String errorMsg);


}
