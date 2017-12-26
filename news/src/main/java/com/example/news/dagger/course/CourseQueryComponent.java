package com.example.news.dagger.course;

import com.example.commonlibrary.dagger.scope.PerActivity;
import com.example.news.dagger.NewsComponent;
import com.example.news.mvp.course.CourseQueryActivity;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/26     15:04
 * QQ:         1981367757
 */
@PerActivity
@Component(dependencies = NewsComponent.class, modules = CourseQueryModule.class)
public interface CourseQueryComponent {
    public void inject(CourseQueryActivity courseQueryActivity);
}
