package com.example.chat.listener;


import com.example.chat.bean.User;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/25      14:19
 * QQ:             1981367757
 */

public class SimpleUserInfoListenerImpl implements DealUserInfoCallBack {
        @Override
        public void updateAvatarSuccess(String id, String avatar) {

        }

        @Override
        public void updateNickSuccess(String id, String nick) {

        }

        @Override
        public void updateSignatureSuccess(String id, String signature) {

        }

        @Override
        public void updateUserInfoSuccess(User user) {

        }

        @Override
        public void onFailed(String uid, int errorId, String errorMsg) {

        }
}
