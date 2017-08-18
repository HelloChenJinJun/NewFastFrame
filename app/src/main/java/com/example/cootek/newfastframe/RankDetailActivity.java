package com.example.cootek.newfastframe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.commonlibrary.baseadapter.EmptyLayout;
import com.example.commonlibrary.baseadapter.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.OnRefreshListener;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.mvp.BaseActivity;
import com.example.cootek.newfastframe.api.RankListBean;
import com.example.cootek.newfastframe.dagger.RankDetailModule;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * Created by COOTEK on 2017/8/16.
 */

public class RankDetailActivity extends BaseActivity<RankListBean, RankDetailPresenter> implements OnRefreshListener, OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.refresh_activity_rank_detail_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.srcv_activity_rank_detail_display)
    SuperRecyclerView display;

    @Inject
    RankDetailAdapter rankDetailAdapter;
    private ImageView headerBg;
    private ImageView headerImage;
    private TextView headName;
    private TextView headComment;
    private TextView headTime;
    private int type;
    private LoadMoreFooterView loadMoreFooterView;
    private LinearLayoutManager linearManager;


    @Override
    public void updateData(RankListBean rankListBean) {
        if (rankListBean.getSong_list() == null && !refreshLayout.isRefreshing()) {
            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
            linearManager.scrollToPosition(rankDetailAdapter.getItemCount() - 1);
        }
        updateHeaderView(rankListBean.getBillboard());
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
            rankDetailAdapter.clearAllData();
            rankDetailAdapter.getData().addAll(rankListBean.getSong_list());
            rankDetailAdapter.notifyDataSetChanged();
        } else {
            rankDetailAdapter.addData(rankListBean.getSong_list());
        }
    }

    private void updateHeaderView(RankListBean.BillboardBean bean) {
        Glide.with(this).load(bean.getPic_s640()).bitmapTransform(new BlurTransformation(this)).into(headerBg);
        Glide.with(this).load(bean.getPic_s192()).centerCrop().into(headerImage);
        headComment.setText(bean.getComment());
        headName.setText(bean.getName());
        headTime.setText(bean.getUpdate_date());
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
        return R.layout.activity_rank_detail;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        DaggerRankDetailActivityComponent.builder().mainComponent(MainApplication.getMainComponent())
                .rankDetailModule(new RankDetailModule(this)).build().inject(this);
        type = getIntent().getIntExtra("type", -1);
        display.setLayoutManager(linearManager = new LinearLayoutManager(this));
        rankDetailAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Toast.makeText(RankDetailActivity.this, "这里播放音乐", Toast.LENGTH_SHORT).show();
            }
        });
        refreshLayout.setOnRefreshListener(this);
        display.setIAdapter(rankDetailAdapter);
        display.addHeaderView(getHeaderView());
        display.setOnLoadMoreListener(this);
        display.setLoadMoreFooterView(loadMoreFooterView = new LoadMoreFooterView(this));
        presenter.getRankDetailInfo(type, true, true);
    }

    private View getHeaderView() {
        View headerView = LayoutInflater.from(this).inflate(R.layout.view_activity_rank_detail_header_view, null);
        headerBg = (ImageView) headerView.findViewById(R.id.iv_view_activity_rank_detail_header_view_bg);
        headerImage = (ImageView) headerView.findViewById(R.id.iv_view_activity_rank_detail_header_view_image);
        headTime = (TextView) headerView.findViewById(R.id.tv_view_activity_rank_detail_header_view_time);
        headComment = (TextView) headerView.findViewById(R.id.tv_view_activity_rank_detail_header_view_comment);
        headName = (TextView) headerView.findViewById(R.id.tv_view_activity_rank_detail_header_view_name);
        return headerView;
    }

    public static void start(Context context, int type) {
        Intent intent = new Intent(context, RankDetailActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    public void onRefresh() {
        presenter.getRankDetailInfo(type, true, false);
    }


    @Override
    public void loadMore() {
        presenter.getRankDetailInfo(type, false, false);
    }


    @Override
    public void hideLoading() {
        super.hideLoading();
        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
    }

    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        super.showError(errorMsg, listener);
        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.ERROR);
    }
}
