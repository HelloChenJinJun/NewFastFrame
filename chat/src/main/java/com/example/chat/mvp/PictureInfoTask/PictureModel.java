package com.example.chat.mvp.PictureInfoTask;

import com.example.chat.MainRepositoryManager;
import com.example.chat.bean.PictureBean;
import com.example.chat.db.ChatDB;

import java.util.List;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/9      0:10
 * QQ:             1981367757
 */

public class PictureModel extends PictureContacts.Model {
        public PictureModel(MainRepositoryManager repositoryManager) {
                super(repositoryManager);
        }

        @Override
        public boolean savePictureInfo(String key, String json) {
                return ChatDB.create().savePictureInfo(key, json);
        }

        @Override
        public List<PictureBean> getPictureInfo(String key) {
                return ChatDB.create().getPictureInfo(key);
        }

        @Override
        public boolean clearAllCacheData() {
                ChatDB.create().clearAllPictureData();
                return false;
        }
}
