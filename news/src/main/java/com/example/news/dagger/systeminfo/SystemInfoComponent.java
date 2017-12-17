package com.example.news.dagger.systeminfo;

import com.example.commonlibrary.dagger.scope.PerActivity;
import com.example.news.dagger.NewsComponent;
import com.example.news.mvp.systeminfo.SystemInfoLoginActivity;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/17     17:51
 * QQ:         1981367757
 */
@PerActivity
@Component(dependencies = NewsComponent.class,modules = SystemInfoModule.class)
public interface SystemInfoComponent {
    public void inject(SystemInfoLoginActivity systemInfoLoginActivity);
}
