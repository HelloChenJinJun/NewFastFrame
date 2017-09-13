package com.example.commonlibrary.net.okhttpconfig;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.utils.CommonLogger;

import java.io.InputStream;

/**
 * Created by COOTEK on 2017/8/17.
 */

public class GlideConfig implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        CommonLogger.e("applyOptions");
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        CommonLogger.e("registerComponents");
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(BaseApplication.getAppComponent().getOkHttpClient()));
    }
}
