package com.example.cootek.newfastframe.mvp.recent;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.decoration.ListViewDecoration;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.customview.ToolBarOption;
import com.example.commonlibrary.customview.swipe.CustomSwipeRefreshLayout;
import com.example.cootek.newfastframe.MusicManager;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.adapter.RecentPlayListAdapter;
import com.example.cootek.newfastframe.base.MusicBaseFragment;
import com.example.cootek.newfastframe.dagger.recent.DaggerRecentPlayListComponent;
import com.example.cootek.newfastframe.dagger.recent.RecentPlayListModule;
import com.example.cootek.newfastframe.util.MusicUtil;

import java.util.List;

import javax.inject.Inject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/11     11:43
 */
public class RecentPlayListFragment extends MusicBaseFragment<List<MusicPlayBean>, RecentPlayListPresenter> implements CustomSwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {

    private CustomSwipeRefreshLayout refresh;
    private SuperRecyclerView display;


    private int from;
    @Inject
    RecentPlayListAdapter mRecentPlayListAdapter;

    public static RecentPlayListFragment newInstance(int from) {
        Bundle bundle = new Bundle();
        bundle.putInt(MusicUtil.FROM, from);
        RecentPlayListFragment recentPlayListFragment = new RecentPlayListFragment();
        recentPlayListFragment.setArguments(bundle);
        return recentPlayListFragment;
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
        return R.layout.fragment_recent_play_list;
    }

    @Override
    protected void initView() {
        refresh = findViewById(R.id.refresh_fragment_recent_play_list_refresh);
        display = findViewById(R.id.srcv_fragment_recent_play_list_display);
        refresh.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        from = getArguments().getInt(MusicUtil.FROM);
        if (from == MusicUtil.FROM_RECENT) {
            display.setLoadMoreFooterView(new LoadMoreFooterView(getContext()));
            display.setOnLoadMoreListener(this);
        }
        DaggerRecentPlayListComponent.builder().mainComponent(getMainComponent())
                .recentPlayListModule(new RecentPlayListModule(this))
                .build().inject(this);
        display.setLayoutManager(new WrappedLinearLayoutManager(getContext()));
        display.addItemDecoration(new ListViewDecoration());
        display.setAdapter(mRecentPlayListAdapter);
        mRecentPlayListAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                MusicManager.getInstance().play(mRecentPlayListAdapter.getData(), position, 0);
            }
        });
        ToolBarOption toolBarOption = new ToolBarOption();
        if (from == MusicUtil.FROM_RECENT) {
            toolBarOption.setTitle("最近播放列表");
        } else if (from == MusicUtil.FROM_LOCAL) {
            toolBarOption.setTitle("本地音乐");
        }
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
        back.setOnClickListener(v -> getActivity().onBackPressed());
        root.setBackgroundColor(Color.parseColor("#3C5F78"));
    }

    @Override
    protected void updateView() {
        onRefresh();
    }


    @Override
    public void showLoading(String loadingMsg) {
        super.showLoading(loadingMsg);
        refresh.setRefreshing(true);
    }

    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        super.showError(errorMsg, listener);
        refresh.setRefreshing(false);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        refresh.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        presenter.getData(true, from);
    }

    @Override
    public void updateData(List<MusicPlayBean> musicPlayBeans) {
        if (refresh.isRefreshing()) {
            mRecentPlayListAdapter.refreshData(musicPlayBeans);
        } else {
            mRecentPlayListAdapter.addData(musicPlayBeans);
        }
    }

    @Override
    public void loadMore() {
        presenter.getData(false, from);
    }
}
