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
    public static final String VIDEO_URL_TYPE = "video_url_type";
    public static final int VIDEO_URL_TYPE_QQ = 20;
    public static final int VIDEO_URL_TYPE_UPDATE = 21;
    public static final int BASE_TYPE_VIDEO_DETAIL_URL = 21;
    public static final String VIDEO_ID = "video_id";
    public static final int BASE_TYPE_ACTOR_VIDEO_DATA = 11;


    //    视频类型
    public static final int VIDEO_TYPE_QQ_CAMERA = 1;
    public static final int VIDEO_TYPE_QQ_TV = 2;
    public static final int VIDEO_TYPE_QQ_CARTOON = 3;
    public static final int VIDEO_TYPE_QQ_VARIETY = 10;
    public static final int VIDEO_TYPE_QQ_RECORD = 9;
    public static final int VIDEO_TYPE_QQ_MV = 11;


//    明星
    public static final int VIDEO_TYPE_QQ_STAR=17;


    public static String getSignedValue(String md5) {
        JSEngine jsEngine = new JSEngine("vip.js");
        return jsEngine.runScript(md5, "sign");
    }


    public static String getMd5Value(String content) {
        String s = "\"\\x24\\x28\\x27\\x23\\x68\\x64\\x4d\\x64\\x35\\x27\\x29\\x2e\\x76\\x61\\x6c\\x28\\x27\\x63\\x34\\x35\\x36\\x30\\x62\\x34\\x35\\x38\\x33\\x62\\x39\\x30\\x65\\x35\\x39\\x36\\x36\\x34\\x37\\x35\\x36\\x31\\x34\\x66\\x35\\x35\\x38\\x30\\x61\\x34\\x34\\x27\\x29\\x3b\"";
        String start = "eval(";
        int index = content.lastIndexOf(start);
        String coreString = content.substring(index + start.length(), index + start.length() + s.length());
        JSEngine jsEngine = new JSEngine("vip.js");
        return jsEngine.runScript(coreString, "cover");
    }


    public static String cmd5(String content) {
        JSEngine jsEngine = new JSEngine("cmd5.js");
        return jsEngine.runScript(content, "desn");
    }


    public static String getVideoHeaderType(int videoType) {
        if (videoType == 1) {
            return "web_vip_movie_new";
        } else if (videoType == 2) {
            return "web_vip_tv_new";
        } else if (videoType == 3) {
            return "web_vip_cartoon_new";
        } else if (videoType == 10) {
            return "vip_variety";
        } else if (videoType == 9) {
            return "web_vip_doco_new";
        } else if (videoType == 22) {
            return "web_vip_music_new";
        }
        return null;
    }

    public static String getParseUrl(String id, int videoUrlType) {
        StringBuilder stringBuilder = new StringBuilder();
        if (videoUrlType == VideoUtil.VIDEO_URL_TYPE_QQ) {
            stringBuilder.append("https://v.qq.com/x/cover/").append(id).append(".html");
        } else {
            //        http://m.bt361.cn/vod/detail/id/79618/
            stringBuilder.append("http://m.bt361.cn/index.php/vod/detail/id/").append(id).append("/");
        }
        //        "https://v.qq.com/x/cover/h0meep6p766jgqh.html"
        return stringBuilder.toString();
    }


    public static String getIdFromUrl(String url) {
        return url.substring(url.indexOf("cover/") + 6, url.indexOf(".html"));
    }


    public static String getVideoDetailUrl(String id) {
        //        https://v.qq.com/detail/b/bojb6fxtqh2ekw0.html

        try {
            Integer.parseInt(id);
            return getVideoVarietyDetailUrl(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return new StringBuilder("https://v.qq.com/detail/b/").append(id).append(".html").toString();
        }
    }

    public static int getVideoVersion(int videoType) {
        if (videoType == 10) {
            return 20340;
        } else {
            return 10000;
        }
    }

    public static int getSourceType(int videoType) {
        if (videoType == 10) {
            return 3;
        } else {
            return 1;
        }
    }

    public static String getVideoVarietyDetailUrl(String id) {
        //        http://v.qq.com/detail/8/80464.html
        StringBuilder stringBuilder = new StringBuilder("http://v.qq.com/detail/8/").append(id).append(".html");
        return stringBuilder.toString();
    }
}
