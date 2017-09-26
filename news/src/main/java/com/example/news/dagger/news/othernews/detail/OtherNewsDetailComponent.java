package com.example.news.dagger.news.othernews.detail;

import com.example.commonlibrary.dagger.scope.PerActivity;
import com.example.news.dagger.NewsComponent;
import com.example.news.mvp.news.othernew.detail.OtherNewsDetailActivity;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/26      16:07
 * QQ:             1981367757
 */
@PerActivity
@Component(dependencies = NewsComponent.class,modules = OtherNewsDetailModule.class)
public interface OtherNewsDetailComponent {
    public void inject(OtherNewsDetailActivity otherNewsDetailActivity);
}
