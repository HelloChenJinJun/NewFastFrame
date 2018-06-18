package com.example.chat.util;

import com.example.chat.base.Constant;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.utils.CommonLogger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/13      18:57
 * QQ:             1981367757
 */
public class TimeUtil {
    public static String getTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(time));
    }




    public static String getRecentTime(long time){
            long deltaTime=System.currentTimeMillis()-time;
            if (deltaTime<ONE_D){
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                return format.format(new Date(time));
            }else if (deltaTime<ONE_D*7){
                int num= (int) (deltaTime/(ONE_D));
                if (num == 1) {
                    return "昨天";
                }
            }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(time));
    }


    public static long getTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }








    public static String getTime(long time,String timeFormat){
        SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        return format.format(new Date(time));
    }



    public static long getTime(String time,String timeFormat){
        SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }


    public static Date getDateFormalFromString(String message) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(message);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public static String getServerFormatTime() {
        long deltaTime = BaseApplication.getAppComponent().getSharedPreferences()
                .getLong(Constant.DELTA_TIME, -1L);
        LogUtil.e("这里通过缓存的时间差值来计算出服务器上的时间");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long realServerTime = System.currentTimeMillis() - deltaTime;
        return simpleDateFormat.format(new Date(realServerTime));
    }

    public static String getRealTime(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long serverTime = simpleDateFormat.parse(time).getTime();
            return getRealTime(serverTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getRealTime(long serverTime) {
        LogUtil.e("服务器上的时间:" + serverTime);
        long deltaTime = BaseApplication.getAppComponent().getSharedPreferences()
                .getLong(Constant.DELTA_TIME, -1L);
        long realTime = serverTime + deltaTime;
        LogUtil.e("客户端的时间:" + realTime);
        LogUtil.e("现在的客户端的时间:" + System.currentTimeMillis());
        long currentDletaTime = System.currentTimeMillis() - realTime;
        LogUtil.e("差值:" + currentDletaTime);
        return getShareTime(Math.abs(currentDletaTime));
    }






    private static final Long ONE_M=1000 * 60 * 60 * 24*30L;
    private static final Long ONE_Y=1000 * 60 * 60 * 24*30* 12L;
    private static final Long ONE_D=1000*60*60*24L;
    private static final Long ONE_H=1000*60*60L;

    private static String getShareTime(long currentDletaTime) {
        String result;
        int time = (int) (currentDletaTime / (1000 * 60));
        LogUtil.e("差值分钟:" + time);
        if (time == 0) {
            result = "刚刚发表";
            return result;
        }
        if (time > 0 && time < 60) {
            result = time + "分钟前";
        } else {
            time = (int) (currentDletaTime / (1000 * 60 * 60));
            if (time > 0 && time < 24) {
                result = time + "小时前";
            } else {
                time = (int) (currentDletaTime / (1000 * 60 * 60 * 24));
                if (time == 1) {
                    result = "昨天";
                } else if (time == 2) {
                    result = "前天";
                } else if (time<29){
                    result = time + "天前";
                }else {
                    time=(int) (currentDletaTime /ONE_M);
                    if (time > 11) {
                        time= (int) (currentDletaTime/ONE_Y);
                        result = time + "年前";
                    }else {
                        result = time + "月前";
                    }
                }
            }
        }
        return result;
    }


    public static String getDateFormalFromString(int currentYear, int currentMonth, int currentDay) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(currentYear, currentMonth, currentDay);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(gregorianCalendar.getTime());
    }

    public static void getServerTime() {
        Bmob.getServerTime(new QueryListener<Long>() {
            @Override
            public void done(Long aLong, BmobException e) {
                if (e == null) {
                    long deltaTime = System.currentTimeMillis() - aLong * 1000L;
                    LogUtil.e("客户端与服务器端的时间差值 :" + deltaTime);
                    BaseApplication.getAppComponent().getSharedPreferences()
                            .edit().putLong(Constant.DELTA_TIME, deltaTime).apply();
                } else {
                    LogUtil.e("获取服务器上的时间失败" + e.toString());
                }
            }
        });
    }

    public static long getLongTime(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long serverTime = 0;
        try {
            serverTime = simpleDateFormat.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LogUtil.e("服务器上的时间:" + serverTime);
        long deltaTime = BaseApplication.getAppComponent().getSharedPreferences()
                .getLong(Constant.DELTA_TIME, -1L);
        return serverTime + deltaTime;
    }

    public static long localToServerTime(long time){
        long deltaTime = BaseApplication.getAppComponent().getSharedPreferences()
                .getLong(Constant.DELTA_TIME, 0L);
        return time-deltaTime;
    }

    public static long severToLocalTime(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long serverTime = 0;
        try {
            serverTime = simpleDateFormat.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        CommonLogger.e("服务器上的时间:" + serverTime);
        long deltaTime = BaseApplication.getAppComponent().getSharedPreferences()
                .getLong(Constant.DELTA_TIME, 0L);
        return serverTime + deltaTime;
    }

    public static String getCurrentTime() {
        return getTime(System.currentTimeMillis(),"yyyy-MM-dd");
    }
}
