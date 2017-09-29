package com.example.cootek.newfastframe.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/29      11:40
 * QQ:             1981367757
 */

public class MusicInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest=chain.request();
       Request newRequest=oldRequest.newBuilder()
                .header("User-Agent", "")
                .url(oldRequest.url()).build();
        return chain.proceed(newRequest);
    }
}
