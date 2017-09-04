package com.example.commonlibrary.skin.attr;

import android.view.View;
import android.widget.TextView;

/**
 * Created by COOTEK on 2017/9/3.
 */

public class TextColorAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (isColorType() && view instanceof TextView) {
            applyTextColor(view);
        }
    }
}
