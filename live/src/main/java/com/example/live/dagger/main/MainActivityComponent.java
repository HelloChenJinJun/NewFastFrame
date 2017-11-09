package com.example.live.dagger.main;

import com.example.commonlibrary.dagger.scope.PerActivity;
import com.example.live.ui.MainActivity;
import com.example.live.dagger.MainComponent;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/14      17:02
 * QQ:             1981367757
 */
@PerActivity
@Component(dependencies = MainComponent.class,modules = MainActivityModules.class)
public interface MainActivityComponent {

    public void inject(MainActivity mainActivity);
}
