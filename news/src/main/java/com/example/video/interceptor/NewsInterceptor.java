package com.example.video.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 项目名称:    NewFastFrame+
 * <p>
 * 创建人:        陈锦军
 * 创建时间:    2017/9/18      18:01
 * QQ:             1981367757
 */

public class NewsInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(chain.request());
    }

}
