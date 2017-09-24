package com.example.news.dagger.news.othernews;

import com.example.commonlibrary.dagger.scope.PerFragment;
import com.example.news.dagger.NewsComponent;
import com.example.news.mvp.news.othernew.OtherNewsListFragment;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/24      18:33
 * QQ:             1981367757
 */
@PerFragment
@Component(dependencies = NewsComponent.class,modules = OtherNewsModule.class)
public interface OtherNewsComponent {
    public void inject(OtherNewsListFragment otherNewsListFragment);
}
