package com.example.chat.mvp.NearByPeopleTask;


import com.example.chat.MainRepositoryManager;
import com.example.chat.bean.User;
import com.example.commonlibrary.mvp.model.BaseModel;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;

import java.util.List;

import cn.bmob.v3.listener.FindListener;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/30      23:10
 * QQ:             1981367757
 */

public interface NearbyPeopleContacts {


        public interface View<T> extends IView<T> {
                void updateNearbyPeople(List<User> list);
        }


        public abstract class Model extends BaseModel<MainRepositoryManager> {
                public Model(MainRepositoryManager repositoryManager) {
                        super(repositoryManager);
                }

                public abstract void queryNearbyPeople(double longitude, double latitude, boolean isAll, boolean sex, FindListener<User> listener);
        }

        public abstract class Presenter extends BasePresenter<View, Model> {
                public Presenter(View iView, Model baseModel) {
                        super(iView, baseModel);
                }

                public abstract void queryNearbyPeople(double longitude, double latitude, boolean isAll, boolean sex);
        }
}
