package com.example.news;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.TextView;

import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.AppUtil;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.news.adapter.ScoreQueryAdapter;
import com.example.news.bean.ScoreBean;
import com.example.news.bean.SystemUserBean;
import com.example.news.dagger.score.DaggerScoreQueryComponent;
import com.example.news.dagger.score.ScoreQueryScoreModule;
import com.example.news.event.ReLoginEvent;
import com.example.news.mvp.score.ScoreQueryPresenter;
import com.example.news.util.ReLoginUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/17     18:15
 * QQ:         1981367757
 */

public class ScoreQueryActivity extends BaseActivity<ScoreBean, ScoreQueryPresenter> implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
    private SwipeRefreshLayout refresh;
    private SuperRecyclerView display;
    @Inject
    ScoreQueryAdapter scoreQueryAdapter;
    private LoadMoreFooterView loadMoreFooterView;
    private ReLoginUtil reLoginUtil;
    private String result;
    private String str;

    @Override
    public void updateData(ScoreBean scoreBean) {
        if (scoreBean != null && scoreBean.getList() != null) {
            if (refresh.isRefreshing()) {
                scoreQueryAdapter.refreshData(scoreBean.getList());
            } else {
                scoreQueryAdapter.addData(scoreBean.getList());
                if (scoreQueryAdapter.getData().size() == 0) {
                    if (loadMoreFooterView != null) {
                        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                    }
                }
            }
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
        return R.layout.activity_score_query;
    }

    @Override
    protected void initView() {
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_activity_score_query_refresh);
        display = (SuperRecyclerView) findViewById(R.id.srcv_activity_score_query_display);
        refresh.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        DaggerScoreQueryComponent.builder()
                .newsComponent(NewsApplication.getNewsComponent())
                .scoreQueryScoreModule(new ScoreQueryScoreModule(this))
                .build().inject(this);
        display.setLayoutManager(new WrappedLinearLayoutManager(this));
        display.setAdapter(scoreQueryAdapter);
        display.setLoadMoreFooterView(loadMoreFooterView = new LoadMoreFooterView(this));
        display.setOnLoadMoreListener(this);
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("分数查询");
        toolBarOption.setRightResId(R.drawable.ic_list_blue_grey_900_24dp);
        toolBarOption.setRightListener(v -> {
            List<String> list = new ArrayList<>();
            list.add("大一");
            list.add("大二");
            list.add("大三");
            list.add("大四");
            showChooseDialog("搜索条件", list, (parent, view, position, id) -> {
                dismissBaseDialog();
                int year=Integer
                        .parseInt(str);
                StringBuilder stringBuilder=new StringBuilder();
                if (position == 0) {
                    stringBuilder.append(year).append("-").append((year+1));
                } else if (position == 1) {
                    stringBuilder.append(year+1).append("-").append((year+2));
                } else if (position == 2) {
                    stringBuilder.append(year+2).append("-").append((year+3));
                }else {
                    stringBuilder.append(year+3).append("-").append((year+4));
                }
                result=stringBuilder.toString();
                presenter.getScore(true, result, null);
            });
        });
        setToolBar(toolBarOption);
        presenter.registerEvent(ReLoginEvent.class, reLoginEvent -> {
            if (reLoginEvent.isSuccess() && reLoginEvent.getFrom().equals("score")) {
                refresh.setRefreshing(true);
                onRefresh();
            }
        });
        str=BaseApplication.getAppComponent()
                .getSharedPreferences().getString(ConstantUtil.YEAR,null);
        int year=Integer
                .parseInt(str);
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(year).append("-").append((year+1));
        result=stringBuilder.toString();
        runOnUiThread(() -> presenter.getScore(true, result, null));
    }


    @Override
    public void showLoading(String loadMessage) {
        refresh.setRefreshing(true);
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ScoreQueryActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onRefresh() {
        presenter.getScore(true, result, null);
    }


    @Override
    public void showError(String errorMsg, final EmptyLayout.OnRetryListener listener) {

        if (AppUtil.isNetworkAvailable(this)) {
            ToastUtils.showShortToast("Cookie失效");
            String account= BaseApplication.getAppComponent().getSharedPreferences()
                    .getString(ConstantUtil.ACCOUNT,null);
            String password=BaseApplication.getAppComponent().getSharedPreferences()
                    .getString(ConstantUtil.PASSWORD,null);
            reLoginUtil =new ReLoginUtil();
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
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
            super.showError(errorMsg, listener);
        }else {
            display.setLoadMoreStatus(LoadMoreFooterView.Status.ERROR);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (reLoginUtil != null) {
            reLoginUtil.clear();
        }
    }

    @Override
    public void hideLoading() {
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
        }
        if (scoreQueryAdapter.getData().size() == 0) {
            showEmptyView();
        } else {
            super.hideLoading();
        }

    }

    @Override
    public void loadMore() {
        display.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
//        presenter.getScore(false, "2016-2017", null);
    }
}
