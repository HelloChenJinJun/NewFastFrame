package com.example.cootek.newfastframe;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.example.commonlibrary.baseadapter.OnRefreshListener;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.mvp.BaseFragment;
import com.example.cootek.newfastframe.api.RankListBean;
import com.example.cootek.newfastframe.dagger.DaggerRankFragmentComponent;
import com.example.cootek.newfastframe.dagger.RankFragmentModule;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by COOTEK on 2017/8/15.
 */

public class RankFragment extends BaseFragment<RankListBean, RankPresenter> implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.srcv_fragment_rank_display)
    SuperRecyclerView display;
    @BindView(R.id.refresh_fragment_rank_refresh)
    SwipeRefreshLayout refresh;
    @Inject
    RankAdapter rankAdapter;
    private List<Integer> typeList;

    @Override
    public void updateData(RankListBean rankListBeen) {
        rankAdapter.addData(rankListBeen);
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
        return R.layout.fragment_rank;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        DaggerRankFragmentComponent.builder().mainComponent(MainApplication.getMainComponent())
                .rankFragmentModule(new RankFragmentModule(this)).build().inject(this);
        typeList = new ArrayList<>();
        typeList.addAll(Arrays.asList(MusicUtil.RANK_TYPE_LIST));
        display.setLayoutManager(new LinearLayoutManager(getContext()));
        refresh.setOnRefreshListener(this);
        rankAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                RankDetailActivity.start(getContext(), Integer.parseInt(rankAdapter.getData(position).getBillboard().getBillboard_type()));
            }
        });
        display.addHeaderView(getHeaderView());
        display.setAdapter(rankAdapter);
    }

    private View getHeaderView() {
        return  LayoutInflater.from(getContext()).inflate(R.layout.view_activity_rank_detail_header_view, null);
    }

    @Override
    protected void updateView() {
        for (Integer type :
                typeList) {
            presenter.getRankList(type, false);
        }
    }

    @Override
    public void onRefresh() {
//        for (Integer type :
//                typeList) {
//            presenter.getRankList(type, true);
//        }
        refresh.setRefreshing(false);
    }

    public static RankFragment newInstance() {
        return new RankFragment();
    }
}
