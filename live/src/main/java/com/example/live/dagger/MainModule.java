package com.example.live.dagger;

import android.support.annotation.Nullable;

import com.example.commonlibrary.bean.chat.DaoSession;
import com.example.commonlibrary.dagger.scope.PerApplication;
import com.example.live.MainRepositoryManager;
import com.example.live.interceptor.LiveInterceptor;
import com.example.live.util.LiveUtil;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/14      10:43
 * QQ:             1981367757
 */
@Module
public class MainModule {



    @Provides
    @PerApplication
    public MainRepositoryManager provideRepositoryManager(@Named("live") Retrofit retrofit, DaoSession daoSession) {
        return new MainRepositoryManager(retrofit, daoSession);
    }

    @Provides
    @Named("live")
    @PerApplication
    public Retrofit provideRetrofit(@Named("live") OkHttpClient okHttpClient,@Nullable Gson gson){
        Retrofit.Builder builder=new Retrofit.Builder().baseUrl(LiveUtil.BASE_URL).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient);
        return builder.build();
    }


    @Provides
    @Named("live")
    @PerApplication
    public OkHttpClient provideOkHttpClient(@Named("live")LiveInterceptor interceptor){
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS).readTimeout(10,TimeUnit.SECONDS);
        builder.addInterceptor(interceptor);
        return builder.build();
    }



    @Provides
    @Named("live")
    @PerApplication
    public LiveInterceptor provideNewsInterceptor(){
        return new LiveInterceptor();
    }


}
