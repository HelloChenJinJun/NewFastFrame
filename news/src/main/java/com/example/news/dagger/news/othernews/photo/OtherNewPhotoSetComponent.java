package com.example.news.dagger.news.othernews.photo;

import com.example.commonlibrary.dagger.scope.PerActivity;
import com.example.news.dagger.NewsComponent;
import com.example.news.mvp.news.othernew.photo.OtherNewPhotoSetActivity;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/26      17:04
 * QQ:             1981367757
 */
@PerActivity
@Component(dependencies = NewsComponent.class, modules = OtherNewPhotoSetModule.class)
public interface OtherNewPhotoSetComponent {
    public void inject(OtherNewPhotoSetActivity otherNewPhotoSetActivity);
}
