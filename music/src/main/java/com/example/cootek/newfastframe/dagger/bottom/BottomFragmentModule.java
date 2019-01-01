package com.example.cootek.newfastframe.dagger.bottom;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.cootek.newfastframe.mvp.bottom.BottomPresenter;
import com.example.cootek.newfastframe.mvp.bottom.BottomFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by COOTEK on 2017/8/14.
 */
@Module
public class BottomFragmentModule {
    private BottomFragment iView;


    public BottomFragmentModule(BottomFragment iView) {
        this.iView = iView;
    }


    @Provides
    public BottomPresenter provideBottomPresenter(DefaultModel bottomModel) {
        return new BottomPresenter(iView, bottomModel);
    }


}
