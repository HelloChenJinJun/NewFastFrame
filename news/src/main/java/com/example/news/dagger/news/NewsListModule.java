package com.example.news.dagger.news;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.repository.DefaultRepositoryManager;
import com.example.news.adapter.NewsListAdapter;
import com.example.news.mvp.news.NewsListFragment;
import com.example.news.mvp.news.NewsListPresenter;

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
    public NewsListPresenter provideNewsListPresenter(DefaultModel defaultModel) {
        return new NewsListPresenter(newsListFragment, defaultModel);
    }

}
