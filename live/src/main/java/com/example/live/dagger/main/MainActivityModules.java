package com.example.live.dagger.main;

import com.example.commonlibrary.baseadapter.adapter.ViewPagerAdapter;
import com.example.live.ui.MainActivity;
import com.example.live.MainRepositoryManager;
import com.example.live.mvp.main.MainModel;
import com.example.live.mvp.main.MainPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/14      17:03
 * QQ:             1981367757
 */
@Module
public class MainActivityModules {
    private MainActivity mainActivity;

    public MainActivityModules(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Provides
    public ViewPagerAdapter provideViewPagerAdapter() {
        return new ViewPagerAdapter(mainActivity.getSupportFragmentManager());
    }


    @Provides
    public MainPresenter provideMainPresenter(MainModel mainModel) {
        return new MainPresenter(mainActivity, mainModel);
    }

    @Provides
    public MainModel provideMainModel(MainRepositoryManager mainRepositoryManager) {
        return new MainModel(mainRepositoryManager);
    }


}
