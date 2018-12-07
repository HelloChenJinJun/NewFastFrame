package com.example.commonlibrary.net.okhttpconfig;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.LibraryGlideModule;
import com.example.commonlibrary.BaseApplication;

import java.io.InputStream;

import androidx.annotation.NonNull;

/**
 * Created by COOTEK on 2017/8/17.
 */

@GlideModule
public class GlideConfig extends LibraryGlideModule {

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(BaseApplication.getAppComponent().getOkHttpClient());
        registry.replace(GlideUrl.class, InputStream.class, factory);


    }
}
