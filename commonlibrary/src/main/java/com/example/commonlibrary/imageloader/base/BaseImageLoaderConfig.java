package com.example.commonlibrary.imageloader.base;

import android.widget.ImageView;

/**
 * Created by COOTEK on 2017/7/31.
 */

public class BaseImageLoaderConfig {
    private ImageView imageView;
    private String url;
    private int errorResId;
    private int placeHolderResId;


    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getErrorResId() {
        return errorResId;
    }

    public void setErrorResId(int errorResId) {
        this.errorResId = errorResId;
    }

    public int getPlaceHolderResId() {
        return placeHolderResId;
    }

    public void setPlaceHolderResId(int placeHolderResId) {
        this.placeHolderResId = placeHolderResId;
    }
}
