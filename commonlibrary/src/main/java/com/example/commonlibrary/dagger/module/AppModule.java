package com.example.commonlibrary.dagger.module;

import android.app.Application;
import android.os.Bundle;

import com.example.commonlibrary.imageloader.ImageLoader;
import com.example.commonlibrary.net.db.DaoMaster;
import com.example.commonlibrary.net.db.DaoSession;
import com.example.commonlibrary.repository.IRepositoryManager;
import com.example.commonlibrary.repository.RepositoryManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by COOTEK on 2017/7/28.
 */

@Module
public class AppModule {


    private Application application;


    public AppModule(Application application) {
        this.application = application;
    }

    @Singleton
    @Provides
    public Application getApplication() {
        return application;
    }


    @Singleton
    @Provides
    public IRepositoryManager getRepositoryManager(RepositoryManager repositoryManager) {
        return repositoryManager;
    }

    @Singleton
    @Provides
    public DaoSession provideDaoSession(Application application) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(application, "common_library_db", null);
        Database database = devOpenHelper.getWritableDb();
        DaoMaster master = new DaoMaster(database);
        return master.newSession();
    }


    @Singleton
    @Provides
    public Gson provideGson(Application application, NetClientModule.GsonConfig gsonConfig) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        if (gsonConfig != null) {
            gsonConfig.config(application, gsonBuilder);
        }
        return gsonBuilder.create();
    }


    @Singleton
    @Provides
    public Bundle getBundle() {
        return new Bundle();
    }
}
