package com.example.commonlibrary.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/21     17:27
 * QQ:         1981367757
 */

public class CommonUtil {
    public static String getTime(long time,String format){
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date(time));
    }

}
