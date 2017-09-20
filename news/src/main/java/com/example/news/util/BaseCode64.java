package com.example.news.util;

import static java.lang.Double.isNaN;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/19      20:27
 * QQ:             1981367757
 */

public class BaseCode64 {


//    041633
//    MDQxNjMz
//    NDg1MjQ5NTQ1MTUx



    private static   final  String _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    public static String encode(String input){
        String output = "";
        int chr1, chr2, chr3, enc1, enc2, enc3, enc4;
        int i = 0;
        input = uft8Encode(input);
        while (i < input.length()) {
            chr1= getCharCode(input.charAt(i++));
            chr2 = input.charAt(i++);
            chr3 = input.charAt(i++);
            enc1 = chr1 >> 2;
            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
            enc4 = chr3 & 63;
            if (isNaN(chr2)) {
                enc3 = enc4 = 64;
            } else if (isNaN(chr3)) {
                enc4 = 64;
            }
            output = output +
                    _keyStr.charAt(enc1) + _keyStr.charAt(enc2) +
                    _keyStr.charAt(enc3) + _keyStr.charAt(enc4);
        }
        return output;
    }

    private static int getCharCode(char c) {
        return ((int) c);
    }

    private static String uft8Encode(String input) {
        input = input.replace("/\r\n/g", "\n");
        String utftext = "";
        for (int n = 0; n < input.length(); n++) {
            int c = input.charAt(n);
            if (c < 128) {
                utftext += String.valueOf(c);
            } else if ((c > 127) && (c < 2048)) {
                utftext += String.valueOf((c >> 6) | 192);
                utftext += String.valueOf((c & 63) | 128);
            } else {
                utftext += String.valueOf((c >> 12) | 224);
                utftext += String.valueOf(((c >> 6) & 63) | 128);
                utftext += String.valueOf((c & 63) | 128);
            }

        }
        return utftext;
    }
}
