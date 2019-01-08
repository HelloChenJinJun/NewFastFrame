package com.example.commonlibrary.customview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.example.commonlibrary.adaptScreen.ScreenAdaptManager;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2019/1/7     16:39
 */
public class CustomWebView extends WebView {
    public CustomWebView(Context context) {
        super(context);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void setOverScrollMode(int mode) {
        super.setOverScrollMode(mode);
        ScreenAdaptManager.getInstance().resetScreen((Activity) getContext());
    }

}
