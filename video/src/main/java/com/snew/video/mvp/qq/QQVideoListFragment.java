package com.snew.video.mvp.qq;

import android.os.Bundle;

import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.cusotomview.ListViewDecoration;
import com.example.commonlibrary.cusotomview.swipe.CustomSwipeRefreshLayout;
import com.example.commonlibrary.manager.video.ListVideoManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.snew.video.R;
import com.snew.video.adapter.VideoAdapter;
import com.snew.video.base.VideoBaseFragment;
import com.snew.video.bean.VideoBean;
import com.snew.video.dagger.qq.DaggerQQVideoListComponent;
import com.snew.video.dagger.qq.QQVideoListModule;
import com.snew.video.util.VideoUtil;

import java.util.List;

import javax.inject.Inject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/12     15:58
 */
public class QQVideoListFragment extends VideoBaseFragment<List<VideoBean>, QQVideoListPresenter> implements CustomSwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {


    private CustomSwipeRefreshLayout refresh;
    private SuperRecyclerView display;
    private int type;

    @Inject
    VideoAdapter mVideoAdapter;

    public static QQVideoListFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(VideoUtil.VIDEO_TYPE, type);
        QQVideoListFragment qqVideoListFragment = new QQVideoListFragment();
        qqVideoListFragment.setArguments(bundle);
        return qqVideoListFragment;
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
    protected boolean needStatusPadding() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_qq_video_list;
    }

    @Override
    protected void initView() {
        refresh = findViewById(R.id.refresh_fragment_qq_video_list_refresh);
        display = findViewById(R.id.srcv_fragment_qq_video_list_display);
        refresh.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        DaggerQQVideoListComponent.builder().qQVideoListModule(new QQVideoListModule(this))
                .videoComponent(getComponent()).build().inject(this);
        type = getArguments().getInt(VideoUtil.VIDEO_TYPE);
        display.setLayoutManager(new WrappedLinearLayoutManager(getContext()));
        display.addItemDecoration(new ListViewDecoration());
        display.setAdapter(mVideoAdapter);
        display.setLoadMoreFooterView(new LoadMoreFooterView(getContext()));
        display.setOnLoadMoreListener(this);
        mVideoAdapter.setOnItemClickListener((view, url) -> {
            if (!url.endsWith(VideoUtil.M3U8) && !url.contains(".mp4")) {
                presenter.getDetailData(url);
                return false;
            } else {
                return true;
            }
        });
    }

    @Override
    protected void updateView() {
        onRefresh();
    }

    @Override
    public void updateData(List<VideoBean> baseBean) {
        if (refresh.isRefreshing()) {
            mVideoAdapter.refreshData(baseBean);
        } else {
            mVideoAdapter.addData(baseBean);
        }
    }


    @Override
    public void showLoading(String loadingMsg) {
        super.showLoading(loadingMsg);
        refresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        refresh.setRefreshing(false);
    }

    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        super.showError(errorMsg, listener);
        refresh.setRefreshing(false);
    }


    @Override
    public void onRefresh() {
        CommonLogger.e("onRefresh");
        presenter.getData(true, type);
    }

    @Override
    public void loadMore() {
        CommonLogger.e("loadMore");
        presenter.getData(false, type);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ListVideoManager.getInstance().release();
    }
}
