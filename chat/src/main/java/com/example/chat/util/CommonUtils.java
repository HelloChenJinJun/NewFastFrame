package com.example.chat.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.example.chat.manager.UserManager;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.utils.DensityUtil;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/12      22:32
 * QQ:             1981367757
 */
public class CommonUtils {



        /**
         * MD5加码 生成32位md5码
         *
         * @param currentUserObjectId 本用户ID
         * @return MD5加码后的32位md5码
         */
        public static String md5(String currentUserObjectId) {
                MessageDigest md5 = null;
                try {
                        md5 = MessageDigest.getInstance("MD5");
                } catch (Exception e) {
                        System.out.println(e.toString());
                        e.printStackTrace();
                        return "";
                }
                char[] charArray = currentUserObjectId.toCharArray();
                byte[] byteArray = new byte[charArray.length];

                for (int i = 0; i < charArray.length; i++)
                        byteArray[i] = (byte) charArray[i];
                byte[] md5Bytes = md5.digest(byteArray);
                StringBuffer hexValue = new StringBuffer();
                for (int i = 0; i < md5Bytes.length; i++) {
                        int val = ((int) md5Bytes[i]) & 0xff;
                        if (val < 16)
                                hexValue.append("0");
                        hexValue.append(Integer.toHexString(val));
                }
                return hexValue.toString();
        }











        public static void showSoftInput(Context context, View view) {
                LogUtil.e("真正显示输入法");
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                view.requestFocus();
                //imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }

        public static void hideSoftInput(Context context, View view) {
                LogUtil.e("真正关闭输入法");
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
        }





        public static boolean isPhone(String mobiles) {
                String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
                if (TextUtils.isEmpty(mobiles)) return false;
                else return mobiles.matches(telRegex);
        }

        public static String getDistance(double longitude, double latitude) {
                double localLongitude = UserManager.getInstance().getCurrentUser().getLocation().getLongitude();
                double localLatitude = UserManager.getInstance().getCurrentUser().getLocation().getLatitude();
                int distance = (int) AMapUtils.calculateLineDistance(new LatLng(localLatitude, localLongitude), new LatLng(latitude, longitude));
                return distance + "";
        }
}
