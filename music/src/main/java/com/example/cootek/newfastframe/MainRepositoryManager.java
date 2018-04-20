package com.example.cootek.newfastframe;




import com.example.commonlibrary.bean.chat.DaoSession;
import com.example.commonlibrary.repository.BaseRepositoryManager;

import retrofit2.Retrofit;

/**
 * Created by COOTEK on 2017/8/11.
 */

public class MainRepositoryManager extends BaseRepositoryManager<DaoSession> {


    public MainRepositoryManager(Retrofit retrofit, DaoSession abstractDaoSession) {
        super(retrofit, abstractDaoSession);
    }
}
