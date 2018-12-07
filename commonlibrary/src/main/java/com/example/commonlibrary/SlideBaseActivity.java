package com.example.commonlibrary;

import android.graphics.Color;
import android.os.Bundle;

import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.komi.slider.ISlider;
import com.komi.slider.SliderConfig;
import com.komi.slider.SliderUtils;
import com.komi.slider.position.SliderPosition;

import androidx.annotation.Nullable;

/**
 * 项目名称:    zhuayu_android
 * 创建人:      陈锦军
 * 创建时间:    2018/11/13     14:42
 */
public abstract class SlideBaseActivity<T, P extends BasePresenter> extends BaseActivity<T, P> {




    protected ISlider iSlider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }




//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }





    protected boolean needSlide(){
        return true;
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (needSlide()) {
            SliderConfig mConfig = new SliderConfig.Builder()
                    .primaryColor(Color.TRANSPARENT)
                    .secondaryColor(Color.TRANSPARENT)
                    .position(SliderPosition.LEFT)
                    .edge(true)
                    .build();
            iSlider = SliderUtils.attachActivity(this, mConfig);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (iSlider!=null) {
            iSlider.slideExit();
        }
    }
}
