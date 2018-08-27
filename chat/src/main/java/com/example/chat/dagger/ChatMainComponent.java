package com.example.chat.dagger;

import com.example.chat.MainRepositoryManager;
import com.example.commonlibrary.bean.chat.DaoSession;
import com.example.commonlibrary.dagger.component.AppComponent;
import com.example.commonlibrary.dagger.scope.PerApplication;
import com.example.commonlibrary.repository.DefaultRepositoryManager;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/10/9      15:35
 * QQ:             1981367757
 */
@PerApplication
@Component(dependencies = AppComponent.class,modules = {ChatMainModule.class})
public interface ChatMainComponent {
    public DefaultRepositoryManager getDefaultRepositoryManager();
}
