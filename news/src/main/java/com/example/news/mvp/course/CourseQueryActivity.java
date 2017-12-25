package com.example.news.mvp.course;

import android.app.Activity;
import android.content.Intent;
import android.widget.GridView;

import com.example.commonlibrary.BaseActivity;
import com.example.news.R;
import com.example.news.adapter.CourseQueryAdapter;
import com.example.news.bean.CourseQueryBean;
import com.example.news.mvp.systemcenter.SystemCenterActivity;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/24     17:26
 * QQ:         1981367757
 */

public class CourseQueryActivity extends BaseActivity<CourseQueryBean,CourseQueryPresenter> {


    private GridView display;
    private CourseQueryAdapter courseQueryAdapter;


    @Override
    public void updateData(CourseQueryBean o) {

    }

    @Override
    protected boolean isNeedHeadLayout() {
        return false;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_course_query;
    }

    @Override
    protected void initView() {
            display= (GridView) findViewById(R.id.gv_activity_course_query_display);
    }

    @Override
    protected void initData() {
        courseQueryAdapter=new CourseQueryAdapter();
        display.setAdapter(courseQueryAdapter);
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, CourseQueryActivity.class);
        activity.startActivity(intent);
    }
}
