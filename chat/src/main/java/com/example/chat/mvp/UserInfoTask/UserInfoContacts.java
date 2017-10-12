package com.example.chat.mvp.UserInfoTask;

import com.example.chat.MainRepositoryManager;
import com.example.chat.bean.User;
import com.example.chat.listener.DealUserInfoCallBack;
import com.example.commonlibrary.mvp.model.BaseModel;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/25      13:49
 * QQ:             1981367757
 */

public interface UserInfoContacts {
        interface View<T> extends IView<T> {
                void updateUserAvatar(String uid, String avatar);

                void updateUserSignature(String uid, String signature);

                void updateUserNick(String uid, String nick);

                void updateUserInfo(User user);
        }


        public abstract class  Model extends BaseModel<MainRepositoryManager>{
                public Model(MainRepositoryManager repositoryManager) {
                        super(repositoryManager);
                }

                abstract void updateUserAvatar(String uid, String avatar, DealUserInfoCallBack dealUserInfoCallBack);

                abstract void updateUserSignature(String uid, String signature, DealUserInfoCallBack dealUserInfoCallBack);

                abstract void updateUserNick(String uid, String nick, DealUserInfoCallBack dealUserInfoCallBack);

                abstract void updateUserInfo(User user, DealUserInfoCallBack dealUserInfoCallBack);
        }

        abstract class Presenter extends BasePresenter<View, Model> {

                public Presenter(View iView, Model baseModel) {
                        super(iView, baseModel);
                }

                public abstract void modifyUserAvatar(String uid, String avatar, DealUserInfoCallBack dealUserInfoCallBack);

                public abstract void modifyUserNick(String uid, String nick, DealUserInfoCallBack dealUserInfoCallBack);

                public abstract void modifyUserSignature(String uid, String signature, DealUserInfoCallBack dealUserInfoCallBack);

                public abstract void modifyUserInfo(User user, DealUserInfoCallBack dealUserInfoCallBack);
        }


}
