package com.example.chat.mvp.notify;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.example.chat.ChatApplication;
import com.example.chat.R;
import com.example.chat.adapter.SystemNotifyAdapter;
import com.example.chat.bean.SystemNotifyBean;
import com.example.chat.dagger.notify.DaggerSystemNotifyComponent;
import com.example.chat.dagger.notify.SystemNotifyModule;
import com.example.chat.base.SlideBaseActivity;
import com.example.chat.events.UnReadSystemNotifyEvent;
import com.example.chat.manager.UserDBManager;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/16     14:11
 * QQ:         1981367757
 */

public class SystemNotifyActivity extends SlideBaseActivity<List<SystemNotifyBean>, SystemNotifyPresenter> implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
    private SwipeRefreshLayout refresh;
    private SuperRecyclerView display;


    @Inject
    SystemNotifyAdapter adapter;

    @Override
    public void updateData(List<SystemNotifyBean> o) {
        if (refresh.isRefreshing()) {
            adapter.refreshData(o);
        } else {
            adapter.addData(o);
        }
    }

    @Override
    protected boolean isNeedHeadLayout() {
        return true;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_system_notify;
    }

    @Override
    protected void initView() {
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_activity_system_notify_refresh);
        display = (SuperRecyclerView) findViewById(R.id.srcv_activity_system_notify_display);
        refresh.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        DaggerSystemNotifyComponent.builder().chatMainComponent(ChatApplication.getChatMainComponent())
                .systemNotifyModule(new SystemNotifyModule(this))
                .build().inject(this);
        display.setLayoutManager(new WrappedLinearLayoutManager(this));
        display.setLoadMoreFooterView(new LoadMoreFooterView(this));
        display.setOnLoadMoreListener(this);
        display.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                ToastUtils.showShortToast("浏览地址"
                +adapter.getData(position).getContentUrl());
            }
        });
        UserDBManager.getInstance().updateSystemNotifyReadStatus();
        RxBusManager.getInstance().post(new UnReadSystemNotifyEvent());
        runOnUiThread(() -> presenter.getAllSystemNotifyData(true, getRefreshTime(true)));
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("系统通知");
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
    }

    private String getRefreshTime(boolean isRefresh) {
        if (isRefresh) {
            return "0000-00-00 01:00:00";
        } else {
            SystemNotifyBean bean = adapter.getData(adapter.getData().size() - 1);
            if (bean != null) {
                return bean.getCreatedAt();
            } else {
                return "0000-00-00 01:00:00";
            }
        }
    }


    @Override
    public void showLoading(String loadMessage) {
        refresh.setRefreshing(true);
    }


    @Override
    public void hideLoading() {
        super.hideLoading();
        refresh.setRefreshing(false);
    }


    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        super.showError(errorMsg, listener);
        refresh.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        presenter.getAllSystemNotifyData(true, getRefreshTime(true));
    }

    @Override
    public void loadMore() {
        presenter.getAllSystemNotifyData(false,getRefreshTime(false));
    }


    public static void start(Activity activity){
        Intent intent=new Intent(activity,SystemNotifyActivity.class);
        activity.startActivity(intent);
    }

}
