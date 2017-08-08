package com.example.cootek.newfastframe;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by COOTEK on 2017/8/8.
 */
@Singleton
@Component(modules = {MainModule.class})
public interface MainComponent {

    public DaoSession getDaoSession();
}
