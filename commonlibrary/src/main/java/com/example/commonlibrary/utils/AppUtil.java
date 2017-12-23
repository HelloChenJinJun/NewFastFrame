package com.example.commonlibrary.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.List;

/**
 * 方法工具类
 */

public class AppUtil {
    /**
     * 描述：判断网络是否有效.
     *
     * @return true, if is network available
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


    /**
     * 读取baseurl
     *
     * @param url
     * @return
     */
    public static String getBasUrl(String url) {
        String head = "";
        int index = url.indexOf("://");
        if (index != -1) {
            head = url.substring(0, index + 3);
            url = url.substring(index + 3);
        }
        index = url.indexOf("/");
        if (index != -1) {
            url = url.substring(0, index + 1);
        }
        return head + url;
    }


    public static boolean isServiceRunning(Context context, String serviceName) {
        if (context == null || TextUtils.isEmpty(serviceName)) {
            return false;
        }
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfoList = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (runningServiceInfoList != null && runningServiceInfoList.size() > 0) {
            for (ActivityManager.RunningServiceInfo info :
                    runningServiceInfoList) {
                if (info.service.getClassName().equals(serviceName)) {
                    return true;
                }
            }
        }
        return false;
    }



    public static String getSortedKey(String name) {
        StringBuilder builder = new StringBuilder();
        String singleItem;
        for (int i = 0; i < name.length(); i++) {
            singleItem = getSinglePinYing(name.charAt(i));
            if (singleItem == null) {
                builder.append(name.charAt(i));
            } else {
                builder.append(singleItem);
            }
        }
        return builder.toString().substring(0, 1).toUpperCase();
    }

    private static String getSinglePinYing(char c) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        try {
            String[] results = PinyinHelper.toHanyuPinyinStringArray(c, format);
            if (results == null) {
//                                不是汉字返回空
                return null;
            } else {
//                                因为有可能是多音字
                return results[0];
            }
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
            return null;
        }
    }

}
