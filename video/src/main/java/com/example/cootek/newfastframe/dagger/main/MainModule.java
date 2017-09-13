package com.example.cootek.newfastframe.dagger.main;

import android.app.Application;

import com.example.commonlibrary.bean.DaoMaster;
import com.example.commonlibrary.bean.DaoSession;
import com.example.commonlibrary.dagger.scope.PerApplication;
import com.example.cootek.newfastframe.MainRepositoryManager;

import org.greenrobot.greendao.database.Database;


import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by COOTEK on 2017/8/8.
 */
@Module
public class MainModule {
    @Provides
    @PerApplication
    public DaoSession provideDaoSession(Application application) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(application, "common_library_db", null);
        Database database = devOpenHelper.getWritableDb();
        DaoMaster master = new DaoMaster(database);
        return master.newSession();
    }


    @Provides
    @PerApplication
    public MainRepositoryManager provideRepositoryManager(Retrofit retrofit, DaoSession daoSession) {
        return new MainRepositoryManager(retrofit, daoSession);
    }
}
