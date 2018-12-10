package com.snew.video.interceptor;

import android.content.SharedPreferences;

import com.example.commonlibrary.BaseApplication;
import com.snew.video.util.VideoUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 项目名称:    Update
 * 创建人:      陈锦军
 * 创建时间:    2018/11/29     10:23
 */
public class IndexInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        String url = chain.request().url().toString();
        if (!url.contains(VideoUtil.BASE_VIDEO_URL)) {
            Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.7 Safari/537.36");
            builder.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
            builder.addHeader("Accept-Encoding", "gzip, deflate");
            builder.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
            builder.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            builder.addHeader("Origin", "http://toutiao.iiilab.com");
            builder.addHeader("Referer", "http://toutiao.iiilab.com/");
            builder.addHeader("Connection", "keep-alive");
            Response response;
            SharedPreferences sharedPreferences = BaseApplication.getAppComponent().getSharedPreferences();
            String iii = sharedPreferences.getString(VideoUtil.III_SESSION, null);
            StringBuilder cookie = new StringBuilder("_ga=GA1.2.1176342053.1543471162;_gid=GA1.2.1372733798.1543471162;_gat=1;");
            if (iii != null) {
                cookie.append(iii);
            }
            String php = sharedPreferences.getString(VideoUtil.PHPSESSIID, null);
            if (php != null) {
                cookie.append(";").append(php);
            }
            String gsp = sharedPreferences.getString(VideoUtil.GSP, null);
            if (gsp != null) {
                cookie.append(";").append(gsp);
            }
            builder.header("Cookie", cookie.toString());
            response = chain.proceed(builder.build());
            List<String> cookieList = response.headers("Set-Cookie");
            //        iii_Session=rsrnan74j85dp27kfl988th8v0; path=/; domain=iiilab.com
            //        PHPSESSIID=651779043456; path=/; domain=iiilab.com
            if (cookieList != null && cookieList.size() > 0) {
                SharedPreferences.Editor share = BaseApplication.getAppComponent().getSharedPreferences().edit();
                for (String s :
                        cookieList) {
                    if (s.contains("iii_Session")) {
                        share.putString(VideoUtil.III_SESSION, s.substring(0, s.indexOf(";")))
                                .apply();
                    } else if (s.contains("PHPSESSIID")) {
                        share.putString(VideoUtil.PHPSESSIID, s.substring(0, s.indexOf(";")));
                    } else if (s.contains("_gsp")) {
                        share.putString(VideoUtil.GSP, s.substring(0, s.indexOf(";")));
                    }
                }
                share.apply();
            }
            return response;
        } else {
            return chain.proceed(chain.request());
        }
    }
}
