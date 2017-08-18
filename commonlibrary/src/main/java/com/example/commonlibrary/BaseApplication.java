package com.example.commonlibrary;

import android.app.Application;

import com.example.commonlibrary.dagger.OkHttpGlobalHandler;
import com.example.commonlibrary.dagger.component.AppComponent;
import com.example.commonlibrary.dagger.component.DaggerAppComponent;
import com.example.commonlibrary.dagger.module.AppConfigModule;
import com.example.commonlibrary.dagger.module.AppModule;
import com.example.commonlibrary.dagger.module.NetClientModule;
import com.example.commonlibrary.imageloader.GlideImageLoaderStrategy;
import com.example.commonlibrary.interceptor.LogInterceptor;
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


    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initDagger();
        instance = this;
    }


    private void initDagger() {
        AppConfigModule.Builder builder = new AppConfigModule.Builder();
        builder.baseUrl(HttpUrl.parse(ConstantUtil.BASE_URL))
                .gsonConfig(new NetClientModule.GsonConfig() {
                    @Override
                    public void config(Application application, GsonBuilder gsonBuilder) {
                        gsonBuilder.serializeNulls().enableComplexMapKeySerialization();
                    }
                }).okHttpConfig(new NetClientModule.OkHttpConfig() {
            @Override
            public void config(Application application, OkHttpClient.Builder builder) {
                builder.connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS);
            }
        }).retrofitConfig(new NetClientModule.RetrofitConfig() {
            @Override
            public void config(Application application, Retrofit.Builder builder) {
//                这里的配置baseURL会覆盖之前的baseUrl
            }
        }).okHttpGlobalHandler(new OkHttpGlobalHandler() {
            @Override
            public Response onResultResponse(String printResult, Interceptor.Chain chain, Response response) {
                CommonLogger.e("onResultResponse");
                return response;
            }

            @Override
            public Request onRequestBefore(Interceptor.Chain chain, Request request) {
                CommonLogger.e("onRequestBefore:"+request.url().toString());
                return request.newBuilder()
//                        .header("cookie","BAIDUID=41F6024562091541FCEE149B292ACB04:FG=1")
//                        .header("accept-encoding","gzip, deflate")
//                        .header("Accept","*/*")
                        .header("User-Agent","")
                        .url(request.url()).build();
            }
        }).level(LogInterceptor.Level.BODY).cacheFile(FileUtil.getDefaultCacheFile(this))
                .baseImageLoaderStrategy(new GlideImageLoaderStrategy());
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).netClientModule(new NetClientModule())
                .appConfigModule(builder.build())
                .build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
