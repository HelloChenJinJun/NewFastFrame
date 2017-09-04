package com.example.commonlibrary.skin.attr;

import android.view.View;

import com.example.commonlibrary.skin.SkinAttr;

/**
 * Created by COOTEK on 2017/9/3.
 */

public class BackgroundAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (isColorType()) {
            applyBackgroundColor(view);
        } else if (isDrawableType()) {
            applyBackgroundDrawable(view);
        }
    }
}
