package com.example.commonlibrary.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Locale;

import okhttp3.MediaType;

/**
 * Created by COOTEK on 2017/7/31.
 */

public class FileUtil {


    public static File getDefaultCacheFile(Context context) {
        File file;
        if (isExistSDCard()) {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            if (!file.exists()) {
                file.mkdir();
            }
        }else {
            file=context.getCacheDir();
        }
        return file;

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
            }else {
                return expendName;
            }
        }
        return null;
    }




    /**
     * 读取 assets 文件
     * @param context
     * @param fileName
     * @return
     */
    public static String readData(Context context, String fileName) {
        InputStream inStream = null;
        String data = null;
        try {
            inStream = context.getAssets().open(fileName);     //打开assets目录中的文本文件
            byte[] bytes = new byte[inStream.available()];  //inStream.available()为文件中的总byte数
            inStream.read(bytes);
            inStream.close();
            data = new String(bytes, "utf-8");        //将bytes转为utf-8字符串
        } catch (IOException e) {
//            Logger.e(e.toString());
            e.printStackTrace();
        }
        return data;
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


    /**
     * 获取随机存取文件
     * @param path  文件路径
     * @param loadBytes 文件已下载大小
     * @param totalBytes    文件总大小
     * @return  文件
     * @throws IOException
     */
    public static RandomAccessFile getRandomAccessFile(String path, int loadBytes, int totalBytes) throws IOException {
        if (TextUtils.isEmpty(path)) {
            throw new RuntimeException("found invalid internal destination path, empty");
        }

        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            throw new RuntimeException(
                    formatString("found invalid internal destination path[%s]," +
                            " & path is directory[%B]", path, file.isDirectory()));
        }

        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException(formatString("create new file error %s",
                        file.getAbsolutePath()));
            }
        }

        RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
        if (totalBytes > 0) {
            final long breakpointBytes = accessFile.length();
            final long requiredSpaceBytes = totalBytes - breakpointBytes;

            final long freeSpaceBytes = getFreeSpaceBytes(path);

            if (freeSpaceBytes < requiredSpaceBytes) {
                accessFile.close();
                // throw a out of space exception.
                throw new RuntimeException(
                        formatString("The file is too large to store, breakpoint in bytes: " +
                                " %d, required space in bytes: %d, but free space in bytes: " +
                                "%d", breakpointBytes, requiredSpaceBytes, freeSpaceBytes));
            } else {
                // pre allocate.
                accessFile.setLength(totalBytes);
            }
        }

        if (loadBytes > 0) {
            accessFile.seek(loadBytes);
        }
        return accessFile;
    }

    /**
     * 格式化字符串
     * @param msg   格式数据
     * @param args  参数
     * @return  格式化字符串
     */
    public static String formatString(final String msg, Object... args) {
        return String.format(Locale.ENGLISH, msg, args);
    }

    /**
     * 获取空闲的空间大小
     * @param path  文件路径
     * @return  空间大小
     */
    public static long getFreeSpaceBytes(final String path) {
        long freeSpaceBytes;
        final StatFs statFs = new StatFs(path);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            freeSpaceBytes = statFs.getAvailableBytes();
        } else {
            //noinspection deprecation
            freeSpaceBytes = statFs.getAvailableBlocks() * (long) statFs.getBlockSize();
        }

        return freeSpaceBytes;
    }

    public static boolean isExistSDCard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

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
