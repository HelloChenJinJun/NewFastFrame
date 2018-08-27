package com.example.commonlibrary.imageloader.glide;

import android.content.Context;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.commonlibrary.imageloader.base.BaseImageLoaderStrategy;

/**
 * Created by COOTEK on 2017/7/31.
 */

public class GlideImageLoaderStrategy implements BaseImageLoaderStrategy<GlideImageLoaderConfig> {
    @Override
    public void loadImage(Context context, GlideImageLoaderConfig config) {
        if (config == null || context == null) {
            return;
        }
        DrawableRequestBuilder drawableRequestBuilder;
        drawableRequestBuilder = Glide.with(context).load(config.getUrl())
                .crossFade().centerCrop();
        switch (config.getCacheStrategy()) {
            case GlideImageLoaderConfig.CACHE_ALL:
                drawableRequestBuilder.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
            case GlideImageLoaderConfig.CACHE_NONE:
                drawableRequestBuilder.diskCacheStrategy(DiskCacheStrategy.NONE);
                break;
            case GlideImageLoaderConfig.CACHE_SOURCE:
                drawableRequestBuilder.diskCacheStrategy(DiskCacheStrategy.SOURCE);
                break;
            case GlideImageLoaderConfig.CACHE_RESULT:
                drawableRequestBuilder.diskCacheStrategy(DiskCacheStrategy.RESULT);
                break;
                default:
                    break;
        }
        if (config.isCenterInside()) {
            drawableRequestBuilder.fitCenter();
        }else {
            drawableRequestBuilder.centerCrop();
        }
        if (config.getBitmapTransformation() != null) {
            drawableRequestBuilder.bitmapTransform(config.getBitmapTransformation());
        }
        if (config.getErrorResId() != 0) {
            drawableRequestBuilder.error(config.getErrorResId());
        }
        if (config.getPlaceHolderResId() != 0) {
            drawableRequestBuilder.placeholder(config.getPlaceHolderResId());
        }
        drawableRequestBuilder.into(config.getImageView());
    }
}
