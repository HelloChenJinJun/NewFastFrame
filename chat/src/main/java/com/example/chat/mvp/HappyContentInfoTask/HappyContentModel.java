package com.example.chat.mvp.HappyContentInfoTask;


import com.example.chat.MainRepositoryManager;
import com.example.chat.bean.HappyContentBean;
import com.example.chat.db.ChatDB;

import java.util.List;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/8      20:32
 * QQ:             1981367757
 */

public class HappyContentModel extends HappyContentContacts.Model {


        public HappyContentModel(MainRepositoryManager repositoryManager) {
                super(repositoryManager);
        }

        @Override
        public boolean saveHappyContentInfo(String key, String json) {
                ChatDB.create().saveHappyContentInfo(key,json);
                return false;
        }

        @Override
        public List<HappyContentBean> getHappyContentInfo(String key) {
                return ChatDB.create().getHappyContentInfo(key);
        }

        @Override
        public boolean clearAllCacheHappyContentData() {
                return ChatDB.create().clearAllCacheHappyContentData();
        }
}
