package com.example.news.mvp.course;

import android.app.Activity;
import android.content.Intent;
import android.widget.GridView;

import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.news.NewsApplication;
import com.example.news.R;
import com.example.news.adapter.CourseQueryAdapter;
import com.example.news.bean.CourseQueryBean;
import com.example.news.dagger.course.CourseQueryModule;
import com.example.news.dagger.course.DaggerCourseQueryComponent;

import javax.inject.Inject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/24     17:26
 * QQ:         1981367757
 */

public class CourseQueryActivity extends BaseActivity<CourseQueryBean, CourseQueryPresenter> {


    private GridView display;
    @Inject
    CourseQueryAdapter courseQueryAdapter;


    @Override
    public void updateData(CourseQueryBean courseQueryBean) {
        if (courseQueryBean != null && courseQueryBean.getKbList() != null) {
            courseQueryAdapter.refreshData(courseQueryBean.getKbList());
        }
    }

    @Override
    protected boolean isNeedHeadLayout() {
        return true;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return true;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_course_query;
    }

    @Override
    protected void initView() {
        display = (GridView) findViewById(R.id.gv_activity_course_query_display);
    }

    @Override
    protected void initData() {
        DaggerCourseQueryComponent.builder().courseQueryModule(new CourseQueryModule(this))
                .newsComponent(NewsApplication.getNewsComponent())
                .build().inject(this);
        courseQueryAdapter = new CourseQueryAdapter();
        display.setAdapter(courseQueryAdapter);
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("课表查询");
        setToolBar(toolBarOption);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                presenter.getQueryCourseData("2016", 3);
            }
        });
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, CourseQueryActivity.class);
        activity.startActivity(intent);
    }
}
