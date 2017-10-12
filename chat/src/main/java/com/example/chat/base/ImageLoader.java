package com.example.chat.base;

import android.content.Context;
import android.widget.ImageView;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/29      13:15
 * QQ:             1981367757
 */

public interface ImageLoader {
         void displayImage(Context context, String path, ImageView imageView, int width, int height);
}
