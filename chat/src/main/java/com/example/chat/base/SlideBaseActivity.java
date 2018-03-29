package com.example.chat.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.chat.R;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.komi.slider.ISlider;
import com.komi.slider.SliderConfig;
import com.komi.slider.SliderUtils;
import com.komi.slider.position.SliderPosition;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/26      11:58
 * QQ:             1981367757
 */

public abstract class SlideBaseActivity<T,P extends BasePresenter> extends MainBaseActivity<T,P> {


        private ISlider iSlider;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);


        }


        @Override
        protected void onPostCreate(@Nullable Bundle savedInstanceState) {
                super.onPostCreate(savedInstanceState);
                SliderConfig mConfig = new SliderConfig.Builder()
                        .primaryColor(getResources().getColor(R.color.colorPrimary))
                        .secondaryColor(Color.TRANSPARENT)
                        .position(SliderPosition.LEFT)
                        .edge(true)
                        .build();
                iSlider = SliderUtils.attachActivity(this, mConfig);
        }

        @Override
        public void onBackPressed() {
                super.onBackPressed();
                iSlider.slideExit();
        }
}
