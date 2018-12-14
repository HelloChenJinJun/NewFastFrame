package com.snew.video.mvp.video;

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
import com.snew.video.dagger.video.DaggerVideoFragmentComponent;
import com.snew.video.dagger.video.VideoFragmentModule;
import com.snew.video.util.VideoUtil;

import java.util.List;

import javax.inject.Inject;


/**
 * 项目名称:    Update
 * 创建人:      陈锦军
 * 创建时间:    2018/11/27     15:41
 */
public class VideoListFragment extends VideoBaseFragment<List<VideoBean>, VideoPresenter> implements CustomSwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {

    private CustomSwipeRefreshLayout refresh;
    private SuperRecyclerView display;


    @Inject
    VideoAdapter mVideoAdapter;
    private String type;

    public static VideoListFragment newInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(VideoUtil.VIDEO_TYPE, type);
        VideoListFragment videoListFragment = new VideoListFragment();
        videoListFragment.setArguments(bundle);
        return videoListFragment;
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
        return R.layout.fragment_list_video;
    }

    @Override
    protected void initView() {
        refresh = findViewById(R.id.refresh_fragment_list_video_refresh);
        display = findViewById(R.id.srcv_fragment_list_video_display);
        refresh.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        DaggerVideoFragmentComponent.builder().videoComponent(getComponent()).videoFragmentModule(new VideoFragmentModule(this))
                .build().inject(this);
        type = getArguments().getString(VideoUtil.VIDEO_TYPE);
        display.setLayoutManager(new WrappedLinearLayoutManager(getContext()));
        display.addItemDecoration(new ListViewDecoration());
        display.setAdapter(mVideoAdapter);
        display.setLoadMoreFooterView(new LoadMoreFooterView(getContext()));
        display.setOnLoadMoreListener(this);
        mVideoAdapter.setOnItemClickListener((view, url) -> {
            if (url.contains(VideoUtil.BASE_VIDEO_URL)) {
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
