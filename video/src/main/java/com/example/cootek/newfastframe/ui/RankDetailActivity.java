package com.example.cootek.newfastframe.ui;

import android.content.Context;
import android.content.Intent;
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
import com.example.commonlibrary.bean.MusicPlayBean;
import com.example.commonlibrary.mvp.BaseActivity;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.MainApplication;
import com.example.cootek.newfastframe.MusicManager;
import com.example.cootek.newfastframe.MusicService;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.adapter.RankDetailAdapter;
import com.example.cootek.newfastframe.dagger.DaggerRankDetailActivityComponent;
import com.example.cootek.newfastframe.mvp.RankDetailPresenter;
import com.example.cootek.newfastframe.api.DownLoadMusicBean;
import com.example.cootek.newfastframe.api.RankListBean;
import com.example.cootek.newfastframe.dagger.RankDetailModule;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * Created by COOTEK on 2017/8/16.
 */

public class RankDetailActivity extends BaseActivity<Object, RankDetailPresenter> implements OnRefreshListener, OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout refreshLayout;
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
    public void updateData(Object rankListBean) {
        if (rankListBean instanceof RankListBean) {
            RankListBean bean = ((RankListBean) rankListBean);
            if (bean.getSong_list() == null && !refreshLayout.isRefreshing()) {
                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                linearManager.scrollToPosition(rankDetailAdapter.getItemCount() - 1);
            }
            updateHeaderView(bean.getBillboard());
        } else if (rankListBean instanceof DownLoadMusicBean) {
            DownLoadMusicBean bean = ((DownLoadMusicBean) rankListBean);
            if (refreshLayout.isRefreshing()) {
                rankDetailAdapter.clearAllData();
                rankDetailAdapter.getData().add(bean);
                rankDetailAdapter.notifyDataSetChanged();
            } else {
                rankDetailAdapter.addData(bean);
            }
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
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_activity_rank_detail_refresh);
        display = (SuperRecyclerView) findViewById(R.id.srcv_activity_rank_detail_display);
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
                DownLoadMusicBean downLoadMusicBean = rankDetailAdapter.getData(position);
                List<MusicPlayBean> list = new ArrayList<>();
                for (DownLoadMusicBean bean :
                        rankDetailAdapter.getData()) {
                    MusicPlayBean playBean = new MusicPlayBean();
                    playBean.setLocal(false);
                    playBean.setSongId(Long.parseLong(bean.getSonginfo().getSong_id()));
                    playBean.setArtistId(bean.getSonginfo().getArtist_id());
                    playBean.setAlbumId(Long.parseLong(bean.getSonginfo().getAlbum_id()));
                    playBean.setAlbumName(bean.getSonginfo().getAlbum_title());
                    playBean.setSongName(bean.getSonginfo().getTitle());
                    playBean.setArtistName(bean.getSonginfo().getAuthor());
                    playBean.setAlbumUrl(bean.getSonginfo().getPic_radio());
                    playBean.setSongUrl(bean.getBitrate().getFile_link());
                    playBean.setLrcUrl(bean.getSonginfo().getLrclink());
                    list.add(playBean);
                }
                if (list.size() > 0) {
                    CommonLogger.e("播放地址:" + downLoadMusicBean.getBitrate().getFile_link());
                    MusicManager.getInstance().play(list, position, MusicService.MODE_NORMAL);
                }
                Toast.makeText(RankDetailActivity.this, "这里播放音乐", Toast.LENGTH_SHORT).show();
            }
        });
        refreshLayout.setOnRefreshListener(this);
        display.setAdapter(rankDetailAdapter);
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
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        super.showError(errorMsg, listener);
        refreshLayout.setRefreshing(false);
        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.ERROR);
    }
}
