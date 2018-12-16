package com.snew.video.dagger.search.detail;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.snew.video.adapter.SearchVideoDetailListAdapter;
import com.snew.video.mvp.search.detail.SearchVideoDetailFragment;
import com.snew.video.mvp.search.detail.SearchVideoDetailPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/15     8:59
 */
@Module
public class SearchDetailModule {
    private SearchVideoDetailFragment mSearchVideoDetailFragment;

    public SearchDetailModule(SearchVideoDetailFragment searchVideoDetailFragment) {
        mSearchVideoDetailFragment = searchVideoDetailFragment;
    }

    @Provides
    public SearchVideoDetailPresenter providePresenter(DefaultModel defaultModel) {
        return new SearchVideoDetailPresenter(mSearchVideoDetailFragment, defaultModel);
    }

    @Provides
    public SearchVideoDetailListAdapter provideAdapter() {
        return new SearchVideoDetailListAdapter();
    }


}
