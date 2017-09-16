package com.example.news.dagger;

import android.app.Application;

import com.example.commonlibrary.bean.DaoMaster;
import com.example.commonlibrary.bean.DaoSession;
import com.example.commonlibrary.dagger.scope.PerApplication;
import com.example.news.MainRepositoryManager;

import org.greenrobot.greendao.database.Database;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/16      16:14
 * QQ:             1981367757
 */
@Module
public class NewsModule {

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
