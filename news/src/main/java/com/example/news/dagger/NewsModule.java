package com.example.news.dagger;


import android.support.annotation.Nullable;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.chat.DaoSession;
import com.example.commonlibrary.dagger.scope.PerApplication;
import com.example.news.MainRepositoryManager;
import com.example.news.interceptor.CacheControlInterceptor;
import com.example.news.interceptor.NewsInterceptor;
import com.example.news.util.NewsUtil;
import com.google.gson.Gson;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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
    public MainRepositoryManager provideRepositoryManager(@Named("news") Retrofit retrofit, DaoSession daoSession) {
        return new MainRepositoryManager(retrofit, daoSession);
    }

    @Provides
    @Named("news")
    @PerApplication
    public Retrofit provideRetrofit(@Named("news") OkHttpClient okHttpClient,@Nullable Gson gson){
        Retrofit.Builder builder=new Retrofit.Builder().baseUrl(NewsUtil.BASE_URL).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient);
        return builder.build();
    }


    @Provides
    @Named("news")
    @PerApplication
    public OkHttpClient provideOkHttpClient(@Named("news")NewsInterceptor interceptor){
        Cache cache=new Cache(new File(BaseApplication.getInstance().getCacheDir(),"news"),
                1024*1024*100);
        OkHttpClient.Builder builder=new OkHttpClient.Builder().cache(cache);
        CacheControlInterceptor cacheControlInterceptor=new CacheControlInterceptor();
        builder.connectTimeout(10, TimeUnit.SECONDS).readTimeout(10,TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .addInterceptor(cacheControlInterceptor)
                .addNetworkInterceptor(cacheControlInterceptor);
        builder.followRedirects(true);
        return builder.build();
    }


    @Provides
    @Named("news")
    @PerApplication
    public NewsInterceptor provideNewsInterceptor(){
        return new NewsInterceptor();
    }
}
