package com.example.commonlibrary.net;

import android.os.Environment;

import java.io.File;


/**
 * 下载配置
 */
public class DownloadConfig {

    // 下载目录
    private String mDownloadDir;
    private int mRetryTimes;
    // 下载目录文件保存
    private static final String STORAGE_KEY = "DownloadDir";


    private DownloadConfig() {
            mDownloadDir = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/common_library/download/";
            File dir = new File(mDownloadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            } else if (!dir.isDirectory()) {
                dir.delete();
                dir.mkdirs();
            }
            mRetryTimes = 10;
    }

    public String getDownloadDir() {
        return mDownloadDir;
    }

    public int getRetryTimes() {
        return mRetryTimes;
    }


    /**
     * 构建器
     */
    public static class Builder {
        private DownloadConfig config;

        public Builder() {
            config = new DownloadConfig();
        }

        public DownloadConfig build() {
            return config;
        }

        public Builder setDownloadDir(String downloadDir) {
            config.mDownloadDir = downloadDir;
            return this;
        }

        public Builder setRetryTimes(int retryTimes) {
            config.mRetryTimes = retryTimes;
            return this;
        }
    }

}
