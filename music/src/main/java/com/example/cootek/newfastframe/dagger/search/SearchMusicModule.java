package com.example.cootek.newfastframe.dagger.search;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.cootek.newfastframe.mvp.search.SearchMusicFragment;
import com.example.cootek.newfastframe.mvp.search.SearchMusicPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/26     13:33
 */
@Module
public class SearchMusicModule {
    private SearchMusicFragment mSearchMusicFragment;

    public SearchMusicModule(SearchMusicFragment searchMusicFragment) {
        mSearchMusicFragment = searchMusicFragment;
    }
    @Provides
    public SearchMusicPresenter providePresenter(DefaultModel defaultModel){
        return new SearchMusicPresenter(mSearchMusicFragment,defaultModel);
    }
}
