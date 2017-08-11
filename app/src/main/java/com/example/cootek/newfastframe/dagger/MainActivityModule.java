package com.example.cootek.newfastframe.dagger;

import android.view.View;

import com.example.commonlibrary.mvp.IView;
import com.example.cootek.newfastframe.MainAdapter;
import com.example.cootek.newfastframe.MainRepositoryManager;
import com.example.cootek.newfastframe.Music;
import com.example.cootek.newfastframe.mvp.MainModel;
import com.example.cootek.newfastframe.mvp.MainPresenter;

import java.util.List;

import dagger.Module;
import dagger.Provides;

/**
 * Created by COOTEK on 2017/8/11.
 */

@Module
public class MainActivityModule {



    private IView iView;


    public MainActivityModule(IView iView) {
        this.iView = iView;
    }

    @Provides
    MainAdapter providerMainAdapter(){
        return new MainAdapter();
    }


    @Provides
    MainPresenter provideMainPresenter(MainModel mainModel){
        return new MainPresenter(iView,mainModel);
    }

    @Provides
    MainModel provideMainModel(MainRepositoryManager mainRepositoryManager){
        return new MainModel(mainRepositoryManager);
    }

}
