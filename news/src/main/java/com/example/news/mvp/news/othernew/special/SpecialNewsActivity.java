package com.example.news.mvp.news.othernew.special;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.imageloader.glide.GlideImageLoaderConfig;
import com.example.news.NewsApplication;
import com.example.news.R;
import com.example.news.adapter.SpecialNewsAdapter;
import com.example.news.bean.SpecialNewsBean;
import com.example.news.dagger.news.othernews.special.DaggerSpecialNewsComponent;
import com.example.news.dagger.news.othernews.special.SpecialNewsModule;
import com.example.news.util.NewsUtil;

import java.util.List;

import javax.inject.Inject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/25      18:43
 * QQ:             1981367757
 */

public class SpecialNewsActivity extends BaseActivity<List<SpecialNewsBean>,SpecialNewsPresenter> implements ISpecialNewsView<List<SpecialNewsBean>>{

    private SuperRecyclerView display;
    @Inject
    SpecialNewsAdapter specialNewsAdapter;
    private String specialId;




    @Override
    public void updateData(List<SpecialNewsBean> list) {
        specialNewsAdapter.addData(list);
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
        return R.layout.activity_special_news;
    }

    @Override
    protected void initView() {
            display= (SuperRecyclerView) findViewById(R.id.srcv_activity_special_news_display);
    }

    @Override
    protected void initData() {
        DaggerSpecialNewsComponent.builder()
                .specialNewsModule(new SpecialNewsModule(this))
                .newsComponent(NewsApplication.getNewsComponent())
                .build().inject(this);
        specialId=getIntent().getStringExtra(NewsUtil.SPECIAL_ID);
        display.setLayoutManager(new WrappedLinearLayoutManager(this));
        display.addHeaderView(getHeaderView());
        display.setAdapter(specialNewsAdapter);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                presenter.getSpecialNewsData(specialId);
            }
        });
        ToolBarOption toolBarOption=new ToolBarOption();
        toolBarOption.setTitle(getIntent().getStringExtra(NewsUtil.TITLE));
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
    }

    private ImageView banner;
    private View getHeaderView() {
        View headerView= LayoutInflater.from(this)
                .inflate(R.layout.view_activity_special_news_header,null);
        banner= (ImageView) headerView.findViewById(R.id.iv_view_activity_special_news_header_banner);
        return headerView;
    }

    public static void start(Context context, String skipID, String title) {
        Intent intent=new Intent(context,SpecialNewsActivity.class);
        intent.putExtra(NewsUtil.SPECIAL_ID,skipID);
        intent.putExtra(NewsUtil.TITLE,title);
        context.startActivity(intent);
    }

    @Override
    public void updateBanner(String url) {
        BaseApplication
                .getAppComponent()
                .getImageLoader()
                .loadImage(this,new GlideImageLoaderConfig.Builder()
                .imageView(banner)
                .url(url).cacheStrategy(GlideImageLoaderConfig.CACHE_RESULT)
                .build());
    }
}
