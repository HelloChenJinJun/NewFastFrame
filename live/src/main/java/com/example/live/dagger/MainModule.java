package com.example.live.dagger;


import com.example.commonlibrary.bean.chat.DaoSession;
import com.example.commonlibrary.dagger.scope.PerApplication;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.repository.DefaultRepositoryManager;
import com.example.live.interceptor.LiveInterceptor;
import com.example.live.util.LiveUtil;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/14      10:43
 * QQ:             1981367757
 */
@Module
public class MainModule {



    @Provides
    public DefaultRepositoryManager provideRepositoryManager(@Named("live") Retrofit retrofit, DaoSession daoSession) {
        return new DefaultRepositoryManager(retrofit, daoSession);
    }

    @Provides
    public DefaultModel provideModel(DefaultRepositoryManager defaultRepositoryManager){
        return new DefaultModel(defaultRepositoryManager);
    }

    @Provides
    @Named("live")
    @PerApplication
    public Retrofit provideRetrofit(@Named("live") OkHttpClient okHttpClient,Retrofit.Builder builder){
        return builder.baseUrl(LiveUtil.BASE_URL).client(okHttpClient).build();
    }


    @Provides
    @Named("live")
    @PerApplication
    public OkHttpClient provideOkHttpClient(OkHttpClient.Builder builder,@Named("live")LiveInterceptor interceptor){
       return builder.addInterceptor(interceptor).build();
    }



    @Provides
    @Named("live")
    @PerApplication
    public LiveInterceptor provideNewsInterceptor(){
        return new LiveInterceptor();
    }


}
