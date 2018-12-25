package com.example.cootek.newfastframe.mvp.rank;

import android.view.View;

import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.customview.swipe.CustomSwipeRefreshLayout;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.cootek.newfastframe.MusicApplication;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.adapter.RankAdapter;
import com.example.cootek.newfastframe.bean.RankListBean;
import com.example.cootek.newfastframe.dagger.rank.DaggerRankFragmentComponent;
import com.example.cootek.newfastframe.dagger.rank.RankFragmentModule;
import com.example.cootek.newfastframe.mvp.songlist.SongListFragment;
import com.example.cootek.newfastframe.util.MusicUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by COOTEK on 2017/8/15.
 */

public class RankFragment extends BaseFragment<RankListBean, RankPresenter> implements CustomSwipeRefreshLayout.OnRefreshListener {


    private SuperRecyclerView display;
    private CustomSwipeRefreshLayout refresh;
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
    protected boolean needStatusPadding() {
        return false;
    }

    @Override
    protected void initView() {
        display = findViewById(R.id.srcv_fragment_rank_display);
        refresh = findViewById(R.id.refresh_fragment_rank_refresh);
    }

    @Override
    protected void initData() {
        DaggerRankFragmentComponent.builder().mainComponent(MusicApplication.getMainComponent())
                .rankFragmentModule(new RankFragmentModule(this)).build().inject(this);
        typeList = new ArrayList<>();
        typeList.addAll(Arrays.asList(MusicUtil.RANK_TYPE_LIST));
        display.setLayoutManager(new WrappedLinearLayoutManager(getContext()));
        refresh.setOnRefreshListener(this);
        rankAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                addBackStackFragment(SongListFragment.newInstance(MusicUtil.FROM_RANK, rankAdapter.getData(position).getBillboard().getBillboard_type()));
            }
        });
        display.setAdapter(rankAdapter);
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
        for (Integer type :
                typeList) {
            presenter.getRankList(type, true);
        }
    }

    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        hideLoading();
        ToastUtils.showShortToast("网络加载失败，请重新刷新下");
    }


    @Override
    public void hideLoading() {
        super.hideLoading();
        refresh.setRefreshing(false);
    }

    public static RankFragment newInstance() {

        return new RankFragment();
    }
}
