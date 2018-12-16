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

    public static final String M3U8 = ".m3u8";
    public static final String MD5 = "EFBFCB31D5BACE6BB5793A529254424E";

    public static final String QQ_TV_URL = "http://api.bbbbbb.me/qqmtv/api.php";
    public static final String URL_ONE = "http://app.baiyug.cn:2019/vip/akey.php";
    public static final String BASE_LIST_QQ_URL = "http://list.video.qq.com/fcgi-bin/list_select_cgi?platform=1&version=10000&otype=json&intfname=web_vip_movie_new";
    public static final String DATA = "data";
    public static final int BASE_TYPE_VIDEO_LIST_HEADER = 1;
    public static final int BASE_TYPE_VIDEO_LIST_DATA = 2;
    public static final String HOT_VIDEO_URL = "http://node.video.qq.com/x/api/hot_search/";
    public static final int BASE_TYPE_SEARCH_HOT = 10;
    public static final int BASE_TYPE_SEARCH_CONTENT = 11;
    public static final int BASE_TYPE_VIDEO_DETAIL_INFO = 20;

    public static String getSignedValue(String coreString) {
        JSEngine jsEngine = new JSEngine("vip.js");
        return jsEngine.runScript(jsEngine.runScript(coreString, "cover"), "sign");
    }

    private static String cover(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }


    // 转化十六进制编码为字符串
    public static String toStringHex2(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(
                        i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "utf-8");// UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }


    public static String getVideoHeaderType(int videoType) {
        if (videoType == 1) {
            return "web_vip_movie_new";
        } else {
            return "web_vip_tv_new";
        }
    }

    public static String getParseUrl(String id) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://v.qq.com/x/cover/").append(id).append(".html");
        //        "https://v.qq.com/x/cover/h0meep6p766jgqh.html"
        return stringBuilder.toString();
    }


    public static String getIdFromUrl(String url) {
        return url.substring(url.indexOf("cover/") + 6, url.indexOf(".html"));
    }


    public static String getVideoDetailUrl(String id) {
        //        https://v.qq.com/detail/b/bojb6fxtqh2ekw0.html
        StringBuilder stringBuilder = new StringBuilder("https://v.qq.com/detail/b/").append(id).append(".html");
        return stringBuilder.toString();
    }
}
