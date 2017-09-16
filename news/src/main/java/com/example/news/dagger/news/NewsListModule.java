package com.example.news.dagger.news;

import com.example.news.MainRepositoryManager;
import com.example.news.NewsListAdapter;
import com.example.news.NewsListFragment;
import com.example.news.mvp.NewsListModel;
import com.example.news.mvp.NewsListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/16      16:53
 * QQ:             1981367757
 */
@Module
public class NewsListModule {
    private NewsListFragment newsListFragment;

    public NewsListModule(NewsListFragment newsListFragment) {
        this.newsListFragment = newsListFragment;
    }


    @Provides
    public NewsListAdapter provideNewsListAdapter() {
        return new NewsListAdapter();
    }


    @Provides
    public NewsListPresenter provideNewsListPresenter(NewsListModel newsListModel) {
        return new NewsListPresenter(newsListFragment, newsListModel);
    }

    @Provides
    public NewsListModel provideNewsListModel(MainRepositoryManager mainRepositoryManager) {
        return new NewsListModel(mainRepositoryManager);
    }
}
