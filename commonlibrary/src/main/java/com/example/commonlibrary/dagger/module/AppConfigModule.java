package com.example.commonlibrary.dagger.module;

import android.app.Application;

import com.example.commonlibrary.interceptor.LogInterceptor;
import com.example.commonlibrary.net.OkHttpGlobalHandler;
import com.example.commonlibrary.imageloader.base.BaseImageLoaderStrategy;
import com.example.commonlibrary.imageloader.glide.GlideImageLoaderStrategy;
import com.example.commonlibrary.utils.FileUtil;

import java.io.File;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;

/**
 * Created by COOTEK on 2017/7/28.
 */
@Module
public class AppConfigModule {
    private HttpUrl baseUrl;
    private GlobalConfigModule.OkHttpConfig okHttpConfig;
    private GlobalConfigModule.RetrofitConfig retrofitConfig;
    private OkHttpGlobalHandler okHttpGlobalHandler;
    private List<Interceptor> interceptorList;
    private BaseImageLoaderStrategy baseImageLoaderStrategy;
    private GlobalConfigModule.GsonConfig gsonConfig;
    private File cacheFile;
    private LogInterceptor.Level level;


    @Provides
    @Singleton
    public HttpUrl getBaseUrl() {
        return baseUrl;
    }

    @Provides
    @Singleton
    public GlobalConfigModule.OkHttpConfig getOkHttpConfig() {
        return okHttpConfig;
    }

    @Provides
    @Singleton
    public GlobalConfigModule.RetrofitConfig getRetrofitConfig() {
        return retrofitConfig;
    }

    @Provides
    @Singleton
    public OkHttpGlobalHandler getOkHttpGlobalHandler() {
        return okHttpGlobalHandler;
    }

    @Provides
    @Singleton
    public BaseImageLoaderStrategy getBaseImageLoaderStrategy() {
//        默认使用Glide图片加载框架
        return baseImageLoaderStrategy == null ? new GlideImageLoaderStrategy() : baseImageLoaderStrategy;
    }

    @Provides
    @Singleton
    public LogInterceptor.Level getLevel() {
        return level;
    }

    @Provides
    @Singleton
    public List<Interceptor> getInterceptorList() {
        return interceptorList;
    }

    @Singleton
    @Provides
    public GlobalConfigModule.GsonConfig getGsonConfig() {
        return gsonConfig;
    }

    @Singleton
    @Provides
    public File getCacheFile(Application application) {
        return cacheFile == null ? FileUtil.getDefaultCacheFile(application) : cacheFile;
    }

    private AppConfigModule(Builder builder) {
        baseUrl = builder.baseUrl;
        okHttpConfig = builder.okHttpConfig;
        retrofitConfig = builder.retrofitConfig;
        okHttpGlobalHandler = builder.okHttpGlobalHandler;
        interceptorList = builder.interceptorList;
        baseImageLoaderStrategy = builder.baseImageLoaderStrategy;
        gsonConfig = builder.gsonConfig;
        cacheFile = builder.cacheFile;
        level = builder.level;
    }


    public static final class Builder {
        private HttpUrl baseUrl;
        private GlobalConfigModule.OkHttpConfig okHttpConfig;
        private GlobalConfigModule.RetrofitConfig retrofitConfig;
        private OkHttpGlobalHandler okHttpGlobalHandler;
        private List<Interceptor> interceptorList;
        private BaseImageLoaderStrategy baseImageLoaderStrategy;
        private GlobalConfigModule.GsonConfig gsonConfig;
        private File cacheFile;
        private LogInterceptor.Level level;

        public Builder() {
        }

        public Builder baseUrl(HttpUrl val) {
            baseUrl = val;
            return this;
        }


        public Builder level(LogInterceptor.Level level) {
            this.level = level;
            return this;
        }


        public Builder cacheFile(File cacheFile) {
            this.cacheFile = cacheFile;
            return this;
        }

        public Builder gsonConfig(GlobalConfigModule.GsonConfig gsonConfig) {
            this.gsonConfig = gsonConfig;
            return this;
        }

        public Builder okHttpConfig(GlobalConfigModule.OkHttpConfig val) {
            okHttpConfig = val;
            return this;
        }

        public Builder retrofitConfig(GlobalConfigModule.RetrofitConfig val) {
            retrofitConfig = val;
            return this;
        }

        public Builder okHttpGlobalHandler(OkHttpGlobalHandler val) {
            okHttpGlobalHandler = val;
            return this;
        }

        public Builder interceptorList(List<Interceptor> val) {
            interceptorList = val;
            return this;
        }

        public Builder baseImageLoaderStrategy(BaseImageLoaderStrategy val) {
            baseImageLoaderStrategy = val;
            return this;
        }

        public AppConfigModule build() {
            return new AppConfigModule(this);
        }
    }
}
