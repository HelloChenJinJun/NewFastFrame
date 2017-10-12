package com.example.chat.mvp.NearByPeopleTask;


import com.example.chat.bean.User;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;

import java.util.List;

import cn.bmob.v3.listener.FindListener;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/30      23:09
 * QQ:             1981367757
 */

public class NearbyPeoplePresenter extends NearbyPeopleContacts.Presenter {

        private double longitude;
        private double latitude;
        private boolean sex;
        private FindListener<User> mUserFindListener;
        private boolean isAll;

        public NearbyPeoplePresenter(NearbyPeopleContacts.View iView, NearbyPeopleContacts.Model baseModel) {
                super(iView, baseModel);
        }

        @Override
        public void queryNearbyPeople(double longitude, double latitude, boolean isAll,boolean sex) {
                this.longitude = longitude;
                this.latitude = latitude;
                this.sex = sex;
                if (mUserFindListener == null) {
                        mUserFindListener = new FindListener<User>() {
                                @Override
                                public void onSuccess(List<User> list) {
                                        iView.hideLoading();
                                        iView.updateNearbyPeople(list);
                                }

                                @Override
                                public void onError(int i, String s) {
                                        iView.hideLoading();
                                        iView.showError(s, new EmptyLayout.OnRetryListener() {
                                                @Override
                                                public void onRetry() {
                                                        queryNearbyPeople(NearbyPeoplePresenter.this.longitude, NearbyPeoplePresenter.this.latitude, NearbyPeoplePresenter.this.isAll,NearbyPeoplePresenter.this.sex);
                                                }
                                        });
                                }
                        };
                }
                iView.showLoading("正在加载附近人消息。。。请稍后");
                baseModel.queryNearbyPeople(longitude, latitude, isAll,sex, mUserFindListener);
        }
}
