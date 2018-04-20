package com.example.live;




import com.example.commonlibrary.bean.chat.DaoSession;
import com.example.commonlibrary.repository.BaseRepositoryManager;

import retrofit2.Retrofit;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/14      10:51
 * QQ:             1981367757
 */


public class MainRepositoryManager extends BaseRepositoryManager<DaoSession> {


    public MainRepositoryManager(Retrofit retrofit, DaoSession abstractDaoSession) {
        super(retrofit, abstractDaoSession);
    }
}
