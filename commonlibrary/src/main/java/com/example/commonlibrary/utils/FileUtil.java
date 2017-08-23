package com.example.commonlibrary.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by COOTEK on 2017/7/31.
 */

public class FileUtil {


    public static File getDefaultCacheFile(Context context) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            File file = null;
//            file = context.getExternalCacheDir();//获取系统管理的sd卡缓存文件
//            if (file == null) {//如果获取的文件为空,就使用自己定义的缓存文件夹做缓存路径
//                file = new File(getCacheFilePath(context));
//                makeDirs(file);
//            }
//            return file;
//        } else {
//            return context.getCacheDir();
//        }
    }

    private static String getCacheFilePath(Context context) {
        String packageName = context.getPackageName();
        return "/mnt/sdcard/" + packageName;
    }


    /**
     * 创建未存在的文件夹
     *
     * @param file
     * @return
     */
    public static File makeDirs(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }


    public static String clipFileName(String path) {
        int index = path.lastIndexOf("/");
        if (index != -1) {
            String expendName = path.substring(index + 1);
            if (expendName.contains("?")) {
                return expendName.substring(0, expendName.indexOf("?"));
            }
        }
        return null;
    }


    public static boolean isFileExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static File getLocalFile(String path) {
        return new File(path);
    }

    public static void writeToFile(String path, String content) {
        try {
            CommonLogger.e("文件地址" + path);
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
            CommonLogger.e("写入成功");
        } catch (IOException e) {
            e.printStackTrace();
            CommonLogger.e("c" + e.getMessage());
        }
    }

    public static File newFile(String path) {
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
