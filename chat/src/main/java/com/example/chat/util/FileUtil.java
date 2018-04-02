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
        public static String getUserVoiceFilePath(String uid, long createTime) {
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

        public static boolean isExistFileLocalPath(String uid, long createTime) {
                File file = new File(Constant.VOICE_CACHE_DIR + CommonUtils.md5(UserManager.getInstance().getCurrentUserObjectId()) + File.separator + uid + File.separator + createTime + ".amr");
                return file.exists();
        }



        public static boolean isExistSDCard() {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        return true;
                }
                return false;
        }

}
