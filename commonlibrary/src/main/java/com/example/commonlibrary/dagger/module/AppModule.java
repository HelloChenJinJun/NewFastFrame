package com.example.commonlibrary.dagger.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


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
    public Gson provideGson(Application application, GlobalConfigModule.GsonConfig gsonConfig) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        if (gsonConfig != null) {
            gsonConfig.config(application, gsonBuilder);
        }
        return gsonBuilder.create();
    }



    @Singleton
    @Provides
    public SharedPreferences provideSharedPreferences(Application application){
       return application.getSharedPreferences("common", Context.MODE_PRIVATE);
    }


    @Singleton
    @Provides
    public Bundle getBundle() {
        return new Bundle();
    }
}
