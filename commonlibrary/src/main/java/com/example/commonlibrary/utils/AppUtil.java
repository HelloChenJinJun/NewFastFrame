package com.example.commonlibrary.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.example.commonlibrary.BaseApplication;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static boolean isNetworkAvailable() {
        return isNetworkAvailable(BaseApplication.getInstance());
    }


    public static boolean checkPackInfo(Context context, String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
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


    public static String getSortedKey(String name) {
        if (name == null) {
            return "";
        }
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

    public static String getPinYin(String name) {
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
        return builder.toString().toUpperCase();
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






    /**
     * 邮箱格式是否正确
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (TextUtils.isEmpty(email))
            return false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }


    public static boolean isPhone(String mobiles) {
        String telRegex = "[1][3456789]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    public static PackageInfo getPackageInfo() {
        try {
            return BaseApplication.getInstance().getPackageManager()
                    .getPackageInfo(BaseApplication.getInstance().getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
