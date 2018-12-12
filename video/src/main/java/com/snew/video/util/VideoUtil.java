package com.snew.video.util;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/8     17:28
 */
public class VideoUtil {
    public static final String BASE_URL = "http://c.3g.163.com/";

    public static final String III_SESSION = "III_SESSION";
    public static final String PHPSESSIID = "PHPSESSIID";
    public static final String GSP = "gsp";
    public static final String POSITION = "position";
    public static final String VIDEO_TYPE = "video_type";
    public static final String BASE_VIDEO_URL = "https://www.ixigua.com/";
    public static final String QQ_VIDEO_BASE_URL = "http://list.video.qq.com/fcgi-bin/list_common_cgi?otype=json&platform=1&version=10000&intfname=web_vip_movie_new&tid=687&appkey=c8094537f5337021&appid=200010596";
    public static final String M3U8 = ".m3u8";
    public static final String MD5 = "EFBFCB31D5BACE6BB5793A529254424E";
    public static final String QQ_TV_URL = "http://api.bbbbbb.me/qqmtv/api.php";

    public static String getSignedValue() {
        JSEngine jsEngine = new JSEngine("vip.js");
        return jsEngine.runScript(MD5, "sign");
    }
}
