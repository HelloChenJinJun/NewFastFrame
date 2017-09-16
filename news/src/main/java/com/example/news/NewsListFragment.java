package com.example.news;

import android.content.Context;
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
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.imageloader.glide.GlideImageLoaderConfig;
import com.example.news.bean.NewListBean;
import com.example.news.dagger.news.DaggerNewsListComponent;
import com.example.news.dagger.news.NewsListModule;
import com.example.news.mvp.NewsListPresenter;
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
    private LoadMoreFooterView loadMoreFooterView;


    @Override
    public void updateData(NewListBean o) {
        if (o != null && o.getBannerBeanList() != null) {
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
        } else if (o != null && o.getNewsItemList() != null) {
            if (refresh.isRefreshing()) {
                newsListAdapter.clearAllData();
                newsListAdapter.notifyDataSetChanged();
                newsListAdapter.addData(o.getNewsItemList());
            } else {
                newsListAdapter.addData(o.getNewsItemList());
                if (newsListAdapter.getData().size() == 0) {
                    loadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
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
    protected int getContentLayout() {
        return R.layout.fragment_news_list;
    }

    @Override
    protected void initView() {
        display = (SuperRecyclerView) findViewById(R.id.srcv_fragment_news_list_display);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_fragment_news_list_refresh);
        display.addHeaderView(getHeaderView());
    }

    private View getHeaderView() {
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.view_fragment_news_list_header, null);
        banner = (Banner) headerView.findViewById(R.id.bn_view_fragment_news_list_header_banner);
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
    }

    @Override
    protected void initData() {
        DaggerNewsListComponent.builder().newsListModule(new NewsListModule(this))
                .newsComponent(NewsApplication.getNewsComponent())
                .build().inject(this);
        refresh.setOnRefreshListener(this);
        display.setLayoutManager(new WrappedLinearLayoutManager(getActivity()));
        loadMoreFooterView = new LoadMoreFooterView(getContext());
        display.setLoadMoreFooterView(loadMoreFooterView);
        display.setOnLoadMoreListener(this);
        display.setAdapter(newsListAdapter);
    }

    @Override
    protected void updateView() {
        presenter.getCugNewsData(true, true);
    }

    public static NewsListFragment newInstance() {
        return new NewsListFragment();
    }

    @Override
    public void onRefresh() {
        presenter.getCugNewsData(false, true);
    }

    @Override
    public void loadMore() {
        presenter.getCugNewsData(false, false);
    }


    @Override
    public void hideLoading() {
        if (newsListAdapter.getData().size()>0) {
            super.hideLoading();
        }else {
            showEmptyView();
        }
        refresh.setRefreshing(false);
    }

    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        super.showError(errorMsg, listener);
        refresh.setRefreshing(false);
    }
}
