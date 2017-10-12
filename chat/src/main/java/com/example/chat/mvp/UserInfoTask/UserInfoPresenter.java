package com.example.chat.mvp.UserInfoTask;


import com.example.chat.bean.User;
import com.example.chat.listener.DealUserInfoCallBack;
import com.example.chat.listener.SimpleUserInfoListenerImpl;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/25      14:05
 * QQ:             1981367757
 */

public class UserInfoPresenter extends UserInfoContacts.Presenter {


        public UserInfoPresenter(UserInfoContacts.View iView, UserInfoContacts.Model baseModel) {
                super(iView, baseModel);
        }

        @Override
        public void modifyUserAvatar(String uid, String avatar, DealUserInfoCallBack dealUserInfoCallBack) {
                baseModel.updateUserAvatar(uid, avatar, new SimpleUserInfoListenerImpl() {
                        @Override
                        public void updateAvatarSuccess(String id, String avatar) {
                                super.updateAvatarSuccess(id, avatar);
                        }

                        @Override
                        public void onFailed(String uid, int errorId, String errorMsg) {
                                super.onFailed(uid, errorId, errorMsg);
                        }
                });
        }

        @Override
        public void modifyUserNick(String uid, String nick, DealUserInfoCallBack dealUserInfoCallBack) {

        }

        @Override
        public void modifyUserSignature(String uid, String signature, DealUserInfoCallBack dealUserInfoCallBack) {

        }

        @Override
        public void modifyUserInfo(User user, DealUserInfoCallBack dealUserInfoCallBack) {

        }
}
