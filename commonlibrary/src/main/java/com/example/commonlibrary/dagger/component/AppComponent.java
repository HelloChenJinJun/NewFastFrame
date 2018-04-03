package com.example.commonlibrary.dagger.component;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.commonlibrary.bean.chat.DaoSession;
import com.example.commonlibrary.dagger.module.AppConfigModule;
import com.example.commonlibrary.dagger.module.AppModule;
import com.example.commonlibrary.dagger.module.GlobalConfigModule;
import com.example.commonlibrary.imageloader.ImageLoader;
import com.example.commonlibrary.manager.ActivityManager;
import com.google.gson.Gson;

import java.io.File;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by COOTEK on 2017/7/28.
 */

@Singleton
@Component(modules = {AppModule.class, GlobalConfigModule.class, AppConfigModule.class})
public interface AppComponent {
    public Application getApplication();

    public Bundle getBundle();

    public ImageLoader getImageLoader();

    public Gson getGson();

    public File getCacheFile();

    public OkHttpClient getOkHttpClient();



    public ActivityManager getActivityManager();



    public DaoSession getDaoSession();

    public OkHttpClient.Builder getOkHttpClientBuilder();

    public Retrofit getRetrofit();
    public SharedPreferences getSharedPreferences();

}
