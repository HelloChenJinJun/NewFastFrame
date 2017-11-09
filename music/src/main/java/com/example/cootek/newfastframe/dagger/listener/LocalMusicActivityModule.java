package com.example.cootek.newfastframe.dagger.listener;

import com.example.commonlibrary.mvp.view.IView;
import com.example.cootek.newfastframe.MainRepositoryManager;
import com.example.cootek.newfastframe.adapter.LocalMusicAdapter;
import com.example.cootek.newfastframe.mvp.main.MainModel;
import com.example.cootek.newfastframe.mvp.main.MainPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/11/5      22:50
 * QQ:             1981367757
 */
@Module
public class LocalMusicActivityModule {
    private IView iView;


    public LocalMusicActivityModule(IView iView) {
        this.iView = iView;
    }

    @Provides
    LocalMusicAdapter providerMainAdapter() {
        return new LocalMusicAdapter();
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
