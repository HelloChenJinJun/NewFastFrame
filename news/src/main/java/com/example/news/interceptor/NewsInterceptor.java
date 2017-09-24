package com.example.news.interceptor;

import com.example.commonlibrary.BaseApplication;
import com.example.news.util.NewsUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
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
        Request request = chain.request();
        if (request.url().toString().equals(NewsUtil.LIBRARY_LOGIN)) {
            Response response = chain.proceed(request);
            String cookie = response.header("Set-Cookie", null);
            if (cookie != null) {
                BaseApplication.getAppComponent()
                        .getSharedPreferences().edit().putString(NewsUtil.LIBRARY_COOKIE, cookie)
                        .apply();
            }
            return response;
        }


        if (request.url().toString().equals(NewsUtil.CARD_LOGIN_URL)) {
            Request newRequest;
            newRequest = request.newBuilder().method(request.method(), request.body()).url(request.url())
                    .header("Cookie", getCardLoginCookie())
                    .build();
            Response response = chain.proceed(newRequest);
            String cookie = response.header("Set-Cookie", null);
            if (cookie != null) {
                BaseApplication.getAppComponent()
                        .getSharedPreferences().edit().putString(NewsUtil.CARD_LOGIN_COOKIE, cookie.substring(0, cookie.indexOf(";")))
                        .apply();
            }
            return response;
        }


        if (request.url().toString().startsWith(NewsUtil.CARD_VERIFY_IMAGE_URL)) {
            Request newRequest;
            if (BaseApplication.getAppComponent().getSharedPreferences().getString(NewsUtil.CARD_LOGIN_COOKIE, null) != null) {
                newRequest = request.newBuilder().method(request.method(), request.body()).url(request.url())
                        .header("Cookie", "_ga=GA1.3.1067555487.1498354603;" + BaseApplication.getAppComponent().getSharedPreferences().getString(NewsUtil.CARD_LOGIN_COOKIE, null))
                        .build();
            } else {
                newRequest = request;
            }
            Response response = chain.proceed(newRequest);
            return response;
        }
        if (request.url().toString().equals(NewsUtil.CARD_POST_LOGIN_URL)) {
            Request newRequest;
            if (BaseApplication.getAppComponent().getSharedPreferences().getString(NewsUtil.CARD_LOGIN_COOKIE, null) != null) {
                newRequest = request.newBuilder().method(request.method(), request.body()).url(request.url())
                        .header("Cookie", "_ga=GA1.3.1067555487.1498354603;" + BaseApplication.getAppComponent().getSharedPreferences().getString(NewsUtil.CARD_LOGIN_COOKIE, null))
                        .build();
            } else {
                newRequest = request;
            }
            Response response = chain.proceed(newRequest);
            String cookie = response.header("Set-Cookie", null);
            if (cookie != null) {
                String newCookie = cookie.substring(0, cookie.indexOf(";"));
                BaseApplication.getAppComponent()
                        .getSharedPreferences().edit().putString(NewsUtil.CARD_POST_LOGIN_COOKIE, newCookie)
                        .apply();
            }
            return response;
        }
        if (request.url().toString().equals(NewsUtil.CARD_PAGE_INFO_URL) ||
                request.url().toString().equals(NewsUtil.CARD_BANK_INFO_URL)
                || request.url().toString().equals(NewsUtil.PAY_URL)) {
            Request newRequest = request.newBuilder().method(request.method(), request.body()).url(request.url())
                    .header("Cookie", getCardLoginCookie())
                    .build();
            Response response = chain.proceed(newRequest);
            return response;
        }
        if (request.url().toString().startsWith("http://202.114.202.207/")) {
            if (BaseApplication.getAppComponent().getSharedPreferences().getString(NewsUtil.LIBRARY_COOKIE, null) != null) {
                Request newRequest = request.newBuilder().method(request.method(), request.body()).url(request.url())
                        .header("Cookie", BaseApplication.getAppComponent().getSharedPreferences().getString(NewsUtil.LIBRARY_COOKIE, null))
                        .build();
                return chain.proceed(newRequest);
            }
        }
        return chain.proceed(request);
    }

    private String getCardLoginCookie() {
        return "_ga=GA1.3.1067555487.1498354603;" + BaseApplication.getAppComponent().getSharedPreferences().getString(NewsUtil.CARD_LOGIN_COOKIE, null)
                + ";" + BaseApplication.getAppComponent().getSharedPreferences().getString(NewsUtil.CARD_POST_LOGIN_COOKIE, null);
    }
}
