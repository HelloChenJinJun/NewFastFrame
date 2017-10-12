package com.example.chat.base;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.chat.R;

import java.io.File;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/29      13:14
 * QQ:             1981367757
 */

class GlideImageLoader implements ImageLoader {
        @Override
        public void displayImage(Context context, String path, ImageView imageView, int width, int height) {
                Glide.with(context).load(Uri.fromFile(new File(path))).error(R.mipmap.default_image).placeholder(R.drawable.location_default).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        }
}
