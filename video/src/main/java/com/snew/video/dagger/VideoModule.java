package com.snew.video.dagger;

import com.example.commonlibrary.dagger.scope.PerApplication;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.net.download.DaoSession;
import com.example.commonlibrary.repository.DefaultRepositoryManager;
import com.snew.video.interceptor.CacheControlInterceptor;
import com.snew.video.interceptor.IndexInterceptor;
import com.snew.video.util.VideoUtil;

import java.io.File;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/8     17:08
 */
@Module
public class VideoModule {

    @Provides
    public DefaultRepositoryManager provideRepositoryManager(@Named("index") Retrofit retrofit, DaoSession daoSession) {
        return new DefaultRepositoryManager(retrofit, daoSession);
    }


    @Provides
    public DefaultModel providerModel(DefaultRepositoryManager defaultRepositoryManager) {
        return new DefaultModel(defaultRepositoryManager);
    }


    @Provides
    @Named("index")
    @PerApplication
    public Retrofit provideRetrofit(@Named("index") OkHttpClient okHttpClient, Retrofit.Builder builder) {
        builder.baseUrl(VideoUtil.BASE_URL).client(okHttpClient);
        return builder.build();
    }


    @Provides
    @Named("index")
    @PerApplication
    public OkHttpClient provideOkHttpClient(@Named("index") IndexInterceptor interceptor, File cacheFile, OkHttpClient.Builder builder) {
        CacheControlInterceptor cacheControlInterceptor = new CacheControlInterceptor();
        builder.addInterceptor(interceptor)
                .addInterceptor(cacheControlInterceptor)
                .addNetworkInterceptor(cacheControlInterceptor)
                .cache(new Cache(new File(cacheFile.getAbsolutePath(), "news"),
                        1024 * 1024 * 100));
        builder.followRedirects(false);
        return builder.build();
    }


    @Provides
    @Named("index")
    @PerApplication
    public IndexInterceptor provideNewsInterceptor() {
        return new IndexInterceptor();
    }
}
