package com.example.live.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.live.LiveApplication;
import com.example.live.R;
import com.example.live.adapter.SearchLiveAdapter;
import com.example.live.bean.SearchLiveBean;
import com.example.live.dagger.search.DaggerSearchLiveComponent;
import com.example.live.dagger.search.SearchLiveModule;
import com.example.live.mvp.search.SearchLivePresenter;
import com.example.live.util.LiveUtil;

import java.util.List;

import javax.inject.Inject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/15      10:32
 * QQ:             1981367757
 */

public class SearchLiveActivity extends BaseActivity<List<SearchLiveBean.DataBean.LiveInfo>, SearchLivePresenter> implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener, View.OnClickListener {
    private SwipeRefreshLayout refreshLayout;
    private SuperRecyclerView display;
    @Inject
    SearchLiveAdapter searchLiveAdapter;
    private LoadMoreFooterView loadMoreFooterView;
    private EditText input;


    @Override
    public void updateData(List<SearchLiveBean.DataBean.LiveInfo> list) {
        if (loadMoreFooterView.getStatus()!= LoadMoreFooterView.Status.LOADING) {
            searchLiveAdapter.clearAllData();
            searchLiveAdapter.notifyDataSetChanged();
            if (list != null) {
                CommonLogger.e("listSize" + list.size());
            }
            searchLiveAdapter.addData(list);
        }else {
            searchLiveAdapter.addData(list);
            if (searchLiveAdapter.getData().size() == 0) {
                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
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
        return R.layout.activity_search_live;
    }

    @Override
    protected void initView() {
        input = (EditText) findViewById(R.id.et_activity_search_live_input);
        TextView search = (TextView) findViewById(R.id.tv_activity_search_live_search);
        search.setOnClickListener(this);
        display = (SuperRecyclerView) findViewById(R.id.srcv_activity_search_live_display);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_activity_search_live_refresh);
    }

    @Override
    protected void initData() {
        DaggerSearchLiveComponent.builder().mainComponent(LiveApplication.getMainComponent())
                .searchLiveModule(new SearchLiveModule(this)).build().inject(this);
        refreshLayout.setOnRefreshListener(this);
        display.setLayoutManager(new WrappedGridLayoutManager(this, 2));
        loadMoreFooterView = new LoadMoreFooterView(this);
        display.setLoadMoreFooterView(loadMoreFooterView);
        display.setOnLoadMoreListener(this);
        display.setAdapter(searchLiveAdapter);
        searchLiveAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent intent=new Intent(view.getContext(),VideoActivity.class);
                SearchLiveBean.DataBean.LiveInfo dataEntity=searchLiveAdapter.getData(position);
                intent.putExtra(LiveUtil.UID,dataEntity.getUid());
                intent.putExtra(LiveUtil.THUMB,dataEntity.getThumb());
                intent.putExtra(LiveUtil.IS_FULL,LiveUtil.SHOWING.equalsIgnoreCase(dataEntity.getCategory_slug()));
                startActivity(intent);
            }
        });
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("搜索");
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SearchLiveActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onRefresh() {
        if (loadMoreFooterView.getStatus() == LoadMoreFooterView.Status.LOADING) {
            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        }
        if (TextUtils.isEmpty(input.getText().toString().trim())) {
            ToastUtils.showShortToast("内容不能为空哦");
        } else {
            presenter.search(false, true, input.getText().toString().trim());
        }
    }

    @Override
    public void loadMore() {
        if (TextUtils.isEmpty(input.getText().toString().trim())) {
            ToastUtils.showShortToast("内容不能为空哦");
        } else {
            presenter.search(false, false, input.getText().toString().trim());
        }
    }


    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        if (loadMoreFooterView.getStatus() == LoadMoreFooterView.Status.LOADING) {
            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.ERROR);
        } else {
            super.showError(errorMsg, listener);
        }
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        if (searchLiveAdapter.getData().size()>0) {
            super.hideLoading();
        }else {
            showEmptyView();
        }
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(input.getText().toString().trim())) {
            ToastUtils.showShortToast("内容不能为空哦");
        } else {
            presenter.search(false, true, input.getText().toString().trim());
        }
    }
}
