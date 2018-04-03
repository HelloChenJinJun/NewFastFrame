package com.example.cootek.newfastframe.dagger.main;

import com.example.commonlibrary.bean.chat.DaoSession;
import com.example.commonlibrary.dagger.component.AppComponent;
import com.example.commonlibrary.dagger.scope.PerApplication;
import com.example.cootek.newfastframe.MainRepositoryManager;

import dagger.Component;

/**
 * Created by COOTEK on 2017/8/8.
 */
@PerApplication
@Component(dependencies = AppComponent.class, modules = {MainModule.class})
public interface MainComponent {

    public DaoSession getDaoSession();

    public MainRepositoryManager getRepositoryManager();
}
