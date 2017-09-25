package com.example.news.dagger.news.othernews.special;

import com.example.commonlibrary.dagger.scope.PerActivity;
import com.example.news.dagger.NewsComponent;
import com.example.news.mvp.news.othernew.special.SpecialNewsActivity;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/25      20:21
 * QQ:             1981367757
 */
@PerActivity
@Component(dependencies = NewsComponent.class, modules = SpecialNewsModule.class)
public interface SpecialNewsComponent {
    public void inject(SpecialNewsActivity specialNewsActivity);
}
