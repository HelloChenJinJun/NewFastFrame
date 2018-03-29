package com.example.chat.mvp.UserInfoTask;


import com.example.chat.MainRepositoryManager;
import com.example.chat.bean.User;
import com.example.chat.listener.DealUserInfoCallBack;
import com.example.chat.listener.SimpleUserInfoListenerImpl;
import com.example.chat.manager.MsgManager;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/25      14:04
 * QQ:             1981367757
 */

public class UserInfoModel extends UserInfoContacts.Model {
        public UserInfoModel(MainRepositoryManager repositoryManager) {
                super(repositoryManager);
        }

        @Override
        public void updateUserAvatar(String uid, String avatar, final DealUserInfoCallBack dealUserInfoCallBack) {
                MsgManager.getInstance().updateUserAvatar(uid, avatar, new SimpleUserInfoListenerImpl() {
                        @Override
                        public void updateAvatarSuccess(String id, String avatar) {
                                super.updateAvatarSuccess(id, avatar);
                                ChatDB.create().updateUserAvatar(id, avatar);
                                dealUserInfoCallBack.updateAvatarSuccess(id, avatar);
                        }


                        @Override
                        public void onFailed(String uid, int errorId, String errorMsg) {
                                super.onFailed(uid, errorId, errorMsg);
                                dealUserInfoCallBack.onFailed(uid, errorId, errorMsg);
                        }
                });
        }

        @Override
        public void updateUserSignature(String uid, String signature, final DealUserInfoCallBack dealUserInfoCallBack) {
                MsgManager.getInstance().updateUserSignature(uid, signature, new SimpleUserInfoListenerImpl() {
                        @Override
                        public void updateSignatureSuccess(String id, String signature) {
                                super.updateSignatureSuccess(id, signature);
                                ChatDB.create().updateUserSignature(id, signature);
                                dealUserInfoCallBack.updateSignatureSuccess(id, signature);
                        }

                        @Override
                        public void onFailed(String uid, int errorId, String errorMsg) {
                                super.onFailed(uid, errorId, errorMsg);
                                dealUserInfoCallBack.onFailed(uid, errorId, errorMsg);
                        }
                });

        }

        @Override
        public void updateUserNick(String uid, String nick, final DealUserInfoCallBack dealUserInfoCallBack) {
                MsgManager.getInstance().updateUserNick(uid, nick, new SimpleUserInfoListenerImpl() {
                        @Override
                        public void updateNickSuccess(String id, String nick) {
                                super.updateNickSuccess(id, nick);
                                ChatDB.create().updateUserNick(id, nick);
                                dealUserInfoCallBack.updateNickSuccess(id, nick);


                        }

                        @Override
                        public void onFailed(String uid, int errorId, String errorMsg) {
                                super.onFailed(uid, errorId, errorMsg);
                                dealUserInfoCallBack.onFailed(uid, errorId, errorMsg);
                        }
                });

        }

        @Override
        public void updateUserInfo(final User user, final DealUserInfoCallBack dealUserInfoCallBack) {
                MsgManager.getInstance().updateUserinfo(user, new SimpleUserInfoListenerImpl() {
                        @Override
                        public void updateUserInfoSuccess(User user) {
                                super.updateUserInfoSuccess(user);
                                ChatDB.create().addOrUpdateContacts(user);
                                dealUserInfoCallBack.updateUserInfoSuccess(user);
                        }

                        @Override
                        public void onFailed(String uid, int errorId, String errorMsg) {
                                super.onFailed(uid, errorId, errorMsg);
                                dealUserInfoCallBack.onFailed(uid, errorId, errorMsg);
                        }
                });
        }
}
