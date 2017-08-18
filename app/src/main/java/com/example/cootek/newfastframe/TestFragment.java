package com.example.cootek.newfastframe;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.commonlibrary.baseadapter.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.mvp.BaseFragment;

/**
 * Created by COOTEK on 2017/8/17.
 */

public class TestFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
    private SwipeRefreshLayout refreshLayout;
    private SuperRecyclerView display;
    private MainAdapter adapter;

    @Override
    public void updateData(Object o) {

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
        return R.layout.fragment_test;
    }

    @Override
    protected void initView() {
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        display = (SuperRecyclerView) findViewById(R.id.display);
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        display.setLayoutManager(new LinearLayoutManager(getContext()));
        display.setLoadMoreFooterView(new LoadMoreFooterView(getContext()));
        display.setOnLoadMoreListener(this);
        adapter=new MainAdapter();
        adapter.setEnableAnimator(true);
        adapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Toast.makeText(getActivity(), "position:" + position, Toast.LENGTH_SHORT).show();
            }
        });
        display.setIAdapter(adapter);

    }

    @Override
    protected void updateView() {

    }

    public static BaseFragment newInstance() {
        return new TestFragment();
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void loadMore() {

    }
}
