package com.example.news.dagger.course;

import com.example.news.MainRepositoryManager;
import com.example.news.adapter.CourseQueryAdapter;
import com.example.news.mvp.course.CourseQueryActivity;
import com.example.news.mvp.course.CourseQueryModel;
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
    public CourseQueryPresenter providerPresenter(CourseQueryModel courseQueryModel) {
        return new CourseQueryPresenter(courseQueryActivity, courseQueryModel);
    }


    @Provides
    public CourseQueryModel providerModel(MainRepositoryManager mainRepositoryManager) {
        return new CourseQueryModel(mainRepositoryManager);
    }


}
