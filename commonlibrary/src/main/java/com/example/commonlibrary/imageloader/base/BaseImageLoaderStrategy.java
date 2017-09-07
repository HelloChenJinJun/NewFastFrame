package com.example.commonlibrary.imageloader.base;

import android.content.Context;

/**
 * Created by COOTEK on 2017/7/31.
 */

public interface BaseImageLoaderStrategy<T extends BaseImageLoaderConfig> {
    public void loadImage(Context context, T config);
}
