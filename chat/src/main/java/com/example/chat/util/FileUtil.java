package com.example.chat.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.example.chat.base.Constant;
import com.example.chat.manager.UserManager;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import okhttp3.ResponseBody;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/10/29      22:01
 * QQ:             1981367757
 */

public class FileUtil {
        /**
         * 删除指定目录的文件
         *
         * @param path 指定目录
         */
        public static void deleteFile(String path) {
                File file = new File(path);
                if (file.exists()) {
                        file.delete();
                }
        }

        /**
         * 根据路径创建文件
         *
         * @param path 路径
         * @return 文件
         */
        public static File newFile(String path) {
                try {
                        File file = new File(path);
                        if (!file.exists()) {
                                file.createNewFile();
                        }
                        return file;
                } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                }
        }

        /**
         * 创建用户所属的语音目录
         *
         * @param uid        用户ID
         * @param createTime 创建时间
         * @return 路径
         */
        public static String getUserVoiceFilePath(String uid, String createTime) {
                try {
                        File file = new File(Constant.VOICE_CACHE_DIR + CommonUtils.md5(UserManager.getInstance().getCurrentUserObjectId()) + File.separator + uid + File.separator + createTime + ".amr");
                        if (!file.exists()) {
                                file.createNewFile();
                        }
                        return file.getAbsolutePath();
                } catch (IOException e) {
                        e.printStackTrace();
                        return "";
                }
        }

        public static String getUserVoiceFileDir(String uid, String createTime) {
                File file = newFile(Constant.VOICE_CACHE_DIR + CommonUtils.md5(UserManager.getInstance().getCurrentUserObjectId()) + File.separator + uid
                        + File.separator + createTime + ".amr");
                if (file != null) {
                        return file.getParent();
                }
                return "";
        }


        /**
         * 新建目录文件
         *
         * @param imageCacheDir 路径
         * @return 目录文件
         */
        public static File newDir(String imageCacheDir) {
                try {
                        File dir = new File(imageCacheDir);
                        if (!dir.exists()) {
                                dir.mkdirs();
                        }
                        return dir;
                } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                }
        }


        public static Bitmap readBitmapFile(String localPath) {
                File file = new File(localPath);
                if (!file.exists()) {
                        return null;
                } else {
                        return BitmapFactory.decodeFile(localPath);
                }
        }

        public static boolean isExistFileLocalPath(String uid, String createTime) {
                File file = new File(Constant.VOICE_CACHE_DIR + CommonUtils.md5(UserManager.getInstance().getCurrentUserObjectId()) + File.separator + uid + File.separator + createTime + ".amr");
                return file.exists();
        }

        /**
         * 把responseBody的字节读入path文件中
         *
         * @param responseBody
         * @param path
         * @param readLength
         * @param totalLength
         */
        public static void writeToCache(ResponseBody responseBody, String path, long readLength, long totalLength) {
                try {
                        File file = newFile(path);
                        long writeLength;
                        if (totalLength == 0) {
                                writeLength = responseBody.contentLength();
                        } else {
                                writeLength = totalLength;
                        }
                        FileChannel fileChannel;
                        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
                        fileChannel = randomAccessFile.getChannel();
                        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, readLength, writeLength);
                        byte[] buffer = new byte[1024 * 8];
                        int length = 0;
                        while ((length = responseBody.byteStream().read(buffer)) != -1) {
                                mappedByteBuffer.put(buffer, 0, length);
                        }
                        responseBody.byteStream().close();
                        if (fileChannel != null) {
                                fileChannel.close();
                        }
                        if (randomAccessFile != null) {
                                randomAccessFile.close();
                        }
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        public static boolean isExistSDCard() {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        return true;
                }
                return false;
        }
}
