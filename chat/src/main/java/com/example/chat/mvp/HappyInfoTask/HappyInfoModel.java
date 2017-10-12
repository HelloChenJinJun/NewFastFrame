package com.example.chat.mvp.HappyInfoTask;


import com.example.chat.MainRepositoryManager;
import com.example.chat.bean.HappyBean;
import com.example.chat.db.ChatDB;

import java.util.List;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/7      21:05
 * QQ:             1981367757
 */

public class HappyInfoModel extends HappyContacts.Model {
        public HappyInfoModel(MainRepositoryManager repositoryManager) {
                super(repositoryManager);
        }

        @Override
        public boolean saveHappyInfo(String key, String json) {
                return ChatDB.create().saveHappyInfo(key, json);
        }

        @Override
        public List<HappyBean> getHappyInfo(String key) {
                return ChatDB.create().getHappyInfo(key);
        }

        @Override
        public boolean clearAllCacheHappyData() {
                return ChatDB.create().clearAllCacheHappyData();
        }
}
