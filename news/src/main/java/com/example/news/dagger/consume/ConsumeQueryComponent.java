package com.example.news.dagger.consume;

import com.example.commonlibrary.dagger.scope.PerActivity;
import com.example.news.dagger.NewsComponent;
import com.example.news.mvp.consume.ConsumeQueryActivity;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/21     18:02
 * QQ:         1981367757
 */
@PerActivity
@Component(dependencies = NewsComponent.class, modules = ConsumeQueryModule.class)
public interface ConsumeQueryComponent {
    public void inject(ConsumeQueryActivity consumeQueryActivity);
}
