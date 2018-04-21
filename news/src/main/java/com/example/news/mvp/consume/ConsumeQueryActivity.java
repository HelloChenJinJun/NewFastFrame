package com.example.news.mvp.consume;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;

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
import com.example.news.NewsApplication;
import com.example.news.R;
import com.example.news.adapter.ConsumeQueryAdapter;
import com.example.news.bean.ConsumeQueryBean;
import com.example.news.bean.SystemUserBean;
import com.example.news.dagger.consume.ConsumeQueryModule;
import com.example.news.dagger.consume.DaggerConsumeQueryComponent;
import com.example.news.event.ReLoginEvent;
import com.example.news.util.ReLoginUtil;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/21     17:04
 * QQ:         1981367757
 */

public class ConsumeQueryActivity extends BaseActivity<ConsumeQueryBean, ConsumeQueryPresenter> implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout refresh;
    private SuperRecyclerView display;
    @Inject
    ConsumeQueryAdapter consumeQueryAdapter;
    private ReLoginUtil loginUtil;

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
        refresh.setOnRefreshListener(this);

    }

    @Override
    protected void initData() {
        ToolBarOption toolBarOption = new ToolBarOption();
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
        presenter.registerEvent(ReLoginEvent.class, new Consumer<ReLoginEvent>() {
            @Override
            public void accept(ReLoginEvent reLoginEvent) throws Exception {
                if (reLoginEvent.isSuccess() && reLoginEvent.getFrom().equals("consume")) {
                    presenter.getQueryData(true);
                }
            }
        });
        display.post(() -> presenter.getQueryData(true));
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
    protected void onDestroy() {
        super.onDestroy();
        if (loginUtil != null) {
            loginUtil.clear();
        }
    }

    @Override
    public void showError(String errorMsg, final EmptyLayout.OnRetryListener listener) {
        if (AppUtil.isNetworkAvailable(this)) {
            ToastUtils.showShortToast("Cookie失效");
            String account= BaseApplication.getAppComponent().getSharedPreferences()
                    .getString(ConstantUtil.ACCOUNT,null);
            String password=BaseApplication.getAppComponent().getSharedPreferences()
                    .getString(ConstantUtil.PASSWORD,null);
             loginUtil=new ReLoginUtil();
            loginUtil.login(account, password, new ReLoginUtil.CallBack() {
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

    @Override
    public void onRefresh() {
        presenter.getQueryData(true);

    }
}
