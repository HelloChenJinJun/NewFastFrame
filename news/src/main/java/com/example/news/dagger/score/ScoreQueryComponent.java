package com.example.news.dagger.score;

import com.example.commonlibrary.dagger.scope.PerActivity;
import com.example.news.ScoreQueryActivity;
import com.example.news.dagger.NewsComponent;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/19     15:19
 * QQ:         1981367757
 */
@PerActivity
@Component(dependencies = NewsComponent.class,modules = ScoreQueryScoreModule.class)
public interface ScoreQueryComponent {
    public void inject(ScoreQueryActivity scoreQueryActivity);
}
