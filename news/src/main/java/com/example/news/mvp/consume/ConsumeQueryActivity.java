package com.example.news.mvp.consume;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;

import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.news.NewsApplication;
import com.example.news.R;
import com.example.news.adapter.ConsumeQueryAdapter;
import com.example.news.bean.ConsumeQueryBean;
import com.example.news.dagger.consume.ConsumeQueryModule;
import com.example.news.dagger.consume.DaggerConsumeQueryComponent;
import com.example.news.mvp.systemcenter.SystemCenterActivity;

import javax.inject.Inject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/21     17:04
 * QQ:         1981367757
 */

public class ConsumeQueryActivity extends BaseActivity<ConsumeQueryBean, ConsumeQueryPresenter> implements OnLoadMoreListener {
    private SwipeRefreshLayout refresh;
    private SuperRecyclerView display;
    @Inject
    ConsumeQueryAdapter consumeQueryAdapter;

    @Override
    public void updateData(ConsumeQueryBean o) {
        if (refresh.isRefreshing()) {
            consumeQueryAdapter.refreshData(o != null ? o.getList() : null);
        } else {
            if (o == null || o.getList() == null || o.getList().size() == 0) {
                display.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
            } else {
                consumeQueryAdapter.addData(o.getList());
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
        return R.layout.activity_consume_query;
    }

    @Override
    protected void initView() {
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_activity_consume_query_refresh);
        display = (SuperRecyclerView) findViewById(R.id.srcv_activity_consume_query_display);

    }

    @Override
    protected void initData() {
        ToolBarOption toolBarOption=new ToolBarOption();
        toolBarOption.setTitle("消费明细");
        setToolBar(toolBarOption);
        DaggerConsumeQueryComponent.builder().newsComponent(NewsApplication
                .getNewsComponent())
                .consumeQueryModule(new ConsumeQueryModule(this)).build()
                .inject(this);
        display.setLayoutManager(new WrappedLinearLayoutManager(this));
        display.setLoadMoreFooterView(new LoadMoreFooterView(this));
        display.setOnLoadMoreListener(this);
        display.setAdapter(consumeQueryAdapter);
        display.post(new Runnable() {
            @Override
            public void run() {
                presenter.getQueryData(true);
            }
        });
    }


    @Override
    public void showLoading(String loadMessage) {
        refresh.setRefreshing(true);
    }

    @Override
    public void loadMore() {
        presenter.getQueryData(false);
    }


    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
            super.showError(errorMsg, listener);
        } else {
            display.setLoadMoreStatus(LoadMoreFooterView.Status.ERROR);
        }

    }


    @Override
    public void hideLoading() {
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
        }
        if (consumeQueryAdapter.getData().size() == 0) {
            showEmptyView();
        } else {
            super.hideLoading();
        }
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ConsumeQueryActivity.class);
        activity.startActivity(intent);
    }
}
