package com.example.news.interceptor;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.utils.AppUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/11     0:09
 * QQ:         1981367757
 */

public class CacheControlInterceptor implements Interceptor {
    private static final long CACHE_STALE_SEC =60 * 60 * 24L;
    public static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request=chain.request();
        Response response=chain.proceed(request);
        if (AppUtil.isNetworkAvailable(BaseApplication.getInstance())) {
            String cacheControl = request.cacheControl().toString();
          return   response.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
        }else {
            return response.newBuilder()
                    .header("Cache-Control", "public, " + CACHE_CONTROL_CACHE)
                    .removeHeader("Pragma")
                    .build();
        }
    }
}
