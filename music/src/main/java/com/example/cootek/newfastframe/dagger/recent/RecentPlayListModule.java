package com.example.cootek.newfastframe.dagger.recent;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.cootek.newfastframe.adapter.RecentPlayListAdapter;
import com.example.cootek.newfastframe.mvp.recent.RecentPlayListFragment;
import com.example.cootek.newfastframe.mvp.recent.RecentPlayListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/11     14:07
 */
@Module
public class RecentPlayListModule {
    private RecentPlayListFragment mRecentPlayListFragment;


    public RecentPlayListModule(RecentPlayListFragment recentPlayListFragment) {
        mRecentPlayListFragment = recentPlayListFragment;
    }

    @Provides
    public RecentPlayListAdapter provideAdapter() {
        return new RecentPlayListAdapter();
    }

    @Provides
    public RecentPlayListPresenter providePresenter(DefaultModel defaultModel) {
        return new RecentPlayListPresenter(mRecentPlayListFragment, defaultModel);
    }
}
