package com.example.chat.dagger;


import com.example.chat.ChatInterceptor;
import com.example.chat.base.Constant;
import com.example.commonlibrary.bean.chat.DaoSession;
import com.example.commonlibrary.dagger.scope.PerApplication;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.repository.DefaultRepositoryManager;
import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/10/9      15:36
 * QQ:             1981367757
 */
@Module
public class ChatMainModule {


    @Provides
    public DefaultRepositoryManager provideRepositoryManager(@Named("chat") Retrofit retrofit, DaoSession daoSession) {
        return new DefaultRepositoryManager(retrofit, daoSession);
    }




    @Provides
    public DefaultModel providerModel(DefaultRepositoryManager defaultRepositoryManager){
        return new DefaultModel(defaultRepositoryManager);
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
