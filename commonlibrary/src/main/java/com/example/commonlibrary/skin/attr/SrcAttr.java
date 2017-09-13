package com.example.commonlibrary.skin.attr;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by COOTEK on 2017/9/3.
 */

public class SrcAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (isDrawableType() && view instanceof ImageView) {
            applyImageDrawable(view);
        }
    }
}
