package com.example.chat.util;

import android.app.Activity;
import android.net.Uri;
import android.os.Environment;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/26      22:26
 * QQ:             1981367757
 */
public class PhotoUtil {

        private static final String CROP_NAME = "crop.jpg";

        public static Uri buildUri(Activity activity) {
                if (CommonUtils.isSupportSdcard()) {
                       return Uri.fromFile(Environment.getExternalStorageDirectory()).buildUpon().appendPath(CROP_NAME).build();
                } else {
                        return Uri.fromFile(activity.getCacheDir()).buildUpon().appendPath(CROP_NAME).build();
                }
        }
}
