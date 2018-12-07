package com.example.commonlibrary.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * 项目名称:    zhuayu_android
 * 创建人:      陈锦军
 * 创建时间:    2018/10/15     21:09
 */
public class TimeUtil {

    /**
     * 将毫秒数格式化为"##:##"的时间
     *
     * @param milliseconds 毫秒数
     * @return ##:##
     */
    public static String formatTime(long milliseconds) {
        if (milliseconds <= 0 || milliseconds >= 24 * 60 * 60 * 1000) {
            return "00:00";
        }
        long totalSeconds = milliseconds / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        StringBuilder stringBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    public static String getTime(long time, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(time);
    }


    public static String getTime(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }


    public static Disposable countDown(int time, Consumer<Long> consumer) {
        return countDown(time, consumer, null);
    }


    public static Disposable countDown(long time, Consumer<Long> consumer, Consumer<Long> complete) {
        return Flowable.intervalRange(0, time + 1, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterNext(aLong -> {
                    consumer.accept(aLong);
                    if (complete != null && time - aLong == 0) {
                        complete.accept(aLong);
                    }
                }).subscribe();
    }


    public static String getCurrentTime(String pattern) {
        return new SimpleDateFormat(pattern).format(new Date(System.currentTimeMillis()));
    }


    public static String getDiffTime(long diff) {
        CommonLogger.e("diff:" + diff);
        Long hour = (diff / (60 * 60 * 1000));    //以小时为单位取整
        Long minute = (diff % (1000 * 60 * 60)) / (1000 * 60);
        Long second = (diff % (1000 * 60)) / 1000;
        StringBuilder stringBuilder = new StringBuilder();
        if (hour < 10) {
            stringBuilder.append(0);
        }
        stringBuilder.append(hour).append(":");
        if (minute < 10) {
            stringBuilder.append(0);
        }
        stringBuilder.append(minute).append(":");
        if (second < 10) {
            stringBuilder.append(0);
        }
        stringBuilder.append(second);
        return stringBuilder.toString();
    }


    public static String getLeaveTime(long time) {
        CommonLogger.e("diff:" + time);
        Long day = time / (60 * 60 * 1000 * 24);
        Long hour = (time % (60 * 60 * 1000 * 24)) / (60 * 60 * 1000);    //以小时为单位取整
        Long minute = (time % (1000 * 60 * 60)) / (1000 * 60);
        Long second = (time % (1000 * 60)) / 1000;
        StringBuilder stringBuilder = new StringBuilder();
        if (day > 0) {
            stringBuilder.append(day).append("天");
        }
        if (hour > 0) {
            stringBuilder.append(hour).append("小时");
        }
        if (minute > 0) {
            stringBuilder.append(minute).append("分");
        }

        if (stringBuilder.length() == 0) {
            return "已过期";
        } else {
            return stringBuilder.toString();
        }
    }
}
