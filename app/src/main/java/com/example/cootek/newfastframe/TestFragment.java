package com.example.cootek.newfastframe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.commonlibrary.baseadapter.OnLoadMoreListener;
import com.example.commonlibrary.mvp.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by COOTEK on 2017/8/17.
 */

public class TestFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.tv_test_download)
    TextView tvTestDownload;
    Unbinder unbinder;

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

    }

    @Override
    protected void initData() {


    }

    @Override
    protected void updateView() {

    }

    public static BaseFragment newInstance() {
        return new TestFragment();
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void loadMore() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tv_test_download)
    public void onViewClicked() {
        Intent intent = new Intent(getContext(), DownLoadActivity.class);
        startActivity(intent);
    }
}
