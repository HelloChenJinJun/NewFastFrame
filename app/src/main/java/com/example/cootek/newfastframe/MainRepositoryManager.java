package com.example.cootek.newfastframe;

import com.example.commonlibrary.repository.BaseRepositoryManager;
import com.example.commonlibrary.repository.IRepositoryManager;

import javax.inject.Named;

import retrofit2.Retrofit;

/**
 * Created by COOTEK on 2017/8/11.
 */

public class MainRepositoryManager extends BaseRepositoryManager<DaoSession> {


    public MainRepositoryManager(Retrofit retrofit, DaoSession abstractDaoSession) {
        super(retrofit, abstractDaoSession);
    }
}
