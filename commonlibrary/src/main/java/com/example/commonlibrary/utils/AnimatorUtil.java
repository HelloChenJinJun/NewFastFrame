package com.example.commonlibrary.utils;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/29     14:40
 */
public class AnimatorUtil {
    public static final LinearOutSlowInInterpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new LinearOutSlowInInterpolator();

    // 显示view
    public static void scaleShow(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        view.setVisibility(View.VISIBLE);
        ViewCompat.animate(view)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1.0f)
                .setDuration(800)
                .setListener(viewPropertyAnimatorListener)
                .setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
                .start();
    }

    // 隐藏view
    public static void scaleHide(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        ViewCompat.animate(view)
                .scaleX(0.0f)
                .scaleY(0.0f)
                .alpha(0.0f)
                .setDuration(800)
                .setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
                .setListener(viewPropertyAnimatorListener)
                .start();
    }

    public static void tanslation(final View view,float  start,float end){
        final ValueAnimator animator=ValueAnimator.ofFloat(start,end);
        view.setVisibility(View.VISIBLE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Float value = (Float) animator.getAnimatedValue();
                view.setTranslationY(value);
            }
        });
        animator.setDuration(200);
        animator.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
        animator.start();
    }

    public static void showHeight(final View view,float  start,float end){
        final ValueAnimator animator=ValueAnimator.ofFloat(start,end);
        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float) animator.getAnimatedValue();
                layoutParams.height=(int) value;
                view.setLayoutParams(layoutParams);

            }
        });
        animator.setDuration(500);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }

    public static void show(final View view, int  start, int end){
        final int height = view.getHeight();
        final ValueAnimator animator=ValueAnimator.ofInt(start,end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) animator.getAnimatedValue();
                view.setTop(value);
                view.setBottom(value+height);
            }
        });
        view.setVisibility(View.VISIBLE);
        animator.setDuration(200);
        animator.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
        animator.start();
    }
}
