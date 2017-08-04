package com.example.commonlibrary.baseadapter.animator;

import android.animation.Animator;
import android.view.View;

import java.util.List;

/**
 * Created by COOTEK on 2017/8/1.
 */

public interface BaseAnimator {
    public List<Animator> getAnimators(View view);
}
