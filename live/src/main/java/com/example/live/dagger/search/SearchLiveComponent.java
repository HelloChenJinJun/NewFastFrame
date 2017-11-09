package com.example.live.dagger.search;

import com.example.commonlibrary.dagger.scope.PerActivity;
import com.example.live.ui.SearchLiveActivity;
import com.example.live.dagger.MainComponent;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/15      12:38
 * QQ:             1981367757
 */
@PerActivity
@Component(dependencies = MainComponent.class,modules = SearchLiveModule.class)
public interface SearchLiveComponent {
    public void inject(SearchLiveActivity searchLiveActivity);
}
