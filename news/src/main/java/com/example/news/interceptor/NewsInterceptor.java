package com.example.news.interceptor;

import android.content.SharedPreferences;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ConstantUtil;
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


    private SharedPreferences sharedPreferences;


    public NewsInterceptor() {
        sharedPreferences = BaseApplication.getAppComponent().getSharedPreferences();
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String url = request.url().toString();
        if (url.equals(NewsUtil.LIBRARY_LOGIN)) {
            Response response = chain.proceed(request);
            String cookie = response.header("Set-Cookie", null);
            if (cookie != null) {
                sharedPreferences.edit().putString(NewsUtil.LIBRARY_COOKIE, cookie)
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
                        sharedPreferences.edit().putString(NewsUtil.SYSTEM_INFO_COOKIE, cookie.substring(0, cookie.indexOf(";")))
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
                        sharedPreferences
                                .edit().putString(NewsUtil.SYSTEM_INFO_TP_UP, cookie.substring(0, cookie.indexOf(";")))
                                .apply();
                    }
                }
            }
            return response;
        }
        if (url.equals(NewsUtil.SCORE_QUERY_URL) ||
                url.equals(NewsUtil.CONSUME_QUERY_URL)
                || url.equals(NewsUtil.SYSTEM_USER_INFO_URL)
                ||url.equals(NewsUtil.PW_RESET_URL)) {
            Request newRequest;
            StringBuilder cookie = new StringBuilder();
            cookie.append("_ga=GA1.3.1067555487.1498354603;UM_distinctid=15ee5c241e60-0ccb143b01978-6a11157a-100200-15ee5c241eaa2;")
                    .append(sharedPreferences.getString(NewsUtil.SYSTEM_INFO_TP_UP, null));
            String loginCookie = sharedPreferences.getString(NewsUtil.SYSTEM_INFO_COOKIE, null);
            if (loginCookie != null) {
                cookie.append(";").append(loginCookie);
            }
            newRequest = request.newBuilder().method(request.method(), request.body()).url(request.url())
                    .header("Cookie", cookie.toString())
                    .build();
            Response response = chain.proceed(newRequest);
            return response;
        }

        if (url.equals(NewsUtil.getRealLoginUrl(sharedPreferences.getString(NewsUtil.SYSTEM_INFO_COOKIE, null)))) {
            Request newRequest;
            newRequest = request.newBuilder().method(request.method(), request.body()).url(request.url())
                    .header("Cookie", "_ga=GA1.3.1067555487.1498354603;UM_distinctid=15ee5c241e60-0ccb143b01978-6a11157a-100200-15ee5c241eaa2;Language=zh_CN;"
                            + sharedPreferences.getString(NewsUtil.SYSTEM_INFO_COOKIE, null)
                    )
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Accept-Encoding", "gzip,deflate")
                    .header("Accept-Language", "zh-CN,zh;q=0.8")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                    .header("Referer", "http://sfrz.cug.edu.cn/tpass/login?service=http%3A%2F%2Fxyfw.cug.edu.cn%2Ftp_up%2F")
                    .header("Origin", "http://sfrz.cug.edu.cn")
                    .header("Upgrade-Insecure-Requests", "1")
                    .header("Cache-Control", "max-age=0")
                    .build();
            Response response = chain.proceed(newRequest);
            String ticketUrl = response.header("Location", null);
            if (ticketUrl != null) {
                sharedPreferences
                        .edit().putString(NewsUtil.SYSTEM_INFO_GET_TICKET, ticketUrl).apply();
            }
            List<String> cookieList = response.headers("Set-Cookie");
            if (cookieList != null && cookieList.size() > 0) {
                for (String cookie :
                        cookieList) {
//                    String newCookie = cookie.substring(0, cookie.indexOf(";"));
                    if (cookie.contains("CASTGC")) {
                        sharedPreferences.edit().putString(NewsUtil.STSTEM_INFO_CASTGC, cookie.substring(0, cookie.indexOf(";")))
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
                sharedPreferences.edit().putString(NewsUtil.CARD_LOGIN_COOKIE, cookie.substring(0, cookie.indexOf(";")))
                        .apply();
            }
            return response;
        }


        if (url.startsWith(NewsUtil.CARD_VERIFY_IMAGE_URL)) {
            Request newRequest;
            if (sharedPreferences.getString(NewsUtil.CARD_LOGIN_COOKIE, null) != null) {
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
            if (sharedPreferences.getString(NewsUtil.CARD_LOGIN_COOKIE, null) != null) {
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
                        sharedPreferences.edit().putString(NewsUtil.CARD_POST_LOGIN_COOKIE_USER_NAME, newCookie)
                                .apply();
                    } else {
                        sharedPreferences.edit().putString(NewsUtil.CARD_POST_LOGIN_COOKIE, newCookie)
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
            if (sharedPreferences.getString(NewsUtil.LIBRARY_COOKIE, null) != null) {
                Request newRequest = request.newBuilder().method(request.method(), request.body()).url(request.url())
                        .header("Cookie", sharedPreferences.getString(NewsUtil.LIBRARY_COOKIE, null))
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

//        课表查询


        if (url.equals(NewsUtil.COURSE_VERIFY_ACCOUNT_URL)) {
//               Cookie:
// CASTGC=TGT-20141000341-495-yI6jVKNd7UqDDrkeeBVDzpoW2cEvOQzCF6qgbu7SSjbdcqy0NC-tpass;
// _ga=GA1.3.1067555487.1498354603;
// UM_distinctid=15ee5c241e60-0ccb143b01978-6a11157a-100200-15ee5c241eaa2;
// Language=zh_CN;
// JSESSIONID=PVeNmYu09AqRgk2trD4UAt_g6WFKQxfwz4bolxAiKH4CFpc48cOH!-801081030
            StringBuilder result = new StringBuilder();
            result.append("_ga=GA1.3.1067555487.1498354603;UM_distinctid=15ee5c241e60-0ccb143b01978-6a11157a-100200-15ee5c241eaa2;Language=zh_CN;");
            result.append(sharedPreferences.getString(NewsUtil.SYSTEM_INFO_COOKIE, null))
                    .append(";")
                    .append(sharedPreferences.getString(NewsUtil.STSTEM_INFO_CASTGC, null));
            Request newRequest = request.newBuilder().method(request.method(), request.body()).url(request.url())
                    .header("Cookie", result.toString())
                    .build();

            Response response = chain.proceed(newRequest);
            String location = response.header("Location");
            if (location != null) {
                sharedPreferences.edit().putString(NewsUtil.COURSE_TICKET_URL, location).apply();
            }
            return response;
        }
//        String temp=sharedPreferences.getString(NewsUtil.COURSE_TICKET_URL,null);
        if (url.startsWith("http://202.114.207.137")
                &&url.contains("&ticket=")) {
            Response response = chain.proceed(request);
            String location = response.header("Location");
            if (location != null) {
                sharedPreferences.edit().putString(NewsUtil.COURSE_JSESSION_URL, location).apply();
            }
            List<String> cookieList = response.headers("Set-Cookie");
            if (cookieList != null && cookieList.size() > 0) {
                for (String cookie :
                        cookieList) {
//                    String newCookie = cookie.substring(0, cookie.indexOf(";"));
                    if (cookie.contains("JSESSIONID")) {
                        sharedPreferences.edit().putString(NewsUtil.COURSE_TEMP_JS_ID, cookie.substring(0, cookie.indexOf(";")))
                                .apply();
                    }
                }
            }
            return response;
        }

        if (url.startsWith("http://202.114.207.137")
                &&url.contains(";jsessionid=")) {

            Request newRequest = request.newBuilder().method(request.method(), request.body()).url(request.url())
                    .header("Cookie", sharedPreferences.getString(NewsUtil.COURSE_TEMP_JS_ID, null))
                    .build();
            Response response = chain.proceed(newRequest);
            String location = response.header("Location");
            if (location != null) {
                sharedPreferences.edit().putString(NewsUtil.COURSE_REAL_VERIFY_URL, location).apply();
            }
            return response;
        }


        if (url.equals(sharedPreferences.getString(NewsUtil.COURSE_REAL_VERIFY_URL, null))) {
//            _ga=GA1.3.1067555487.1498354603;
// UM_distinctid=15ee5c241e60-0ccb143b01978-6a11157a-100200-15ee5c241eaa2
            Request newRequest = request.newBuilder().method(request.method(), request.body()).url(request.url())
                    .header("Cookie", "_ga=GA1.3.1067555487.1498354603; UM_distinctid=15ee5c241e60-0ccb143b01978-6a11157a-100200-15ee5c241eaa2")
                    .build();
            Response response = chain.proceed(newRequest);
            List<String> cookieList = response.headers("Set-Cookie");
            if (cookieList != null && cookieList.size() > 0) {
                for (String cookie :
                        cookieList) {
//                    String newCookie = cookie.substring(0, cookie.indexOf(";"));
                    if (cookie.contains("JSESSIONID")) {
                        sharedPreferences.edit().putString(NewsUtil.COURSE_JSESSION_ID, cookie.substring(0, cookie.indexOf(";")))
                                .apply();
                    }
                }
            }
            return response;
        }

        if (url.equals(NewsUtil.COURSE_QUERY_URL)) {
            String origin = "http://jwgl.cug.edu.cn/jwglxt/kbcx/xskbcx_cxXskbcxIndex.html?gnmkdm=N2151&layout=default&su="
                    + sharedPreferences.getString(ConstantUtil.ACCOUNT, null);
            Request newRequest = request.newBuilder().method(request.method(), request.body()).url(request.url())
                    .header("Cookie", "_ga=GA1.3.1067555487.1498354603; UM_distinctid=15ee5c241e60-0ccb143b01978-6a11157a-100200-15ee5c241eaa2;"
                            + sharedPreferences.getString(NewsUtil.COURSE_JSESSION_ID, null))
                    .header("Referer", origin)
                    .build();
            return chain.proceed(newRequest);
        }
        if (url.startsWith("http://jwgl.cug.edu.cn/jwglxt/xsxxxggl/xsgrxxwh_cxXsgrxx.html")) {
            Request newRequest = request.newBuilder().method(request.method(), request.body()).url(request.url())
                    .header("Cookie", "_ga=GA1.3.1067555487.1498354603; UM_distinctid=15ee5c241e60-0ccb143b01978-6a11157a-100200-15ee5c241eaa2;"
                            + sharedPreferences.getString(NewsUtil.COURSE_JSESSION_ID, null))
                    .build();
            return chain.proceed(newRequest);
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
