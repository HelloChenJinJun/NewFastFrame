package com.example.news.mvp.course;

import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.news.bean.CourseQueryBean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/25     19:48
 * QQ:         1981367757
 */

public class CourseQueryPresenter extends BasePresenter<IView<CourseQueryBean>,CourseQueryModel>{
    public CourseQueryPresenter(IView<CourseQueryBean> iView, CourseQueryModel baseModel) {
        super(iView, baseModel);
    }



    public void getQueryCourseData(){
        iView.showLoading(null);
//        baseModel.getRepositoryManager().getApi()

    }
}
