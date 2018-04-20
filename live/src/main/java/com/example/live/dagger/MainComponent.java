package com.example.live.dagger;

import com.example.commonlibrary.bean.chat.DaoSession;
import com.example.commonlibrary.dagger.component.AppComponent;
import com.example.commonlibrary.dagger.scope.PerApplication;
import com.example.live.MainRepositoryManager;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/14      10:42
 * QQ:             1981367757
 */
@PerApplication
@Component(dependencies = AppComponent.class, modules = MainModule.class)
public interface MainComponent {
    public DaoSession getDaoSession();

    public MainRepositoryManager getMainRepositoryManager();
}
