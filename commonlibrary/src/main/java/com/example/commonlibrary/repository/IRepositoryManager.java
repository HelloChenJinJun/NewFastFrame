package com.example.commonlibrary.repository;

import com.example.commonlibrary.net.db.DaoSession;

/**
 * Created by COOTEK on 2017/7/31.
 */

public interface IRepositoryManager {
    <T> T getApi(Class<T> retrofitClass);


    <T> void clearApi(Class<T> retrofitClass);


    void clearAllCache();


    DaoSession getDaoSession();



}
