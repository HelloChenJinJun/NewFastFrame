package com.example.chat.mvp.WinXinInfoTask;


import com.example.chat.MainRepositoryManager;
import com.example.chat.bean.WinXinBean;
import com.example.chat.db.ChatDB;

import java.util.List;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/7      10:12
 * QQ:             1981367757
 */

public class WinXinInfoModel extends WinXinInfoContacts.Model {


        public WinXinInfoModel(MainRepositoryManager repositoryManager) {
                super(repositoryManager);
        }

        @Override
        public boolean saveCacheWeiXinInfo(String key, String json) {
                return ChatDB.create().saveWeiXinInfo(key, json);
        }

        @Override
        public List<WinXinBean> getCacheWeiXinInfo(String key) {
                return ChatDB.create().getWeiXinInfo(key);
        }

        @Override
        public boolean clearAllData() {
                return ChatDB.create().clearAllWeiXinData();
        }
}
