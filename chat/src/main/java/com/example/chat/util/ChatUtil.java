package com.example.chat.util;

import com.example.chat.base.Constant;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/10/8      21:03
 * QQ:             1981367757
 */

public class ChatUtil {
    public static final String LOGIN_STATUS = "LOGIN_STATUS";
    public static final String VIBRATE_STATUS = "VIBRATE_STATUS";
    public static final String VOICE_STATUS = "VOICE_STATUS";
    public static final String PUSH_NOTIFY = "PUSH_NOTIFY";
    public static final String DELTA_TIME = "DELTA_TIME";
    public static final String BASE_URL = "http://www.quanmin.tv/";
    public static final String LAST_GROUP_MESSAGE_TIME = "last_group_message_time";
    public static final String LAST_SHARE_MESSAGE_TIME = "last_share_message_time";
    public static final String USER_DATA_LAST_UPDATE_TIME = "user_data_last_update_time";
    public static final String LOCATION = "location";
    public static final String ADDRESS = "ADDRESS";
    public static final String PUSH_STATUS = "PUSH_STATUS";



//            @GET("/wxnew/?key=" + Constant.TIAN_XING_KEY + "&num=20")
    public static String getWinXinUrl(int page) {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("https://api.tianapi.com/wxnew?key=")
                .append(Constant.TIAN_XING_KEY).append("&num=20&page=").append(page);
        return stringBuilder.toString();
    }


//        @GET("/joke/img/text.from?key=" + Constant.JU_HE_KEY)

    public static String getHappyUrl(int page, int num) {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("http://japi.juhe.cn/joke/img/text.from?key=")
                .append(Constant.JU_HE_KEY).append("&page=").append(page)
        .append("&pagesize=").append(num);
        return stringBuilder.toString();
    }
//        @GET("/joke/content/text.from?key=" + Constant.JU_HE_KEY)

    public static String getHappyContentUrl(int page, int num) {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("http://japi.juhe.cn/joke/content/text.from?key=")
                .append(Constant.JU_HE_KEY).append("&page=").append(page)
                .append("&pagesize=").append(num);
        return stringBuilder.toString();
    }


//            @GET("/api/data/福利/10/{page}")
//    http://gank.io
    public static String getPictureUrl(int page) {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("http://gank.io/api/data/福利/10/").append(page);
        return stringBuilder.toString();
    }
}
