package com.example.news.mvp.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.cusotomview.ListViewDecoration;
import com.example.commonlibrary.imageloader.glide.GlideImageLoaderConfig;
import com.example.news.NewsApplication;
import com.example.news.NewsContentActivity;
import com.example.news.adapter.NewsListAdapter;
import com.example.news.R;
import com.example.news.bean.NewListBean;
import com.example.news.dagger.news.DaggerNewsListComponent;
import com.example.news.dagger.news.NewsListModule;
import com.example.news.util.NewsUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoaderInterface;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/16      16:26
 * QQ:             1981367757
 */

public class NewsListFragment extends BaseFragment<NewListBean, NewsListPresenter> implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
    private Banner banner;
    private SwipeRefreshLayout refresh;
    private SuperRecyclerView display;
    @Inject
    NewsListAdapter newsListAdapter;
    private String url;


    private List<NewListBean.BannerBean> bannerList;


    @Override
    public void updateData(NewListBean o) {
        if (o != null && o.getBannerBeanList() != null) {
            bannerList = o.getBannerBeanList();
            List<String> titleList = new ArrayList<>();
            List<String> imageList = new ArrayList<>();
            for (NewListBean.BannerBean bean :
                    o.getBannerBeanList()) {
                titleList.add(bean.getTitle());
                imageList.add(bean.getThumb());
            }
            banner.setImages(imageList);
            banner.setBannerTitles(titleList);
            banner.start();
        } else {
            if (refresh.isRefreshing()) {
                newsListAdapter.refreshData(o != null ? o.getNewsItemList() : null);
            } else {
                if (o == null || o.getNewsItemList() == null || o.getNewsItemList().size() == 0) {
                    display.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
                }else {
                    newsListAdapter.addData(o.getNewsItemList());
                }
            }
        }
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
    protected boolean needStatusPadding() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_news_list;
    }

    @Override
    protected void initView() {
        display = (SuperRecyclerView) findViewById(R.id.srcv_fragment_news_list_display);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_fragment_news_list_refresh);
    }

    private View getHeaderView() {
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.view_fragment_news_list_header, null);
        banner = headerView.findViewById(R.id.bn_view_fragment_news_list_header_banner);
        initBannerConfig();
        return headerView;
    }

    private void initBannerConfig() {
        banner.setBannerAnimation(Transformer.ZoomOutSlide);
        banner.setImageLoader(new ImageLoaderInterface() {
            @Override
            public void displayImage(Context context, Object path, View imageView) {
                BaseApplication.getAppComponent().getImageLoader().loadImage(context,
                        new GlideImageLoaderConfig.Builder().url(((String) path)).imageView(((ImageView) imageView)).build());
            }

            @Override
            public View createImageView(Context context) {
                return new ImageView(context);
            }
        });
        banner.isAutoPlay(true);
        banner.setDelayTime(3000);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.setOnBannerListener(position -> {
            if (bannerList != null && bannerList.size() > position) {
                Intent intent = new Intent(getContext(), NewsContentActivity.class);
                intent.putExtra(NewsUtil.URL, bannerList.get(position).getContentUrl());
                intent.putExtra(NewsUtil.TITLE, bannerList.get(position).getTitle());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            url = getArguments().getString(NewsUtil.URL);
        }
        DaggerNewsListComponent.builder().newsListModule(new NewsListModule(this))
                .newsComponent(NewsApplication.getNewsComponent())
                .build().inject(this);
        if (NewsUtil.CUG_NEWS.equals(url) || NewsUtil.DK_INDEX_URL.equals(url)
                ||NewsUtil.CUG_VOICE_INDEX.equals(url)
                || NewsUtil.JG_INDEX_URL.equals(url)
                || NewsUtil.GG_INDEX_URL.equals(url)
                || NewsUtil.JSJ_INDEX_URL.equals(url)
                || NewsUtil.WY_INDEX_URL.equals(url)
                || NewsUtil.DY_INDEX_URL.equals(url)
                || NewsUtil.XY_INDEX_URL.equals(url)
                || NewsUtil.ZDH_INDEX_URL.equals(url)
                || NewsUtil.ZY_INDEX_URL.equals(url)
                || NewsUtil.CH_INDEX_URL.equals(url)
                || NewsUtil.GC_INDEX_URL.equals(url)
                || NewsUtil.HJ_INDEX_URL.equals(url)
                || NewsUtil.DWK_INDEX_URL.equals(url)
                || NewsUtil.JD_INDEX_URL.equals(url)
                || NewsUtil.HY_INDEX_URL.equals(url)
                || NewsUtil.SL_INDEX_URL.equals(url)
                || NewsUtil.MY_INDEX_URL.equals(url)) {
            display.addHeaderView(getHeaderView());
        }
        refresh.setOnRefreshListener(this);
        display.setLayoutManager(new WrappedLinearLayoutManager(getActivity()));
        if (!url.startsWith(NewsUtil.WY_BASE_URL)) {
            display.setLoadMoreFooterView(new LoadMoreFooterView(getContext()));
            display.setOnLoadMoreListener(this);
        }
        display.addItemDecoration(new ListViewDecoration());
        display.setAdapter(newsListAdapter);
        newsListAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent intent = new Intent(getContext(), NewsContentActivity.class);
                NewListBean.NewsItem newsItem = newsListAdapter.getData(position);
                intent.putExtra(NewsUtil.URL, newsItem.getContentUrl());
                intent.putExtra(NewsUtil.TITLE, newsItem.getTitle());
                startActivity(intent);
            }
        });
    }


    @Override
    protected void updateView() {
        presenter.getCugNewsData(true, true, url);
    }

    public static NewsListFragment newInstance(String url) {
        NewsListFragment newsListFragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(NewsUtil.URL, url);
        newsListFragment.setArguments(bundle);
        return newsListFragment;
    }

    @Override
    public void onRefresh() {
        presenter.getCugNewsData(false, true, url);
    }

    @Override
    public void loadMore() {
        presenter.getCugNewsData(false, false, url);
    }


    @Override
    public void showLoading(String loadingMsg) {
        refresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
        }
        if (newsListAdapter.getData().size() == 0) {
            showEmptyView();
        } else {
            super.hideLoading();
        }
    }

    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
            super.showError(errorMsg, listener);
        } else {
            display.setLoadMoreStatus(LoadMoreFooterView.Status.ERROR);
        }
    }
}
