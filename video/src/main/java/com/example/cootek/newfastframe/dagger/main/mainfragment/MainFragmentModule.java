package com.example.cootek.newfastframe.dagger.main.mainfragment;

import com.example.commonlibrary.mvp.view.IView;
import com.example.cootek.newfastframe.adapter.LocalListAdapter;
import com.example.cootek.newfastframe.MainRepositoryManager;
import com.example.cootek.newfastframe.mvp.main.MainModel;
import com.example.cootek.newfastframe.mvp.main.MainPresenter;

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
    LocalListAdapter providerMainAdapter() {
        return new LocalListAdapter();
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
