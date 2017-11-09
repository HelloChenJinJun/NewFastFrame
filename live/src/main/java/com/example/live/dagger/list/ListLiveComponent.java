package com.example.live.dagger.list;

import com.example.commonlibrary.dagger.scope.PerFragment;
import com.example.live.ui.fragment.ListLiveFragment;
import com.example.live.dagger.MainComponent;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/14      21:09
 * QQ:             1981367757
 */
@PerFragment
@Component(dependencies = MainComponent.class, modules = ListLiveModule.class)
public interface ListLiveComponent {
    public void inject(ListLiveFragment listLiveFragment);
}
