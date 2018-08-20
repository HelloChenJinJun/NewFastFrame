package com.example.cootek.newfastframe.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.VideoApplication;
import com.example.cootek.newfastframe.adapter.RankAdapter;
import com.example.cootek.newfastframe.bean.RankListBean;
import com.example.cootek.newfastframe.dagger.rank.DaggerRankFragmentComponent;
import com.example.cootek.newfastframe.dagger.rank.RankFragmentModule;
import com.example.cootek.newfastframe.mvp.rank.RankPresenter;
import com.example.cootek.newfastframe.ui.SongListActivity;
import com.example.cootek.newfastframe.util.MusicUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by COOTEK on 2017/8/15.
 */

public class RankFragment extends BaseFragment<RankListBean, RankPresenter> implements SwipeRefreshLayout.OnRefreshListener {


    private SuperRecyclerView display;
    private SwipeRefreshLayout refresh;
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
        display = (SuperRecyclerView) findViewById(R.id.srcv_fragment_rank_display);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_fragment_rank_refresh);
    }

    @Override
    protected void initData() {
        DaggerRankFragmentComponent.builder().mainComponent(VideoApplication.getMainComponent())
                .rankFragmentModule(new RankFragmentModule(this)).build().inject(this);
        typeList = new ArrayList<>();
        typeList.addAll(Arrays.asList(MusicUtil.RANK_TYPE_LIST));
        display.setLayoutManager(new LinearLayoutManager(getContext()));
        refresh.setOnRefreshListener(this);
        rankAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent intent = new Intent(getContext(), SongListActivity.class);
                intent.putExtra(MusicUtil.FROM, MusicUtil.FROM_RANK);
                intent.putExtra(MusicUtil.RANK_TYPE, Integer.parseInt(rankAdapter.getData(position).getBillboard().getBillboard_type()));
                startActivity(intent);
            }
        });
//        display.addHeaderView(getHeaderView());
        display.setAdapter(rankAdapter);
    }

//    private View getHeaderView() {
//        return LayoutInflater.from(getContext()).inflate(R.layout.view_activity_song_list_header_view, null);
//    }

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
