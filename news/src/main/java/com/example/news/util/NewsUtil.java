package com.example.news.util;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/17      9:36
 * QQ:             1981367757
 */

public class NewsUtil {

    public static final String TITLE = "title";

    //  http://202.114.202.207/
    public static final String BASE_URL = "http://c.3g.163.com/";

    public static final String PHOTO_SET = "photoset";
    public static final String SPECIAL_TITLE = "special";
    public static final String SPECIAL_ID = "special_id";
    public static final String POST_ID = "post_id";
    public static final String PHOTO_SET_ID = "photo_set_id";

    public static final String HEADER_AGENT = "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";
    public static final String CACHE_CONTROL = "Cache-Control: public, max-age=3600";


    //    http://gank.io/api/data/福利/10/{page}
    public static String getPhotoListUrl(int size, int num) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://gank.io/api/data/福利/").append(size)
                .append("/").append(num);
        return stringBuilder.toString();
    }


}
