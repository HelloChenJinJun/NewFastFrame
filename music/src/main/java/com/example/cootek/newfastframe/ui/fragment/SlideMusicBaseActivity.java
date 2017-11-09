package com.example.cootek.newfastframe.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.ui.MusicBaseActivity;
import com.komi.slider.ISlider;
import com.komi.slider.SliderConfig;
import com.komi.slider.SliderListener;
import com.komi.slider.SliderUtils;
import com.komi.slider.position.SliderPosition;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/11/8      15:16
 * QQ:             1981367757
 */

public abstract class SlideMusicBaseActivity<T, P extends BasePresenter> extends MusicBaseActivity<T,P> {


    protected ISlider iSlider;
    private GestureDetector mGestureDetector;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SliderConfig mConfig = new SliderConfig.Builder()
                .primaryColor(getResources().getColor(R.color.colorPrimary))
                .secondaryColor(Color.TRANSPARENT)
                .position(SliderPosition.LEFT)
                .edge(false)
                .build();
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//                                如果水平方向的偏移量大于竖直方向的偏移量，就消化该事件,
                return Math.abs(distanceX) > Math.abs(distanceY) || super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
        mConfig.setSliderListener(new SliderListener() {
            @Override
            public void onSlideStateChanged(int state) {

            }

            @Override
            public void onSlideChange(float percent) {

            }

            @Override
            public void onSlideOpened() {

            }

            @Override
            public void onSlideClosed() {

            }

            @Override
            public boolean customSlidable(MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
        iSlider = SliderUtils.attachActivity(this, mConfig);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        iSlider.slideExit();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {

        }
        return super.dispatchTouchEvent(ev);
    }
}
