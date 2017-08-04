package com.example.commonlibrary.baseadapter.animator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COOTEK on 2017/8/1.
 */

public class LeftInAnimator implements BaseAnimator {

    @Override
    public List<Animator> getAnimators(View view) {
        List<Animator> list = new ArrayList<>();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationX", -view.getRootView().getWidth(), 0);
        list.add(objectAnimator);
        return list;
    }
}
