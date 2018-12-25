package com.example.cootek.newfastframe.dagger.songlist;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.cootek.newfastframe.adapter.RecentPlayListAdapter;
import com.example.cootek.newfastframe.mvp.songlist.SongListFragment;
import com.example.cootek.newfastframe.mvp.songlist.SongListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/7     11:15
 */
@Module
public class SongListModule {
    private SongListFragment mSongListFragment;

    public SongListModule(SongListFragment songListFragment) {
        mSongListFragment = songListFragment;
    }


    @Provides
    public RecentPlayListAdapter provideAdapter() {
        return new RecentPlayListAdapter();
    }

    @Provides
    public SongListPresenter providePresenter(DefaultModel defaultModel) {
        return new SongListPresenter(mSongListFragment, defaultModel);
    }

}
