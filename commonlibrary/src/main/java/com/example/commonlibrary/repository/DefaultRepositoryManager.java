package com.example.commonlibrary.repository;

import com.example.commonlibrary.bean.chat.DaoSession;

import retrofit2.Retrofit;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/8/26     14:47
 */

public class DefaultRepositoryManager extends BaseRepositoryManager<DaoSession> {
    public DefaultRepositoryManager(Retrofit retrofit, DaoSession abstractDaoSession) {
        super(retrofit, abstractDaoSession);
    }
}
