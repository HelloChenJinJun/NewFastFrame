package com.example.news;

import android.support.v4.widget.SwipeRefreshLayout;

import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.news.mvp.NewsListPresenter;
import com.youth.banner.Banner;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/16      16:26
 * QQ:             1981367757
 */

public class NewsListFragment extends BaseFragment<Object,NewsListPresenter> implements SwipeRefreshLayout.OnRefreshListener {
    private Banner banner;
    private SwipeRefreshLayout refresh;
    private SuperRecyclerView display;



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
        return R.layout.fragment_news_list;
    }

    @Override
    protected void initView() {
        banner= (Banner) findViewById(R.id.bn_fragment_news_list_banner);
        display= (SuperRecyclerView) findViewById(R.id.srcv_fragment_news_list_display);
        refresh= (SwipeRefreshLayout) findViewById(R.id.refresh_fragment_news_list_refresh);

    }

    @Override
    protected void initData() {
        refresh.setOnRefreshListener(this);
        display.setLayoutManager(new WrappedLinearLayoutManager(getActivity()));
    }

    @Override
    protected void updateView() {

    }

    public static NewsListFragment newInstance() {
        return new NewsListFragment();
    }

    @Override
    public void onRefresh() {

    }
}
