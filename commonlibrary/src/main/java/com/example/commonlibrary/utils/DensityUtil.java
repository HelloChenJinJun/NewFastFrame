package com.example.commonlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.example.commonlibrary.BaseApplication;

public class DensityUtil {

    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int dip2px(Context context, float dpVale) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpVale * scale + 0.5f);
    }


    public static int dip2sp(Context context, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }




    public static int toDp(int i) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,i, BaseApplication.getInstance().getResources().getDisplayMetrics());
    }





    public static int[] getViewWidthAndHeight(View contentView) {
        int[] result = new int[2];
        if (contentView.getMeasuredHeight() == 0 || contentView.getMeasuredWidth() == 0) {
            contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            // 计算contentView的高宽
            int windowHeight = contentView.getMeasuredHeight();
            int windowWidth = contentView.getMeasuredWidth();
            result[0] = windowWidth;
            result[1] = windowHeight;
        }
        return result;
    }
}
