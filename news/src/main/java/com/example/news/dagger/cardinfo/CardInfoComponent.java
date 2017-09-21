package com.example.news.dagger.cardinfo;

import com.example.commonlibrary.dagger.scope.PerActivity;
import com.example.news.mvp.cardinfo.CardInfoActivity;
import com.example.news.dagger.NewsComponent;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/19      23:22
 * QQ:             1981367757
 */
@PerActivity
@Component(dependencies = NewsComponent.class,modules = CardInfoModule.class)
public interface CardInfoComponent {
    public void inject(CardInfoActivity cardInfoActivity);
}
