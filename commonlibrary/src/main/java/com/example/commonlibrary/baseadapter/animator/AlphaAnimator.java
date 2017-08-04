package com.example.commonlibrary.baseadapter.animator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COOTEK on 2017/8/1.
 */

public class AlphaAnimator implements BaseAnimator {
    private static final float DEFAULT_ALPHA = 0.5F;
    private float currentValue;

    public AlphaAnimator() {
        this(DEFAULT_ALPHA);
    }

    public AlphaAnimator(float currentValue) {
        this.currentValue = currentValue;
    }

    @Override
    public List<Animator> getAnimators(View view) {
        List<Animator> list = new ArrayList<>();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", currentValue, 1f);
        list.add(objectAnimator);
        return list;
    }
}
