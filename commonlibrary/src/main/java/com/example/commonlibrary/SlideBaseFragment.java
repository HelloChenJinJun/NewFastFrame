package com.example.commonlibrary;

import android.os.Bundle;

import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.komi.slider.ISlider;

import androidx.annotation.Nullable;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/5     15:13
 */
public abstract class SlideBaseFragment<T, P extends BasePresenter> extends BaseFragment<T, P> {

    protected ISlider iSlider;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    //    @Override
    //    protected void attachBaseContext(Context newBase) {
    //        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    //    }


    protected boolean needSlide() {
        return true;
    }


//    @Override
    //    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    //        super.onActivityCreated(savedInstanceState);
    //        if (needSlide()) {
    //            SliderConfig mConfig = new SliderConfig.Builder()
    //                    .primaryColor(Color.TRANSPARENT)
    //                    .secondaryColor(Color.TRANSPARENT)
    //                    .position(SliderPosition.LEFT)
    //                    .edge(true)
    //                    .build();
    //            iSlider = SliderUtils.attachV4Fragment(this, null, mConfig);
    //        }
    //    }

}
