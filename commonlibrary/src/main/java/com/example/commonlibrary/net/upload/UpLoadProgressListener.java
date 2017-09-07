package com.example.commonlibrary.net.upload;

/**
 * Created by COOTEK on 2017/8/4.
 */

public interface UpLoadProgressListener {

    public void onUpdate(long hasUpLoadSize, long totalUpLoadSize);

}
