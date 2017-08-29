package com.example.cootek.newfastframe.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.R;

import java.util.ArrayList;
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
        final PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f);
        ValueAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(alpha, scaleX, scaleY)
                .setDuration(layoutTransition.getDuration(LayoutTransition.DISAPPEARING));
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animation.getAnimatedFraction() == 1) {
                    deal();
                }
            }
        });
        return objectAnimator;
    }


    private boolean flag = true;
    private int num = 0;

    private synchronized void deal() {
        num++;
        CommonLogger.e("num" + num);
        if (position >= 5) {
            if (flag) {
                flag = false;
                next();
            } else {
                flag = true;
                remove();
            }
        }
    }


    private List<View> viewList = new ArrayList<>();

    private void next() {
        CommonLogger.e("这里添加");
        addView(getItemView(position));
        position++;
    }

    private Animator getAppearingAnimator() {
        final PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
        ValueAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(alpha, scaleX, scaleY)
                .setDuration(layoutTransition.getDuration(LayoutTransition.APPEARING) + 1);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animation.getAnimatedFraction() == 1) {
                    deal();
                }
            }
        });
        return objectAnimator;
    }

    private void remove() {
        CommonLogger.e("这里移除1");
        if (position > 5) {
            removeView(viewList.get(0));
            CommonLogger.e("这里真正移除");
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


    private volatile int position = 0;

    private View getItemView(int position) {
        return updateView(LayoutInflater.from(getContext()).inflate(R.layout.view_comment_layout_item, null), position);
    }

    private View updateView(View itemView, int position) {
        if (position >= 0 && position < data.size()) {
            ((TextView) itemView.findViewById(R.id.tv_view_comment_layout_item_content)).setText(data.get(position));
            itemView.setOnClickListener(this);
            viewList.add(itemView);
            return itemView;
        } else {
            return null;
        }
    }


    @Override
    public void run() {
        if (position < 5) {
            next();
        } else {
            removeView(viewList.get(0));
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
