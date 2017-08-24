package com.example.cootek.newfastframe;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.commonlibrary.utils.CommonLogger;

import java.util.List;

/**
 * Created by COOTEK on 2017/8/24.
 */

public class CommentLayout extends LinearLayout implements Runnable, View.OnClickListener {
    private LayoutTransition layoutTransition;

    public CommentLayout(Context context) {
        this(context, null);
    }

    public CommentLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        layoutTransition = new LayoutTransition();
        layoutTransition.setAnimator(LayoutTransition.APPEARING, getAppearingAnimator());
        layoutTransition.setAnimator(LayoutTransition.DISAPPEARING, getDisAppearingAnimator());
        setLayoutTransition(layoutTransition);
    }


    private Animator getDisAppearingAnimator() {
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, alpha, scaleX, scaleY)
                .setDuration(layoutTransition.getDuration(LayoutTransition.DISAPPEARING));
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                CommonLogger.e("删除到尾端了吗？");
                next();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return objectAnimator;
    }

    private void next() {
        addView(getItemView(position));
        position++;
    }

    private Animator getAppearingAnimator() {
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, alpha, scaleX, scaleY)
                .setDuration(layoutTransition.getDuration(LayoutTransition.APPEARING));
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                CommonLogger.e("添加到尾端了吗？");
                remove();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return objectAnimator;
    }

    private void remove() {
        if (getChildCount() > 0 && position > 5) {
            removeViewAt(0);
        }
    }

    private List<String> data;

    public void setData(List<String> data) {
        if (data != null) {
            this.data = data;
            position = 0;
        }
    }


    public void start() {
        if (data != null && data.size() > 0) {
            setVisibility(VISIBLE);
            post(this);
        }
    }


    private int position = 0;

    private View getItemView(int position) {
        return updateView(LayoutInflater.from(getContext()).inflate(R.layout.view_comment_layout_item, null), position);
    }

    private View updateView(View itemView, int position) {
        if (position >= 0 && position < data.size()) {
            ((TextView) itemView.findViewById(R.id.tv_view_comment_layout_item_content)).setText(data.get(position));
            itemView.setOnClickListener(this);
            return itemView;
        } else {
            return null;
        }
    }


    @Override
    public void run() {
        if (position < 5) {
            addView(getItemView(position));
            position++;
        } else {
            removeViewAt(0);
            removeCallbacks(this);
            return;
        }
        postDelayed(this, 1000);
    }

    public void stop() {
        removeCallbacks(this);
        setVisibility(GONE);
    }

    @Override
    public void onClick(View v) {
        stop();
    }
}
