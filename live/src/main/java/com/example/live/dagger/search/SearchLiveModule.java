package com.example.live.dagger.search;

import com.example.live.MainRepositoryManager;
import com.example.live.SearchLiveActivity;
import com.example.live.SearchLiveAdapter;
import com.example.live.SearchLiveModel;
import com.example.live.SearchLivePresenter;

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
    public SearchLiveModel provideSearchLiveModel(MainRepositoryManager mainRepositoryManager){
        return new SearchLiveModel(mainRepositoryManager);
    }


    @Provides
    public SearchLivePresenter provideSearchPresenter(SearchLiveModel searchLiveModel){
        return new SearchLivePresenter(searchLiveActivity,searchLiveModel);
    }
}
