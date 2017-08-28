package com.example.cootek.newfastframe.dagger;

import com.example.commonlibrary.mvp.IView;
import com.example.commonlibrary.repository.BaseRepositoryManager;
import com.example.cootek.newfastframe.MainAdapter;
import com.example.cootek.newfastframe.MainRepositoryManager;
import com.example.cootek.newfastframe.mvp.MainModel;
import com.example.cootek.newfastframe.mvp.MainPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by COOTEK on 2017/8/13.
 */

@Module
public class MainFragmentModule {
    private IView iView;


    public MainFragmentModule(IView iView) {
        this.iView = iView;
    }

    @Provides
    MainAdapter providerMainAdapter() {
        return new MainAdapter();
    }


    @Provides
    MainPresenter provideMainPresenter(MainModel mainModel) {
        return new MainPresenter(iView, mainModel);
    }

    @Provides
    MainModel provideMainModel(MainRepositoryManager mainRepositoryManager) {
        return new MainModel(mainRepositoryManager);
    }
}
