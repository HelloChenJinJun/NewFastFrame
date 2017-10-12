package com.example.chat.mvp.NearByPeopleTask;

import com.example.chat.MainRepositoryManager;
import com.example.chat.bean.User;
import com.example.chat.manager.UserManager;

import cn.bmob.v3.listener.FindListener;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/30      23:10
 * QQ:             1981367757
 */

public class NearbyPeopleModel extends NearbyPeopleContacts.Model {

        public NearbyPeopleModel(MainRepositoryManager repositoryManager) {
                super(repositoryManager);
        }

        @Override
        public void queryNearbyPeople(double longitude, double latitude,boolean isAll, boolean sex, FindListener<User> listener) {
                UserManager.getInstance().queryNearbyPeople(longitude, latitude, isAll,sex, listener);
        }
}
