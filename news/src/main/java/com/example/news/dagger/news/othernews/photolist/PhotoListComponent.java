package com.example.news.dagger.news.othernews.photolist;

import com.example.commonlibrary.dagger.scope.PerFragment;
import com.example.news.dagger.NewsComponent;
import com.example.news.mvp.news.othernew.photolist.PhotoListFragment;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/29      17:48
 * QQ:             1981367757
 */
@PerFragment
@Component(dependencies = NewsComponent.class,modules = PhotoListModule.class)
public interface PhotoListComponent {
    public void inject(PhotoListFragment photoListFragment);
}
