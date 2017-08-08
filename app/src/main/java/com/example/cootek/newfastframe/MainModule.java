package com.example.cootek.newfastframe;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by COOTEK on 2017/8/8.
 */
@Module
class MainModule {
    private Application application;


    public MainModule(Application application) {
        this.application = application;
    }


    @Provides
    @Singleton
    public Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    public DaoSession provideDaoSession(Application application) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(application, "common_library_db", null);
        Database database = devOpenHelper.getWritableDb();
        DaoMaster master = new DaoMaster(database);
        return master.newSession();
    }
}
