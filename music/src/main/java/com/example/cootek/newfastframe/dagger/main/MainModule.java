package com.example.cootek.newfastframe.dagger.main;

import android.support.annotation.Nullable;

import com.example.commonlibrary.bean.chat.DaoSession;
import com.example.commonlibrary.dagger.scope.PerApplication;
import com.example.cootek.newfastframe.MainRepositoryManager;
import com.example.cootek.newfastframe.interceptor.MusicInterceptor;
import com.example.cootek.newfastframe.util.MusicUtil;
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
 * Created by COOTEK on 2017/8/8.
 */
@Module
public class MainModule {

    @Provides
    @PerApplication
    public MainRepositoryManager provideRepositoryManager(@Named("music") Retrofit retrofit, DaoSession daoSession) {
        return new MainRepositoryManager(retrofit, daoSession);
    }

    @Provides
    @Named("music")
    @PerApplication
    public Retrofit provideRetrofit(@Named("music") OkHttpClient okHttpClient, @Nullable Gson gson){
        Retrofit.Builder builder=new Retrofit.Builder().baseUrl(MusicUtil.BASE_URL).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient);
        return builder.build();
    }


    @Provides
    @Named("music")
    @PerApplication
    public OkHttpClient provideOkHttpClient(@Named("music")MusicInterceptor interceptor){
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS).readTimeout(10,TimeUnit.SECONDS);
        builder.addInterceptor(interceptor);
        return builder.build();
    }



    @Provides
    @Named("music")
    @PerApplication
    public MusicInterceptor provideNewsInterceptor(){
        return new MusicInterceptor();
    }
}
