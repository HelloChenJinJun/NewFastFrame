package com.example.commonlibrary.imageloader;

import android.content.Context;

import com.example.commonlibrary.imageloader.base.BaseImageLoaderConfig;
import com.example.commonlibrary.imageloader.base.BaseImageLoaderStrategy;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by COOTEK on 2017/7/31.
 */
@Singleton
public class ImageLoader {
    private BaseImageLoaderStrategy baseImageLoaderStrategy;

    @Inject
    public ImageLoader(BaseImageLoaderStrategy baseImageLoaderStrategy) {
        this.baseImageLoaderStrategy = baseImageLoaderStrategy;
    }

    public void setBaseImageLoaderStrategy(BaseImageLoaderStrategy baseImageLoaderStrategy) {
        this.baseImageLoaderStrategy = baseImageLoaderStrategy;
    }

    public <T extends BaseImageLoaderConfig> void loadImage(Context context, T config) {
        baseImageLoaderStrategy.loadImage(context, config);
    }


}
