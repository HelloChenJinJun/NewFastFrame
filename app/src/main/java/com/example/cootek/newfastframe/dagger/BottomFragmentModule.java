package com.example.cootek.newfastframe.dagger;

import com.example.commonlibrary.mvp.IView;
import com.example.commonlibrary.repository.BaseRepositoryManager;
import com.example.cootek.newfastframe.MainRepositoryManager;
import com.example.cootek.newfastframe.mvp.BottomModel;
import com.example.cootek.newfastframe.mvp.BottomPresenter;
import com.example.cootek.newfastframe.mvp.IBottomView;

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
