package com.example.cootek.newfastframe;

import com.example.commonlibrary.dagger.component.AppComponent;
import com.example.commonlibrary.repository.BaseRepositoryManager;
import com.example.cootek.newfastframe.scope.PerApplication;


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
