package com.example.cootek.newfastframe.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/11/7      16:34
 * QQ:             1981367757
 */

public class PinYinUtil {
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



    public static String getPinYin(String name){
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
}
