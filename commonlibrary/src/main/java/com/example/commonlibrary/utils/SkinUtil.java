package com.example.commonlibrary.utils;

import android.content.Context;

import com.example.commonlibrary.BaseApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by COOTEK on 2017/9/3.
 */

public class SkinUtil {
    private static final String SKIN_DIR_NAME = "skin";
    public static final String NAME_PLACE = "http://schemas.android.com/apk/res/android";


    public static String getSkinFilePath(String skinName) {
        return getSkinDir() + File.separator + skinName;
    }

    public static void setUpSkinFile() {
        try {
            String[] skinFiles = BaseApplication.getInstance().getAssets().list(SKIN_DIR_NAME);
            if (skinFiles != null && skinFiles.length > 0) {
                for (String fileName : skinFiles) {
                    File file = new File(getSkinDir(), fileName);
                    if (!file.exists())
                        copySkinAssetsToDir(fileName, getSkinDir());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String copySkinAssetsToDir(String name, String toDir) {
        String toFile = toDir + File.separator + name;
        CommonLogger.e("将要写入的文件" + toFile);
        try {
            InputStream is = BaseApplication.getInstance().getAssets().open(SKIN_DIR_NAME + File.separator + name);
            File fileDir = new File(toDir);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            OutputStream os = new FileOutputStream(toFile);
            int byteCount;
            byte[] bytes = new byte[1024];

            while ((byteCount = is.read(bytes)) != -1) {
                os.write(bytes, 0, byteCount);
            }
            os.close();
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
            CommonLogger.e("写入出错啦啦啦" + e.getMessage());
        }
        return toFile;
    }

    private static String getSkinDir() {
        File skinDir = new File(BaseApplication.getInstance().getCacheDir(), SKIN_DIR_NAME);
        if (!skinDir.exists()) {
            skinDir.mkdirs();
        }
        return skinDir.getAbsolutePath();

    }
}
