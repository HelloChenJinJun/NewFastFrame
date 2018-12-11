package com.example.cootek.newfastframe.dagger.recent;

import com.example.commonlibrary.dagger.scope.PerFragment;
import com.example.cootek.newfastframe.dagger.main.MainComponent;
import com.example.cootek.newfastframe.mvp.recent.RecentPlayListFragment;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/11     14:07
 */
@PerFragment
@Component(dependencies = MainComponent.class, modules = RecentPlayListModule.class)
public interface RecentPlayListComponent {

    public void inject(RecentPlayListFragment recentPlayListFragment);
}
