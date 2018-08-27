package com.example.commonlibrary.skin.attr;

import android.view.View;
import android.widget.SeekBar;

import com.example.commonlibrary.utils.CommonLogger;

/**
 * Created by COOTEK on 2017/9/3.
 */

public class BackgroundAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof SeekBar) {
            return;
        }
        if (isColorType()) {
            applyBackgroundColor(view);
        } else if (isDrawableType()) {
            applyBackgroundDrawable(view);
        }
    }
}
