package com.example.live;

import android.support.v4.widget.SwipeRefreshLayout;

import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.live.bean.ListLiveBean;
import com.example.live.dagger.list.DaggerListLiveComponent;
import com.example.live.dagger.list.ListLiveModule;

import javax.inject.Inject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/14      21:00
 * QQ:             1981367757
 */

public class ListLiveFragment extends BaseFragment<ListLiveBean,ListLivePresenter> implements SwipeRefreshLayout.OnRefreshListener {
    private SuperRecyclerView display;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String slug;
    @Inject
    ListLiveAdapter listLiveAdapter;
    @Override
    public void updateData(ListLiveBean listLiveBean) {
        if (listLiveBean!=null) {
            listLiveAdapter.addData(listLiveBean.getData());
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
        return R.layout.fragment_list_live;
    }

    @Override
    protected void initView() {
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.refresh_fragment_list_live_refresh);
        display= (SuperRecyclerView) findViewById(R.id.srcv_fragment_list_live_display);

    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            slug=getArguments().getString(LiveUtil.SLUG);
        }
        DaggerListLiveComponent.builder().listLiveModule(new ListLiveModule(this))
                .mainComponent(LiveApplication.getMainComponent()).build().inject(this);
        display.setLayoutManager(new WrappedGridLayoutManager(getContext(),2));
        display.setAdapter(listLiveAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void updateView() {
        presenter.getCategoryItemData(slug);

    }

    public static ListLiveFragment newInstance() {
        return new ListLiveFragment();
    }


    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
        if (listLiveAdapter.getData().size()>0) {
            super.hideLoading();
        }else {
            showEmptyView();
        }
    }


    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        super.showError(errorMsg, listener);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        presenter.getCategoryItemData(slug);
    }
}
