package com.example.news.dagger;

import com.example.commonlibrary.dagger.component.AppComponent;
import com.example.commonlibrary.dagger.scope.PerApplication;
import com.example.news.MainRepositoryManager;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/16      16:14
 * QQ:             1981367757
 */
@PerApplication
@Component(dependencies = AppComponent.class,modules =NewsModule.class)
public interface NewsComponent {
    public MainRepositoryManager getRepositoryManager();
    public OkHttpClient getOkHttpClient();
}
