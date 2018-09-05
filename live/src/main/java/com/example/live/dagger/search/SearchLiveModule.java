package com.example.live.dagger.search;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.live.mvp.search.SearchLiveActivity;
import com.example.live.adapter.SearchLiveAdapter;
import com.example.live.mvp.search.SearchLivePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/15      12:38
 * QQ:             1981367757
 */
@Module
public class SearchLiveModule {
    private SearchLiveActivity searchLiveActivity;

    public SearchLiveModule(SearchLiveActivity searchLiveActivity) {
        this.searchLiveActivity = searchLiveActivity;
    }

    @Provides
    public SearchLiveAdapter provideSearchLiveAdapter(){
        return new SearchLiveAdapter();
    }



    @Provides
    public SearchLivePresenter provideSearchPresenter(DefaultModel searchLiveModel){
        return new SearchLivePresenter(searchLiveActivity,searchLiveModel);
    }
}
