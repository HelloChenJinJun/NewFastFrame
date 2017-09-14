package com.example.cootek.newfastframe.dagger.bottom;

import com.example.cootek.newfastframe.MainRepositoryManager;
import com.example.cootek.newfastframe.mvp.bottom.BottomModel;
import com.example.cootek.newfastframe.mvp.bottom.BottomPresenter;
import com.example.cootek.newfastframe.mvp.bottom.IBottomView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by COOTEK on 2017/8/14.
 */
@Module
public class BottomFragmentModule {
    private IBottomView iView;


    public BottomFragmentModule(IBottomView iView) {
        this.iView = iView;
    }


    @Provides
    public BottomPresenter provideBottomPresenter(BottomModel bottomModel) {
        return new BottomPresenter(iView, bottomModel);
    }


    @Provides
    public BottomModel provideBottomModel(MainRepositoryManager baseRepositoryManager) {
        return new BottomModel(baseRepositoryManager);
    }

}
