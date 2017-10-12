package com.example.chat.api;

import com.example.chat.util.CommonUtils;
import com.example.commonlibrary.BaseApplication;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/6      21:19
 * QQ:             1981367757
 */

public class HttpServiceManager {
        private static HttpServiceManager instance = null;
        private static OkHttpClient client = null;
        private static final String happyBaseUrl = "http://japi.juhe.cn";
        private static PictureApi sPictureApi;
        private static final String pictureBaseUrl = "http://gank.io";

        public static HttpServiceManager getInstance() {
                if (instance == null) {
                        instance = new HttpServiceManager();
                }
                return instance;
        }

        private static final String CACHE_CONTROL = "Cache_Control";

        private Interceptor cacheInterceptor = null;
        private static int netWorkCacheTime = 60;
        /**
         * 一周
         */
        private int outOfNetCacheTime = 60 * 60 * 24 * 7;
        private static long maxCache = 1024 * 1024 * 10;
        private static File cacheFileDir = new File(BaseApplication.getInstance().getCacheDir(), "tx_cache");
        private static Cache sCache = new Cache(cacheFileDir, maxCache);


        private HttpServiceManager() {
                if (cacheInterceptor == null) {
                        cacheInterceptor = new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                        Response response = chain.proceed(chain.request());
                                        if (CommonUtils.isNetWorkAvailable()) {
                                                return response.newBuilder().removeHeader("program").removeHeader(CACHE_CONTROL)
                                                        .addHeader(CACHE_CONTROL, "public, max-age=" + netWorkCacheTime).build();
                                        } else {
                                                return response.newBuilder().removeHeader("program").removeHeader(CACHE_CONTROL)
                                                        .addHeader(CACHE_CONTROL, "public, only-if-cached, max-stale=" + outOfNetCacheTime).build();
                                        }
                                }
                        };
                }
                if (client == null) {
                        client = new OkHttpClient.Builder().addInterceptor(cacheInterceptor).addNetworkInterceptor(cacheInterceptor).cache(sCache).build();
                }
        }

        private static TxApi sTxApi;
        private static HappyApi sHappyApi;
        private static final Object monitor = new Object();
        private static final Object happy = new Object();
        private static final String TxBaseUrl = "https://api.tianapi.com";

        public TxApi getTxApi() {
                if (sTxApi == null) {
                        synchronized (monitor) {
                                if (sTxApi == null) {
                                        sTxApi = new Retrofit.Builder().client(client).baseUrl(TxBaseUrl).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                                .addConverterFactory(GsonConverterFactory.create()).build().create(TxApi.class);
                                }
                        }
                }
                return sTxApi;
        }


        public HappyApi getHappyApi() {
                if (sHappyApi == null) {
                        synchronized (happy) {
                                if (sHappyApi == null) {
                                        sHappyApi = new Retrofit.Builder().client(client).baseUrl(happyBaseUrl).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                                .addConverterFactory(GsonConverterFactory.create()).build().create(HappyApi.class);
                                }
                        }
                }
                return sHappyApi;
        }


        public PictureApi getPictureApi() {
                if (sPictureApi == null) {
                        synchronized (monitor) {
                                if (sPictureApi == null) {
                                        sPictureApi = new Retrofit.Builder().client(client).baseUrl(pictureBaseUrl).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                                .addConverterFactory(GsonConverterFactory.create()).build().create(PictureApi.class);
                                }
                        }
                }
                return sPictureApi;
        }
}
