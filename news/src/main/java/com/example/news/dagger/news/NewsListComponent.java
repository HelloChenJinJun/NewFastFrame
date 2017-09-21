package com.example.news.dagger.news;

import com.example.commonlibrary.dagger.scope.PerFragment;
import com.example.news.mvp.news.NewsListFragment;
import com.example.news.dagger.NewsComponent;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/16      16:52
 * QQ:             1981367757
 */
@PerFragment
@Component(dependencies = NewsComponent.class, modules = NewsListModule.class)
public interface NewsListComponent {
    public void inject(NewsListFragment newsListFragment);
}
