package com.example.cootek.newfastframe.dagger.album;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.cootek.newfastframe.mvp.album.AlbumListPresenter;
import com.example.cootek.newfastframe.mvp.search.AlbumListFragment;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/26     15:44
 */
@Module
public class AlbumListModule {
    private AlbumListFragment mAlbumListFragment;

    public AlbumListModule(AlbumListFragment albumListFragment) {
        mAlbumListFragment = albumListFragment;
    }

    @Provides
    public AlbumListPresenter providePresenter(DefaultModel defaultModel) {
        return new AlbumListPresenter(mAlbumListFragment, defaultModel);
    }
}
