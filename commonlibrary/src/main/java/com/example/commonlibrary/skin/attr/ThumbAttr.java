package com.example.commonlibrary.skin.attr;

import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;

import com.example.commonlibrary.skin.SkinManager;
import com.example.commonlibrary.utils.CommonLogger;

/**
 * Created by COOTEK on 2017/9/6.
 */

public class ThumbAttr extends SkinAttr {

    @Override
    public void apply(View view) {
        if (isDrawableType()) {
            if (view instanceof SeekBar) {
                ((SeekBar) view).setThumb(SkinManager.getInstance().getDrawable(getResId()));
            }
        }
    }
}
