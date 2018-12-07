package com.example.commonlibrary.skin.attr;

import android.os.Build;
import android.view.View;
import android.widget.RadioButton;

import com.example.commonlibrary.skin.SkinManager;

import androidx.annotation.RequiresApi;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/5/22     15:44
 */

public class DrawableTopAttr extends SkinAttr {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void apply(View view) {
        if (isDrawableType()&&view instanceof RadioButton) {
            ((RadioButton) view).setCompoundDrawablesRelativeWithIntrinsicBounds
                    (null, SkinManager.getInstance().getDrawable(getResId()), null, null);
        }
    }
}
