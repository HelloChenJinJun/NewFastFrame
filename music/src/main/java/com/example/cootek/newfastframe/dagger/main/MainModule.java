package com.example.cootek.newfastframe.dagger.main;


import com.example.commonlibrary.bean.chat.DaoSession;
import com.example.commonlibrary.dagger.scope.PerApplication;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.repository.DefaultRepositoryManager;
import com.example.cootek.newfastframe.interceptor.MusicInterceptor;
import com.example.cootek.newfastframe.util.MusicUtil;
import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by COOTEK on 2017/8/8.
 */
@Module
public class MainModule {







    @Provides
    public DefaultRepositoryManager provideRepositoryManager(@Named("music") Retrofit retrofit, DaoSession daoSession) {
        return new DefaultRepositoryManager(retrofit, daoSession);
    }


    @Provides
    public DefaultModel provideModel(DefaultRepositoryManager defaultRepositoryManager){
        return new DefaultModel(defaultRepositoryManager);
    }

    @Provides
    @Named("music")
    @PerApplication
    public Retrofit provideRetrofit(@Named("music") OkHttpClient okHttpClient,Retrofit.Builder builder){
     return   builder.baseUrl(MusicUtil.BASE_URL).client(okHttpClient).build();
    }


    @Provides
    @Named("music")
    @PerApplication
    public OkHttpClient provideOkHttpClient(@Named("music")MusicInterceptor interceptor,OkHttpClient.Builder builder){
      return   builder.addInterceptor(interceptor).build();
    }



    @Provides
    @Named("music")
    @PerApplication
    public MusicInterceptor provideNewsInterceptor(){
        return new MusicInterceptor();
    }
}
