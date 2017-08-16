package com.example.cootek.newfastframe;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.mvp.BaseFragment;
import com.example.cootek.newfastframe.api.RankListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by COOTEK on 2017/8/15.
 */

public class RankFragment extends BaseFragment<RankListBean> {


    @BindView(R.id.srcv_fragment_rank_display)
    SuperRecyclerView display;
    @BindView(R.id.refresh_fragment_rank_refresh)
    SwipeRefreshLayout refresh;

    @Override
    public void updateData(RankListBean rankListBeen) {

    }

    @Override
    protected boolean isNeedHeadLayout() {
        return false;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
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

    }

    @Override
    protected void updateView() {

    }

}
