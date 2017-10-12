package com.example.chat.listener;


import com.example.chat.bean.User;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/25      14:09
 * QQ:             1981367757
 */

public interface DealUserInfoCallBack {
        void updateAvatarSuccess(String id, String avatar);

        void updateNickSuccess(String id, String nick);

        void updateSignatureSuccess(String id, String signature);

        void updateUserInfoSuccess(User user);

        void onFailed(String uid, int errorId, String errorMsg);
}
