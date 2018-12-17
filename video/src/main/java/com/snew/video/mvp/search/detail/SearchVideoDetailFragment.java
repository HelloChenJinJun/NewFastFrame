package com.snew.video.mvp.search.detail;

import android.os.Bundle;

import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.cusotomview.ListViewDecoration;
import com.example.commonlibrary.cusotomview.swipe.CustomSwipeRefreshLayout;
import com.snew.video.R;
import com.snew.video.adapter.SearchVideoDetailListAdapter;
import com.snew.video.base.VideoBaseFragment;
import com.snew.video.bean.SearchVideoBean;
import com.snew.video.dagger.search.detail.DaggerSearchDetailComponent;
import com.snew.video.dagger.search.detail.SearchDetailModule;
import com.snew.video.util.VideoUtil;

import javax.inject.Inject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/14     21:02
 */
public class SearchVideoDetailFragment extends VideoBaseFragment<BaseBean, SearchVideoDetailPresenter> implements CustomSwipeRefreshLayout.OnRefreshListener {
    private SuperRecyclerView display;
    private CustomSwipeRefreshLayout refresh;
    private String content;

    @Inject
    SearchVideoDetailListAdapter mSearchVideoDetailAdapter;

    public static SearchVideoDetailFragment newInstance(String content) {
        Bundle bundle = new Bundle();
        bundle.putString(VideoUtil.DATA, content);
        SearchVideoDetailFragment searchVideoDetailFragment = new SearchVideoDetailFragment();
        searchVideoDetailFragment.setArguments(bundle);
        return searchVideoDetailFragment;
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
        return R.layout.fragment_search_video_detail;
    }

    @Override
    protected void initView() {
        display = findViewById(R.id.srcv_fragment_search_video_detail_display);
        refresh = findViewById(R.id.refresh_fragment_search_video_detail_refresh);
        refresh.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        DaggerSearchDetailComponent.builder().searchDetailModule(new SearchDetailModule(this))
                .videoComponent(getComponent())
                .build().inject(this);
        content = getArguments().getString(VideoUtil.DATA);
        display.setLayoutManager(new WrappedLinearLayoutManager(getContext()));
        display.addItemDecoration(new ListViewDecoration());
        display.setAdapter(mSearchVideoDetailAdapter);
    }

    @Override
    protected void updateView() {
        onRefresh();
    }


    @Override
    public void updateData(BaseBean baseBean) {
        if (baseBean.getType() == VideoUtil.BASE_TYPE_SEARCH_CONTENT) {
            SearchVideoBean searchVideoBean = (SearchVideoBean) baseBean.getData();
            //            mSearchVideoDetailAdapter.refreshData(searchVideoBean.getItem());
        }

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
//        presenter.search(content);
    }

    public void notifyDataChanged(String content) {
        this.content = content;
        onRefresh();
    }
}
