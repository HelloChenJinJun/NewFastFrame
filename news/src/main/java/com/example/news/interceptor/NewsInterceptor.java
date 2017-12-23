package com.example.news.interceptor;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.IOUtils;
import com.example.news.util.NewsUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

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
        String url=request.url().toString();
        if (url.equals(NewsUtil.LIBRARY_LOGIN)) {
            Response response = chain.proceed(request);
            String cookie = response.header("Set-Cookie", null);
            if (cookie != null) {
                BaseApplication.getAppComponent()
                        .getSharedPreferences().edit().putString(NewsUtil.LIBRARY_COOKIE, cookie)
                        .apply();
            }
            return response;
        }

        if (url.equals(NewsUtil.SYSTEM_INFO_INDEX_URL)) {
//            _ga=GA1.3.1067555487.1498354603;UM_distinctid=15ee5c241e60-0ccb143b01978-6a11157a-100200-15ee5c241eaa2;
            Request newRequest;
            newRequest = request.newBuilder().method(request.method(), request.body()).url(request.url())
                    .header("Cookie", "_ga=GA1.3.1067555487.1498354603;UM_distinctid=15ee5c241e60-0ccb143b01978-6a11157a-100200-15ee5c241eaa2;")
                    .build();
            Response response = chain.proceed(newRequest);
            List<String> cookieList = response.headers("Set-Cookie");
            if (cookieList != null && cookieList.size() > 0) {
                for (String cookie :
                        cookieList) {
                    if (cookie.contains("JSESSIONID")) {
                        BaseApplication.getAppComponent()
                                .getSharedPreferences().edit().putString(NewsUtil.SYSTEM_INFO_COOKIE, cookie.substring(0, cookie.indexOf(";")))
                                .apply();
                    }
                }
            }
            return response;
        }


        if (url.startsWith("http://xyfw.cug.edu.cn/tp_up/?ticket")) {
            Response response = chain.proceed(request);
            List<String> cookieList = response.headers("Set-Cookie");
            if (cookieList != null && cookieList.size() > 0) {
                for (String cookie :
                        cookieList) {
                    if (cookie.contains("tp_up")) {
                        BaseApplication.getAppComponent().getSharedPreferences()
                                .edit().putString(NewsUtil.SYSTEM_INFO_TP_UP, cookie.substring(0, cookie.indexOf(";")))
                                .apply();
                    }
                }
            }
            return response;
        }
        if (url.equals(NewsUtil.SCORE_QUERY_URL)||
                url.equals(NewsUtil.CONSUME_QUERY_URL)
                ||url.equals(NewsUtil.SYSTEM_USER_INFO_URL)) {
            Request newRequest;
            StringBuilder cookie = new StringBuilder();
            cookie.append("_ga=GA1.3.1067555487.1498354603;UM_distinctid=15ee5c241e60-0ccb143b01978-6a11157a-100200-15ee5c241eaa2;")
                    .append(BaseApplication.getAppComponent()
                            .getSharedPreferences().getString(NewsUtil.SYSTEM_INFO_TP_UP, null));
            String loginCookie=BaseApplication.getAppComponent().getSharedPreferences().getString(NewsUtil.SYSTEM_INFO_COOKIE,null);
            if (loginCookie != null) {
                cookie.append(";").append(loginCookie);
            }
            newRequest = request.newBuilder().method(request.method(), request.body()).url(request.url())
                    .header("Cookie", cookie.toString())
                    .build();
            Response response = chain.proceed(newRequest);
            return response;
        }

        if (url.equals(NewsUtil.getRealLoginUrl(BaseApplication
                .getAppComponent().getSharedPreferences().getString(NewsUtil.SYSTEM_INFO_COOKIE, null)))) {
            Request newRequest;
            newRequest = request.newBuilder().method(request.method(), request.body()).url(request.url())
                    .header("Cookie", "_ga=GA1.3.1067555487.1498354603;UM_distinctid=15ee5c241e60-0ccb143b01978-6a11157a-100200-15ee5c241eaa2;Language=zh_CN;"
                            + BaseApplication
                            .getAppComponent().getSharedPreferences().getString(NewsUtil.SYSTEM_INFO_COOKIE, null)
                    )
                    .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Accept-Encoding","gzip,deflate")
                    .header("Accept-Language","zh-CN,zh;q=0.8")
                    .header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                    .header("Referer","http://sfrz.cug.edu.cn/tpass/login?service=http%3A%2F%2Fxyfw.cug.edu.cn%2Ftp_up%2F")
                    .header("Origin","http://sfrz.cug.edu.cn")
                    .header("Upgrade-Insecure-Requests","1")
                    .header("Cache-Control","max-age=0")
                    .build();
            Response response = chain.proceed(newRequest);
            List<String> cookieList = response.headers("Set-Cookie");
            String ticketUrl = response.header("Location", null);
            if (ticketUrl != null) {
                BaseApplication.getAppComponent().getSharedPreferences()
                        .edit().putString(NewsUtil.SYSTEM_INFO_GET_TICKET, ticketUrl).apply();
            }
            if (cookieList != null && cookieList.size() > 0) {
                for (String cookie :
                        cookieList) {
//                    String newCookie = cookie.substring(0, cookie.indexOf(";"));
                    if (cookie.contains("CASTGC")) {
                        BaseApplication.getAppComponent()
                                .getSharedPreferences().edit().putString(NewsUtil.STSTEM_INFO_CASTGC, cookie.substring(0, cookie.indexOf(";")))
                                .apply();
                    }
                }
            }
            return response;
        }


        if (url.equals(NewsUtil.CARD_LOGIN_URL)) {
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


        if (url.startsWith(NewsUtil.CARD_VERIFY_IMAGE_URL)) {
            Request newRequest;
            if (BaseApplication.getAppComponent().getSharedPreferences().getString(NewsUtil.CARD_LOGIN_COOKIE, null) != null) {
                newRequest = request.newBuilder().method(request.method(), request.body()).url(request.url())
                        .header("Cookie", getCardLoginCookie())
                        .build();
            } else {
                newRequest = request;
            }
            Response response = chain.proceed(newRequest);
            return response;
        }
        if (url.equals(NewsUtil.CARD_POST_LOGIN_URL)) {
            Request newRequest;
            if (BaseApplication.getAppComponent().getSharedPreferences().getString(NewsUtil.CARD_LOGIN_COOKIE, null) != null) {
                newRequest = request.newBuilder().method(request.method(), request.body()).url(request.url())
                        .header("Cookie", getCardLoginCookie())
                        .header("X-Requested-With", "XMLHttpRequest")
                        .header("Accept", "application/json, text/javascript, */*; q=0.01")
                        .header("Accept-Language", "zh-CN,zh;q=0.8")
                        .header("Accept-Encoding", "gzip, deflate")
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                        .header("Referer", "http://card.cug.edu.cn/")
                        .header("Origin", "http://card.cug.edu.cn")
                        .build();
            } else {
                newRequest = request;
            }
            Response response = chain.proceed(newRequest);
            List<String> cookieList = response.headers("Set-Cookie");


            if (cookieList != null && cookieList.size() > 1) {
                for (String temp :
                        cookieList) {
                    String newCookie = temp.substring(0, temp.indexOf(";"));
                    if (temp.contains("username")) {
                        BaseApplication.getAppComponent()
                                .getSharedPreferences().edit().putString(NewsUtil.CARD_POST_LOGIN_COOKIE_USER_NAME, newCookie)
                                .apply();
                    } else {
                        BaseApplication.getAppComponent()
                                .getSharedPreferences().edit().putString(NewsUtil.CARD_POST_LOGIN_COOKIE, newCookie)
                                .apply();
                    }
                }
            }

            return response;
        }
        if (url.equals(NewsUtil.CARD_PAGE_INFO_URL) ||
                request.url().toString().equals(NewsUtil.CARD_BANK_INFO_URL)
                || request.url().toString().equals(NewsUtil.PAY_URL)) {
            Request newRequest = request.newBuilder().method(request.method(), request.body()).url(request.url())
                    .header("Cookie", getCardLoginCookie() + ";" + BaseApplication.getAppComponent().getSharedPreferences()
                            .getString(NewsUtil.CARD_POST_LOGIN_COOKIE_USER_NAME, null))
                    .build();

            Response response = chain.proceed(newRequest);
            return response;
        }
        if (url.startsWith("http://202.114.202.207/")) {
            if (BaseApplication.getAppComponent().getSharedPreferences().getString(NewsUtil.LIBRARY_COOKIE, null) != null) {
                Request newRequest = request.newBuilder().method(request.method(), request.body()).url(request.url())
                        .header("Cookie", BaseApplication.getAppComponent().getSharedPreferences().getString(NewsUtil.LIBRARY_COOKIE, null))
                        .build();
                return chain.proceed(newRequest);
            }
        } else if (url.startsWith(NewsUtil.WY_BASE_URL)
                ) {
            Response response = chain.proceed(chain.request());
            ResponseBody responseBody = response.body();
            byte[] bytes = IOUtils.toByteArray(responseBody
                    .byteStream());
            Charset charset = Charset.forName("gb2312");
            String body = new String(bytes, charset);
            responseBody = ResponseBody.create(MediaType.parse("text/html; charset=gb2312"), bytes);
            return response.newBuilder().body(responseBody).build();
        }
        return chain.proceed(request);
    }

    private String getCardLoginCookie() {
        StringBuilder result = new StringBuilder();
        result.append("_ga=GA1.3.1067555487.1498354603;UM_distinctid=15ee5c241e60-0ccb143b01978-6a11157a-100200-15ee5c241eaa2;");
        if (BaseApplication.getAppComponent().getSharedPreferences().getString(NewsUtil.CARD_LOGIN_COOKIE, null) != null) {
            result.append(BaseApplication.getAppComponent().getSharedPreferences().getString(NewsUtil.CARD_LOGIN_COOKIE, null));
            result.append(";");
        }
        if (BaseApplication.getAppComponent().getSharedPreferences().getString(NewsUtil.CARD_POST_LOGIN_COOKIE, null) != null) {
            result.append(BaseApplication.getAppComponent().getSharedPreferences().getString(NewsUtil.CARD_POST_LOGIN_COOKIE, null));
            result.append(";");
        }
        return result.toString();
    }
}
