package com.example.commonlibrary.imageloader.glide;

import android.graphics.Bitmap;
import android.view.View;

import com.bumptech.glide.load.Transformation;
import com.example.commonlibrary.imageloader.base.BaseImageLoaderConfig;

/**
 * Created by COOTEK on 2017/7/31.
 */

public class GlideImageLoaderConfig extends BaseImageLoaderConfig {
    public static final int CACHE_ALL = 0;
    public static final int CACHE_NONE = 1;
    public static final int CACHE_SOURCE = 2;
    public static final int CACHE_RESULT = 3;


    private Transformation<Bitmap> bitmapTransformation;
    private int cacheStrategy;
    private boolean isClearMemoryCache;
    private boolean isClearDiskCache;
    private boolean isCenterInside;
    private boolean asGif;

    private GlideImageLoaderConfig(Builder builder) {
        setView(builder.imageView);
        setHeight(builder.height);
        setWidth(builder.width);
        asGif = builder.asGif;
        setUrl(builder.url);
        setErrorResId(builder.errorResId);
        bitmapTransformation = builder.bitmapTransformation;
        isCenterInside = builder.isCenterInside;
        setPlaceHolderResId(builder.placeHolderResId);
        cacheStrategy = builder.cacheStrategy;
        isClearMemoryCache = builder.isClearMemeryCache;
        isClearDiskCache = builder.isClearDiskCache;
    }


    public boolean asGif() {
        return asGif;
    }


    public Transformation<Bitmap> getBitmapTransformation() {
        return bitmapTransformation;
    }

    public int getCacheStrategy() {
        return cacheStrategy;
    }

    public boolean isClearDiskCache() {
        return isClearDiskCache;
    }

    public boolean isCenterInside() {
        return isCenterInside;
    }


    public boolean isClearMemoryCache() {
        return isClearMemoryCache;
    }

    public static Builder newBuild() {
        return new Builder();
    }

    public static final class Builder {
        private View imageView;
        private String url;
        private int errorResId;
        private Transformation<Bitmap> bitmapTransformation;
        private int placeHolderResId;
        private int cacheStrategy;
        private boolean isClearMemeryCache;
        private boolean isClearDiskCache;
        private boolean isCenterInside = false;
        private boolean asGif = false;
        private int width = 0;
        private int height = 0;


        public Builder() {
        }

        public Builder asGif() {
            asGif = true;
            return this;
        }


        public Builder override(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }


        public Builder imageView(View val) {
            imageView = val;
            return this;
        }


        public Builder centerInside() {
            isCenterInside = true;
            return this;
        }

        public Builder url(String val) {
            url = val;
            return this;
        }

        public Builder errorResId(int val) {
            errorResId = val;
            return this;
        }

        public Builder bitmapTransformation(Transformation<Bitmap> val) {
            bitmapTransformation = val;
            return this;
        }

        public Builder placeHolderResId(int val) {
            placeHolderResId = val;
            return this;
        }

        public Builder cacheStrategy(int val) {
            cacheStrategy = val;
            return this;
        }

        public Builder isClearMemeryCache(boolean val) {
            isClearMemeryCache = val;
            return this;
        }

        public Builder isClearDiskCache(boolean val) {
            isClearDiskCache = val;
            return this;
        }

        public GlideImageLoaderConfig build() {
            return new GlideImageLoaderConfig(this);
        }
    }
}
