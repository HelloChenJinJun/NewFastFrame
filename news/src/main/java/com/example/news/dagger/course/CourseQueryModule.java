package com.example.news.dagger.course;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.news.adapter.CourseQueryAdapter;
import com.example.news.mvp.course.CourseQueryActivity;
import com.example.news.mvp.course.CourseQueryPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/26     15:05
 * QQ:         1981367757
 */
@Module
public class CourseQueryModule {
    private CourseQueryActivity courseQueryActivity;

    public CourseQueryModule(CourseQueryActivity courseQueryActivity) {
        this.courseQueryActivity = courseQueryActivity;
    }

    @Provides
    public CourseQueryAdapter providerAdapter() {
        return new CourseQueryAdapter();
    }


    @Provides
    public CourseQueryPresenter providerPresenter(DefaultModel courseQueryModel) {
        return new CourseQueryPresenter(courseQueryActivity, courseQueryModel);
    }




}
