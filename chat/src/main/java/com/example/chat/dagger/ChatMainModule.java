package com.example.chat.dagger;

import android.support.annotation.Nullable;

import com.example.chat.ChatInterceptor;
import com.example.chat.MainRepositoryManager;
import com.example.chat.base.Constant;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.chat.DaoSession;
import com.example.commonlibrary.dagger.scope.PerApplication;
import com.example.commonlibrary.repository.DefaultRepositoryManager;
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
 * 创建时间:    2017/10/9      15:36
 * QQ:             1981367757
 */
@Module
public class ChatMainModule {


    @Provides
    @PerApplication
    public DefaultRepositoryManager provideRepositoryManager(@Named("chat") Retrofit retrofit, DaoSession daoSession) {
        return new DefaultRepositoryManager(retrofit, daoSession);
    }





    @Provides
    @Named("chat")
    @PerApplication
    public Retrofit provideRetrofit(@Named("chat") OkHttpClient okHttpClient,Retrofit.Builder builder){
        builder.baseUrl(Constant.BASE_URL).client(okHttpClient);
        return builder.build();
    }


    @Provides
    @Named("chat")
    @PerApplication
    public OkHttpClient provideOkHttpClient(@Named("chat")ChatInterceptor interceptor,OkHttpClient.Builder builder){
        builder.addInterceptor(interceptor);
        return builder.build();
    }



    @Provides
    @Named("chat")
    @PerApplication
    public ChatInterceptor provideNewsInterceptor(){
        return new ChatInterceptor();
    }
}
