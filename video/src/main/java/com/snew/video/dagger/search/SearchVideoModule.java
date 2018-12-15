package com.snew.video.dagger.search;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.snew.video.mvp.search.SearchVideoActivity;
import com.snew.video.mvp.search.SearchVideoPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/15     8:56
 */
@Module
public class SearchVideoModule {
    private SearchVideoActivity mSearchVideoActivity;

    public SearchVideoModule(SearchVideoActivity searchVideoActivity) {
        mSearchVideoActivity = searchVideoActivity;
    }


    @Provides
    public SearchVideoPresenter providePresenter(DefaultModel defaultModel) {
        return new SearchVideoPresenter(mSearchVideoActivity, defaultModel);
    }

}
