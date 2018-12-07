package com.example.commonlibrary.utils;

import android.os.Environment;

import com.example.commonlibrary.BuildConfig;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by COOTEK on 2017/7/31.
 */


public class Constant {
    //    默认Base URL
    //    8990正式服端口、、、、、、、、8992测试服端口

    public static final String BASE_URL = BuildConfig.DEBUG ? "http://120.78.168.113:8992/" : "http://bacao.wawazhuawawa.com:8990/";
    public static final String DESIGNED_WIDTH = "designed_width";
    public static final String DESIGNED_HEIGHT = "designed_height";
    public static final String UM_KEY = "5bbf0ba6f1f5568e9d000011";
    public static final String WALLPAPER = "wallpaper";
    public static final String DATA = "data";
    public static final String TITLE = "title";
    public static final String POSITION = "position";
    public static final String ALONE = "alone";
    private static final String BASE_CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "common" + File.separator;
    /**
     * 声音的缓存目录
     */
    public static final String VOICE_CACHE_DIR = BASE_CACHE_DIR + "voice" + File.separator;
    public static final String IMAGE_CACHE_DIR = BASE_CACHE_DIR + "image" + File.separator;
    public static final String VIDEO_CACHE_DIR = BASE_CACHE_DIR + "video" + File.separator;
    public static final String IMAGE_COMPRESS_DIR = BASE_CACHE_DIR + "compress" + File.separator;
    public static final int REQUEST_CODE_CROP = 1;
    public static final String BUGLY_ID = "cf73e8a627";
    public static final String TOKEN = "user_token";


    public static final String PUSH_NOTIFY = "push_notify";
    public static final String VOICE_STATUS = "voice_status";
    public static final String VIBRATE_STATUS = "vibrate_status";


    //    token过期code
    public static final List<Integer> tokenCodeList = Arrays.asList(402001, 402002, 402003);
    public static final String HEADER = "Authorization";
}
