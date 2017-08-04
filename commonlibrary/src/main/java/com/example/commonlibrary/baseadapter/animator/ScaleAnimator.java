package com.example.commonlibrary.baseadapter.animator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COOTEK on 2017/8/1.
 */

public class ScaleAnimator implements BaseAnimator {
    private static final float DEFAULT_SCALE = 0.5F;
    private float currentFrom;


    public ScaleAnimator() {
        this(DEFAULT_SCALE);
    }

    public ScaleAnimator(float currentFrom) {
        this.currentFrom = currentFrom;
    }

    @Override
    public List<Animator> getAnimators(View view) {
        List<Animator> list = new ArrayList<>();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", currentFrom, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", currentFrom, 1f);
        list.add(scaleX);
        list.add(scaleY);
        return list;
    }
}
