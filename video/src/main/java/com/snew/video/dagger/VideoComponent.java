package com.snew.video.dagger;

import com.example.commonlibrary.dagger.component.AppComponent;
import com.example.commonlibrary.dagger.scope.PerApplication;
import com.example.commonlibrary.repository.DefaultRepositoryManager;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/8     17:06
 */
@PerApplication
@Component(dependencies = AppComponent.class,modules = VideoModule.class)
public interface VideoComponent {
    public DefaultRepositoryManager getRepositoryManager();
}
