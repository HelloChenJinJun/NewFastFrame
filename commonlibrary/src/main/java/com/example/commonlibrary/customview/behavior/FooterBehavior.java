package com.example.commonlibrary.customview.behavior;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/21     11:02
 */
public class FooterBehavior extends CoordinatorLayout.Behavior<View> {

    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    private int sinceDirectionChange;

    public FooterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //1.判断滑动的方向 我们需要垂直滑动


    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        if (dyConsumed > 0) {
            hide(child);
        } else if (dyConsumed < 0) {
            show(child);
        }
    }


    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }


    @Override
    public void onNestedScrollAccepted(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
    }

    private void hide(final View view) {
        if (view.getTranslationY() != view.getHeight()) {
            ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), view.getHeight()).setDuration(200).start();
        }
    }

    private void show(final View view) {
        if (view.getTranslationY() != 0) {
            ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), 0).setDuration(200).start();
        }
    }
}
