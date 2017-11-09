package com.example.cootek.newfastframe.dagger.listener;

import com.example.commonlibrary.dagger.scope.PerActivity;
import com.example.cootek.newfastframe.dagger.main.MainComponent;
import com.example.cootek.newfastframe.ui.LocalMusicActivity;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/11/5      22:48
 * QQ:             1981367757
 */
@PerActivity
@Component(dependencies = MainComponent.class, modules = LocalMusicActivityModule.class)
public interface LocalMusicActivityComponent {
    public void inject(LocalMusicActivity localMusicActivity);
}
