package com.example.news.mvp.course;

import android.app.Activity;
import android.content.Intent;
import android.os.UserManager;
import android.widget.GridView;

import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.utils.AppUtil;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.news.NewsApplication;
import com.example.news.R;
import com.example.news.adapter.CourseQueryAdapter;
import com.example.news.bean.CourseQueryBean;
import com.example.news.bean.SystemUserBean;
import com.example.news.dagger.course.CourseQueryModule;
import com.example.news.dagger.course.DaggerCourseQueryComponent;
import com.example.news.util.ReLoginUtil;

import java.util.ArrayList;
import java.util.List;

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
    private ReLoginUtil reLoginUtil;

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
        toolBarOption.setRightResId(R.drawable.ic_list_blue_grey_900_24dp);
        toolBarOption.setRightListener(v -> {
            List<String> list = new ArrayList<>();
            list.add("大一第一学期");
            list.add("大一第二学期");
            list.add("大二第一学期");
            list.add("大二第二学期");
            list.add("大三第一学期");
            list.add("大三第二学期");
            list.add("大四第一学期");
            list.add("大四第二学期");
            showChooseDialog("搜索条件", list, (parent, view, position, id) -> {
                dismissBaseDialog();
                String str=BaseApplication.getAppComponent()
                        .getSharedPreferences().getString(ConstantUtil.YEAR,null);
                int year=Integer
                        .parseInt(str);
                if (position == 0) {
                    presenter.getQueryCourseData(str, 3);
                } else if (position == 1) {
                    presenter.getQueryCourseData(str, 12);
                } else if (position == 2) {
                    presenter.getQueryCourseData(str, 3);
                } else if (position == 3) {
                    presenter.getQueryCourseData((year+1)+"", 12);
                } else if (position == 4) {
                    presenter.getQueryCourseData((year+2)+"", 3);
                } else if (position == 5) {
                    presenter.getQueryCourseData((year+2)+"", 12);
                } else if (position == 6) {
                    presenter.getQueryCourseData((year+3)+"", 3);
                } else if (position == 7) {
                    presenter.getQueryCourseData((year+3)+"", 12);
                }

            });
        });
        setToolBar(toolBarOption);
        runOnUiThread(() -> presenter.getQueryCourseData("2016", 3));
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, CourseQueryActivity.class);
        activity.startActivity(intent);
    }


    @Override
    public void showError(String errorMsg, final EmptyLayout.OnRetryListener listener) {
        if (AppUtil.isNetworkAvailable(this)) {
            ToastUtils.showShortToast("Cookie失效");
            String account= BaseApplication.getAppComponent().getSharedPreferences()
                    .getString(ConstantUtil.ACCOUNT,null);
            String password=BaseApplication.getAppComponent().getSharedPreferences()
                    .getString(ConstantUtil.PASSWORD,null);
            reLoginUtil=new ReLoginUtil();
            reLoginUtil.login(account, password, new ReLoginUtil.CallBack() {
                @Override
                public void onSuccess(SystemUserBean systemUserBean) {
                    if (listener!=null) {
                        listener.onRetry();
                    }
                }

                @Override
                public void onFailed(String errorMessage) {
                    ToastUtils.showShortToast("重试失败"+errorMessage);
                    hideLoading();
                }
            });
            return;
        }
        super.showError(errorMsg, listener);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (reLoginUtil != null) {
            reLoginUtil.clear();
        }
    }
}
