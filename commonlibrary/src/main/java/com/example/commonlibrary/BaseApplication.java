package com.example.commonlibrary;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.commonlibrary.adaptScreen.ScreenAdaptManager;
import com.example.commonlibrary.dagger.component.AppComponent;
import com.example.commonlibrary.dagger.component.DaggerAppComponent;
import com.example.commonlibrary.dagger.module.AppConfigModule;
import com.example.commonlibrary.dagger.module.AppModule;
import com.example.commonlibrary.dagger.module.GlobalConfigModule;
import com.example.commonlibrary.imageloader.glide.GlideImageLoaderStrategy;
import com.example.commonlibrary.interceptor.LogInterceptor;
import com.example.commonlibrary.net.OkHttpGlobalHandler;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.FileUtil;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

/**
 * Created by COOTEK on 2017/7/28.
 */

public class BaseApplication extends Application {


    private static AppComponent appComponent;
    private static BaseApplication instance;
    private ApplicationDelegate applicationDelegate;

    public static BaseApplication getInstance() {
        return instance;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        applicationDelegate = new ApplicationDelegate();
        applicationDelegate.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        ARouter.openLog();     // 打印日志
        ARouter.openDebug();
        ARouter.init(this);
        initDagger();
        instance = this;
        applicationDelegate.onCreate(this);
       new ScreenAdaptManager.Builder().designedHeight(640)
               .designedWidth(360).build().initData(this);
    }


    private void initDagger() {
        AppConfigModule.Builder builder = new AppConfigModule.Builder();
        builder.baseUrl(HttpUrl.parse(ConstantUtil.BASE_URL))
                .gsonConfig((application, gsonBuilder) -> gsonBuilder.serializeNulls().enableComplexMapKeySerialization()).okHttpConfig(new GlobalConfigModule.OkHttpConfig() {
            @Override
            public void config(Application application, OkHttpClient.Builder builder) {
                builder.connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS);
            }
        }).retrofitConfig((application, builder1) -> {
//                这里的配置baseURL会覆盖之前的baseUrl
        }).okHttpGlobalHandler(new OkHttpGlobalHandler() {
            @Override
            public Response onResultResponse(String printResult, Interceptor.Chain chain, Response response) {
                CommonLogger.e("onResultResponse");
                return response;
            }

            @Override
            public Request onRequestBefore(Interceptor.Chain chain, Request request) {
                CommonLogger.e("onRequestBefore:" + request.url().toString());
                return request.newBuilder()
                        .header("User-Agent", "")
                        .url(request.url()).build();
            }
        }).level(LogInterceptor.Level.BODY).cacheFile(FileUtil.getDefaultCacheFile(this))
                .baseImageLoaderStrategy(new GlideImageLoaderStrategy());
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).globalConfigModule(new GlobalConfigModule())
                .appConfigModule(builder.build())
                .build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        applicationDelegate.onTerminate(this);
    }
}
