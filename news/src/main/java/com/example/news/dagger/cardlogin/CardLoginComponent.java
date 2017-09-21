package com.example.news.dagger.cardlogin;

import com.example.commonlibrary.dagger.scope.PerActivity;
import com.example.news.mvp.cardlogin.CardLoginActivity;
import com.example.news.dagger.NewsComponent;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/19      23:08
 * QQ:             1981367757
 */
@PerActivity
@Component(dependencies = NewsComponent.class, modules = CardLoginModule.class)
public interface CardLoginComponent {
    public void inject(CardLoginActivity cardLoginActivity);


}
