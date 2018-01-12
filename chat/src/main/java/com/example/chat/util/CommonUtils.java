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
         * 判断是否有网络
         *
         * @param context
         * @return
         */
        public static boolean isNetWorkAvailable(Context context) {
                NetworkInfo info = getNetWorkInfo(context);
                if (info != null) {
                        return info.isAvailable();
                }
                return false;
        }


        public static boolean isAppOnForeground(Context context) {
                ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> list = manager.getRunningAppProcesses();
                if (list == null) {
                        return false;
                }
                for (ActivityManager.RunningAppProcessInfo info :
                        list) {
                        if (info.processName.equals(context.getPackageName())) {
//                                BACKGROUND=400 EMPTY=500 FOREGROUND=100
//                                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=20

                                if (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                                        return true;
                                }
                        }
                }
                return false;
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


        public static boolean isNetWorkAvailable() {
                return isNetWorkAvailable(BaseApplication.getInstance());
        }

        private static NetworkInfo getNetWorkInfo(Context context) {
                ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                return manager.getActiveNetworkInfo();
        }


        public static boolean isSupportSdcard() {
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        return false;
                } else {
                        return true;
                }
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

        public static boolean isAppAlive(Context context) {
                ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo appProcessInfo :
                        appProcessInfos
                        ) {
                        if (appProcessInfo.processName.equals(context.getPackageName())) {
                                LogUtil.e("当前的APP存活");
                                return true;
                        }
                }
                return false;
        }

        public static String getBaseUrl(String url) {
                return "";
        }

        /**
         * 获取圆形图片
         *
         * @param bitmap 源bitmap
         * @return 圆形bitmap
         */
        public static Bitmap getCircleBitmap(Bitmap bitmap) {
                Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(outBitmap);
                float roundSize;
                roundSize = Math.min(bitmap.getHeight(), bitmap.getWidth()) / 2f;
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                canvas.drawARGB(0, 0, 0, 0);
                paint.setColor(Color.WHITE);
                RectF rectF = new RectF(0, 0, roundSize * 2, roundSize * 2);
                Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                canvas.drawRoundRect(rectF, roundSize, roundSize, paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(bitmap, rect, new Rect(0, 0, Math.min(bitmap.getHeight(), bitmap.getWidth()), Math.min(bitmap.getHeight(), bitmap.getWidth())), paint);
                return outBitmap;
        }

        public static DisplayMetrics getScreenPix(Context context) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                return displayMetrics;
        }


        public static int getLayoutItemSize(Context context, int count, int marginSize) {
                DisplayMetrics displayMetrics = getScreenPix(context);
                int screenWidth = displayMetrics.widthPixels;
                int itemSize = (int) ((screenWidth - (marginSize * (count - 1))) / (float) count);
                return itemSize;
        }

        public static String list2string(List<String> likeUsers) {
                if (likeUsers == null || likeUsers.size() == 0) {
                        return null;
                }
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < likeUsers.size(); i++) {
                        if (i == 0) {
                                stringBuilder.append(likeUsers.get(i));
                        } else {
                                stringBuilder.append("," + likeUsers.get(i));
                        }
                }
                return stringBuilder.toString();
        }

        public static List<String> string2list(String string) {
                if (string != null && string.contains(",")) {
                        LogUtil.e("这里内容需要转义");
                        return new ArrayList<>(Arrays.asList(string.split(",")));
                } else if (string != null) {
                        LogUtil.e("只有一个字符串，没有分隔符");
                        List<String> list = new ArrayList<>();
                        list.add(string);
                        return list;
                } else {
                        LogUtil.e("string中为空");
                        return null;
                }
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

        public static List<String> commentMsg2List(String commentMessage) {
                if (commentMessage != null && commentMessage.contains("&")) {
                        return new ArrayList<>(Arrays.asList(commentMessage.split("&")));
                } else if (commentMessage != null) {
                        LogUtil.e("只有一个评论的情况下");
                        List<String> list = new ArrayList<>();
                        list.add(commentMessage);
                        return list;
                } else {
                        LogUtil.e("评论消息为空");
                        return null;
                }
        }

        public static String list2Comment(List<String> commentMsgList) {
                if (commentMsgList == null || commentMsgList.size() == 0) {
                        LogUtil.e("评论列表为空");
                        return null;
                }
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < commentMsgList.size(); i++) {
                        if (i == 0) {
                                stringBuilder.append(commentMsgList.get(i));
                        } else {
                                stringBuilder.append("&" + commentMsgList.get(i));
                        }
                }
                return stringBuilder.toString();
        }

        public static List<String> content2List(String commentMessage) {
//                需要转义
                if (commentMessage != null && commentMessage.contains("$")) {
                        LogUtil.e("这里内容需要转义");
                        return new ArrayList<>(Arrays.asList(commentMessage.split("\\$")));
                } else if (commentMessage != null) {
                        List<String> list = new ArrayList<>();
                        list.add(commentMessage);
                        return list;
                } else {
                        LogUtil.e("内容中为空");
                        return null;
                }
        }


        public static String list2Content(List<String> list) {
                if (list == null || list.size() == 0) {
                        LogUtil.e("内容列表为空");
                        return null;
                }
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < list.size(); i++) {
                        if (i == 0) {
                                stringBuilder.append(list.get(i));
                        } else {
                                stringBuilder.append("$" + list.get(i));
                        }
                }
                return stringBuilder.toString();
        }

        public static boolean isPhone(String content) {
//                先暂时测试
                return content.length() == 11;
        }

        public static String getDistance(double longitude, double latitude) {
                double localLongitude = UserManager.getInstance().getCurrentUser().getLocation().getLongitude();
                double localLatitude = UserManager.getInstance().getCurrentUser().getLocation().getLatitude();
                int distance = (int) AMapUtils.calculateLineDistance(new LatLng(localLatitude, localLongitude), new LatLng(latitude, longitude));
                return distance + "";
        }

        public static int getStatusBarHeight() {
                return PixelUtil.todp(55);
        }
}
