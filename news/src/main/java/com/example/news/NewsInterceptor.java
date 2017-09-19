package com.example.news;

import com.example.commonlibrary.BaseApplication;
import com.example.news.util.NewsUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 项目名称:    NewFastFrame+
 *
 * 创建人:        陈锦军
 * 创建时间:    2017/9/18      18:01
 * QQ:             1981367757
 */

public class NewsInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request=chain.request();

        if (request.url().toString().equals(NewsUtil.LIBRARY_LOGIN)) {
           Response response=chain.proceed(request);
            String cookie=response.header("Set-Cookie",null);
            if (cookie != null) {
                BaseApplication.getAppComponent()
                        .getSharedPreferences().edit().putString(NewsUtil.LIBRARY_COOKIE,cookie)
                .apply();
            }
            return response;
        }
        if (request.url().toString().startsWith(NewsUtil.BASE_URL)) {
            if (BaseApplication.getAppComponent().getSharedPreferences().getString(NewsUtil.LIBRARY_COOKIE, null) != null) {
               Request newRequest=request.newBuilder().method(request.method(),request.body()).url(request.url())
                        .header("Cookie",BaseApplication.getAppComponent().getSharedPreferences().getString(NewsUtil.LIBRARY_COOKIE, null))
                        .build();
                return chain.proceed(newRequest);
            }
        }
        return chain.proceed(request);
    }
}
