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
import com.example.cootek.newfastframe.MusicManager;
import com.example.cootek.newfastframe.MusicService;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.VideoApplication;
import com.example.cootek.newfastframe.adapter.SongListAdapter;
import com.example.cootek.newfastframe.dagger.DaggerSongListActivityComponent;
import com.example.cootek.newfastframe.dagger.SongListModule;
import com.example.cootek.newfastframe.mvp.SongListPresenter;
import com.example.cootek.newfastframe.api.DownLoadMusicBean;
import com.example.cootek.newfastframe.api.RankListBean;
import com.example.cootek.newfastframe.util.MusicUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * Created by COOTEK on 2017/8/16.
 */

public class SongListActivity extends BaseActivity<Object, SongListPresenter> implements OnRefreshListener, OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout refreshLayout;
    SuperRecyclerView display;

    @Inject
    SongListAdapter songListAdapter;
    private ImageView headerBg;
    private ImageView headerImage;
    private TextView headName;
    private int type;
    private LoadMoreFooterView loadMoreFooterView;
    private LinearLayoutManager linearManager;
    private String listId;


    @Override
    public void updateData(Object rankListBean) {
        if (rankListBean instanceof RankListBean) {
            RankListBean bean = ((RankListBean) rankListBean);
            if (bean.getSong_list() == null && !refreshLayout.isRefreshing()) {
                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                linearManager.scrollToPosition(songListAdapter.getItemCount() - 1);
            }
            updateHeaderView(bean.getBillboard());
        } else if (rankListBean instanceof DownLoadMusicBean) {
            DownLoadMusicBean bean = ((DownLoadMusicBean) rankListBean);
            if (refreshLayout.isRefreshing()) {
                songListAdapter.clearAllData();
                songListAdapter.getData().add(bean);
                songListAdapter.notifyDataSetChanged();
            } else {
                songListAdapter.addData(bean);
            }
        }
    }

    private void updateHeaderView(RankListBean.BillboardBean bean) {
        Glide.with(this).load(bean.getPic_s640()).bitmapTransform(new BlurTransformation(this)).into(headerBg);
        Glide.with(this).load(bean.getPic_s192()).centerCrop().into(headerImage);
        headName.setText(bean.getName());
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
        return R.layout.activity_song_list;
    }

    @Override
    protected void initView() {
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_activity_song_list_refresh);
        display = (SuperRecyclerView) findViewById(R.id.srcv_activity_song_list_display);
    }

    @Override
    protected void initData() {
        DaggerSongListActivityComponent.builder().mainComponent(VideoApplication.getMainComponent())
                .songListModule(new SongListModule(this)).build().inject(this);
        type = getIntent().getIntExtra("type", -1);
        display.setLayoutManager(linearManager = new LinearLayoutManager(this));

        int from = getIntent().getIntExtra(MusicUtil.FROM, 0);


        if (from == MusicUtil.FROM_SONG_MENU) {
            listId = getIntent().getStringExtra(MusicUtil.LIST_ID);
        }


        songListAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                DownLoadMusicBean downLoadMusicBean = songListAdapter.getData(position);
                List<MusicPlayBean> list = new ArrayList<>();
                for (DownLoadMusicBean bean :
                        songListAdapter.getData()) {
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
                Toast.makeText(SongListActivity.this, "这里播放音乐", Toast.LENGTH_SHORT).show();
            }
        });
        refreshLayout.setOnRefreshListener(this);
        display.setAdapter(songListAdapter);
        display.addHeaderView(getHeaderView());
        display.setOnLoadMoreListener(this);
        display.setLoadMoreFooterView(loadMoreFooterView = new LoadMoreFooterView(this));
        presenter.getRankDetailInfo(type, true, true);
    }

    private View getHeaderView() {
        View headerView = LayoutInflater.from(this).inflate(R.layout.view_activity_song_list_header_view, null);
        headerBg = (ImageView) headerView.findViewById(R.id.iv_view_activity_song_list_header_view_bg);
        headerImage = (ImageView) headerView.findViewById(R.id.iv_view_activity_song_list_header_view_image);
        headName = (TextView) headerView.findViewById(R.id.tv_view_activity_song_list_header_view_title);
        return headerView;
    }

    public static void start(Context context, int type, int from) {
        Intent intent = new Intent(context, SongListActivity.class);
        intent.putExtra(MusicUtil.FROM, from);
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
