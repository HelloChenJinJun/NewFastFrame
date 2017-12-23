package com.example.news;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.TextView;

import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.news.adapter.ScoreQueryAdapter;
import com.example.news.bean.ScoreBean;
import com.example.news.dagger.score.DaggerScoreQueryComponent;
import com.example.news.dagger.score.ScoreQueryScoreModule;
import com.example.news.mvp.score.ScoreQueryPresenter;

import javax.inject.Inject;

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

    @Override
    public void updateData(ScoreBean scoreBean) {
        if (scoreBean != null && scoreBean.getList() != null) {
            if (refresh.isRefreshing()) {
                scoreQueryAdapter.refreshData(scoreBean.getList());
            } else {
                scoreQueryAdapter.addData(scoreBean.getList());
                if (scoreQueryAdapter.getData().size() == 0) {
                    if (loadMoreFooterView!=null) {
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
        setToolBar(toolBarOption);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(true);
                onRefresh();
            }
        });
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ScoreQueryActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onRefresh() {
        presenter.getScore(true, "2016-2017", null);
    }


    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
        }
        super.showError(errorMsg, listener);
    }


    @Override
    public void hideLoading() {
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
        }
        if (scoreQueryAdapter.getData().size() == 0) {
            showEmptyView();
        }else {
            super.hideLoading();
        }

    }

    @Override
    public void loadMore() {
        presenter.getScore(false, "2016-2017", null);
    }
}
