package com.example.commonlibrary.skin.attr;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.commonlibrary.R;
import com.example.commonlibrary.utils.CommonLogger;

import java.util.List;

/**
 * Created by COOTEK on 2017/9/4.
 */

public class SkinItem {
    private View view;
    private List<SkinAttr> skinAttrs;


    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public List<SkinAttr> getSkinAttrs() {
        return skinAttrs;
    }

    public void setSkinAttrs(List<SkinAttr> skinAttrs) {
        this.skinAttrs = skinAttrs;
    }


    public void apply() {
        if (view != null) {
            SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("theme", Context.MODE_PRIVATE);
            boolean isTheme = sharedPreferences.getBoolean("isTheme", false);
            boolean isNight = sharedPreferences.getBoolean("isNight", false);
            int value = sharedPreferences.getInt("theme", Color.BLUE);
            for (SkinAttr skinAttr :
                    skinAttrs) {
                if (isTheme) {
                    updateBg(skinAttr, view);
                    updateTextColor(skinAttr, view);
                } else {
                    skinAttr.apply(view);
                }
            }
        }
    }

    private void updateTextColor(SkinAttr skinAttr, View view) {
        TypedValue typedValue = new TypedValue();
        if (skinAttr.getResName().endsWith("text_main")) {
            CommonLogger.e("text_main");
            view.getContext().getTheme().resolveAttribute(R.attr.custom_attr_text_main, typedValue, true);
            view.setBackgroundColor(typedValue.resourceId);
            ((TextView) view).setTextColor(typedValue.resourceId);
        }
    }

    private void updateBg(SkinAttr skinAttr, View view) {
        if (view instanceof SeekBar) {
            return;
        }
        TypedValue typedValue = new TypedValue();
        if (skinAttr.getResName().endsWith("app_bg")) {
            CommonLogger.e("app_bg");
            view.getContext().getTheme().resolveAttribute(R.attr.custom_attr_app_bg, typedValue, true);
            view.setBackgroundColor(typedValue.resourceId);
        } else if (skinAttr.getResName().endsWith("content_bg")) {
            CommonLogger.e("content_bg");
            view.getContext().getTheme().resolveAttribute(R.attr.custom_attr_app_content_bg, typedValue, true);
            view.setBackgroundColor(typedValue.resourceId);
        } else if (skinAttr.getResName().endsWith("title_bg")) {
            CommonLogger.e("title_bg");
            view.getContext().getTheme().resolveAttribute(R.attr.custom_attr_app_title_bg, typedValue, true);
            view.setBackgroundColor(typedValue.resourceId);
        } else if (skinAttr.getResName().endsWith("sb_thumb_bg")) {
            view.getContext().getTheme().resolveAttribute(R.attr.custom_attr_sb_thumb_bg, typedValue, true);
            view.setBackgroundColor(typedValue.resourceId);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null && obj instanceof SkinItem && ((SkinItem) obj).getView().equals(getView()));
    }
}
