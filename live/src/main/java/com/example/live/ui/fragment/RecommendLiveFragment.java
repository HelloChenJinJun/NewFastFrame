package com.example.live.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;

import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.live.LiveApplication;
import com.example.live.R;
import com.example.live.adapter.RecommendLiveAdapter;
import com.example.live.bean.RecommendLiveBean;
import com.example.live.dagger.recommend.DaggerRecommendLiveComponent;
import com.example.live.dagger.recommend.RecommendLiveModule;
import com.example.live.mvp.recommend.RecommendLivePresenter;

import javax.inject.Inject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/14      17:20
 * QQ:             1981367757
 */

public class RecommendLiveFragment extends BaseFragment<RecommendLiveBean, RecommendLivePresenter> implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private SuperRecyclerView display;
    @Inject
    RecommendLiveAdapter recommendLiveAdapter;

    @Override
    public void updateData(RecommendLiveBean recommendLiveBean) {
        if (recommendLiveBean != null) {
            recommendLiveAdapter.addData(recommendLiveBean.getRoom());
        }
    }

    @Override
    protected boolean isNeedHeadLayout() {
        return false;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return true;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_recommend_live;
    }

    @Override
    protected void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_fragment_recommend_live_refresh);
        display = (SuperRecyclerView) findViewById(R.id.srcv_fragment_recommend_live_display);
    }

    @Override
    protected void initData() {
        DaggerRecommendLiveComponent.builder().recommendLiveModule(new RecommendLiveModule(this))
                .mainComponent(LiveApplication.getMainComponent()).build().inject(this);
        display.setLayoutManager(new WrappedLinearLayoutManager(getContext()));
        display.setAdapter(recommendLiveAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void updateView() {
        presenter.getRecommendLiveData(true, true);
    }

    public static RecommendLiveFragment newInstance() {
        return new RecommendLiveFragment();
    }

    @Override
    public void onRefresh() {
        presenter.getRecommendLiveData(false, true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
        if (recommendLiveAdapter.getData().size() > 0) {
            super.hideLoading();
        } else {
            showEmptyView();
        }
    }


    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        super.showError(errorMsg, listener);
        swipeRefreshLayout.setRefreshing(false);
    }
}
