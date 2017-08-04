package com.example.commonlibrary.utils;

import java.net.FileNameMap;
import java.net.URLConnection;

import okhttp3.MediaType;

/**
 * Created by COOTEK on 2017/8/4.
 */

public class NetUtil {


    /**
     * 根据文件名获取MIME类型
     */
    public static MediaType guessMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        fileName = fileName.replace("#", "");   //解决文件名中含有#号异常的问题
        String contentType = fileNameMap.getContentTypeFor(fileName);
        if (contentType == null) {
            return MediaType.parse("application/octet-stream");
        }
        return MediaType.parse(contentType);
    }
}
