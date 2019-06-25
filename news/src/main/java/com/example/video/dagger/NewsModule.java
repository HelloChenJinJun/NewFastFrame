package com.example.video.dagger;

import com.example.commonlibrary.dagger.scope.PerApplication;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.net.download.DaoSession;
import com.example.commonlibrary.repository.DefaultRepositoryManager;
import com.example.video.interceptor.CacheControlInterceptor;
import com.example.video.interceptor.NewsInterceptor;
import com.example.video.util.NewsUtil;

import java.io.File;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/16      16:14
 * QQ:             1981367757
 */
@Module
public class NewsModule {


    @Provides
    public DefaultRepositoryManager provideRepositoryManager(@Named("news") Retrofit retrofit, DaoSession daoSession) {
        return new DefaultRepositoryManager(retrofit, daoSession);
    }


    @Provides
    public DefaultModel provideModel(DefaultRepositoryManager defaultRepositoryManager) {
        return new DefaultModel(defaultRepositoryManager);
    }

    @Provides
    @Named("news")
    @PerApplication
    public Retrofit provideRetrofit(@Named("news") OkHttpClient okHttpClient, Retrofit.Builder builder) {
        return builder.baseUrl(NewsUtil.BASE_URL).client(okHttpClient).build();
    }


    @Provides
    @Named("news")
    @PerApplication
    public OkHttpClient provideOkHttpClient(OkHttpClient.Builder builder, File cacheFile, @Named("news") NewsInterceptor interceptor) {
        CacheControlInterceptor cacheControlInterceptor = new CacheControlInterceptor();
        builder.addInterceptor(interceptor)
                .addInterceptor(cacheControlInterceptor)
                .addNetworkInterceptor(cacheControlInterceptor)
                .cache(new Cache(new File(cacheFile.getAbsolutePath(), "news"),
                        1024 * 1024 * 100));
        builder.followRedirects(true);
        return builder.build();
    }


    @Provides
    @Named("news")
    @PerApplication
    public NewsInterceptor provideNewsInterceptor() {
        return new NewsInterceptor();
    }
}
