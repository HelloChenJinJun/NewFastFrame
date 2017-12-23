package com.example.news.dagger.systemcenter;

import com.example.commonlibrary.dagger.scope.PerActivity;
import com.example.news.dagger.NewsComponent;
import com.example.news.mvp.systemcenter.SystemCenterActivity;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/23     18:04
 * QQ:         1981367757
 */
@PerActivity
@Component(dependencies = NewsComponent.class, modules = SystemCenterModule.class)
public interface SystemCenterComponent {
    public void inject(SystemCenterActivity systemCenterActivity);
}
