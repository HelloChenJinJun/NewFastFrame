package com.example.cootek.newfastframe.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.refresh.OnRefreshListener;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.bean.music.SingerListBean;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.MusicManager;
import com.example.cootek.newfastframe.MusicService;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.VideoApplication;
import com.example.cootek.newfastframe.adapter.SongListAdapter;
import com.example.cootek.newfastframe.bean.AlbumBean;
import com.example.cootek.newfastframe.bean.SongMenuBean;
import com.example.cootek.newfastframe.dagger.songlist.DaggerSongListActivityComponent;
import com.example.cootek.newfastframe.dagger.songlist.SongListModule;
import com.example.cootek.newfastframe.mvp.songlist.SongListPresenter;
import com.example.cootek.newfastframe.bean.DownLoadMusicBean;
import com.example.cootek.newfastframe.bean.RankListBean;
import com.example.cootek.newfastframe.util.MusicUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * Created by COOTEK on 2017/8/16.
 */

public class SongListActivity extends MusicBaseActivity<Object, SongListPresenter> implements OnRefreshListener, OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

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
    private int from;
    private String albumId;
    private String tingId;


    @Override
    public void updateData(Object object) {
        if (object instanceof RankListBean) {
            RankListBean bean = ((RankListBean) object);
            if ((bean.getSong_list() == null || bean.getSong_list().size() == 0) && !refreshLayout.isRefreshing()) {
                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                linearManager.scrollToPosition(songListAdapter.getItemCount() - 1);
            }
            updateHeaderView(bean.getBillboard().getPic_s260(), bean.getBillboard().getName());
        } else if (object instanceof DownLoadMusicBean) {
            DownLoadMusicBean bean = ((DownLoadMusicBean) object);
            if (bean.getSonginfo() == null) {
                if (loadMoreFooterView.getStatus() == LoadMoreFooterView.Status.LOADING) {
                    loadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                }
            }
            if (refreshLayout.isRefreshing()) {
                songListAdapter.clearAllData();
                songListAdapter.getData().add(bean);
                songListAdapter.notifyDataSetChanged();
            } else {
                songListAdapter.addData(bean);
            }
        } else if (object instanceof SongMenuBean) {
            SongMenuBean songMenuBean = (SongMenuBean) object;
            if ((songMenuBean.getContent() == null || songMenuBean.getContent().size() == 0) && !refreshLayout.isRefreshing()) {
                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                linearManager.scrollToPosition(songListAdapter.getItemCount() - 1);
            }
            updateHeaderView(songMenuBean.getPic_300(), songMenuBean.getDesc());
        } else if (object instanceof AlbumBean) {
            AlbumBean albumBean = (AlbumBean) object;
            if ((albumBean.getSonglist() == null || albumBean.getSonglist().size() == 0) && !refreshLayout.isRefreshing()) {
                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                linearManager.scrollToPosition(songListAdapter.getItemCount() - 1);
            }
            updateHeaderView(albumBean.getAlbumInfo().getPic_s500(), albumBean.getAlbumInfo().getInfo());
        } else if (object instanceof SingerListBean) {
            SingerListBean singerListBean = (SingerListBean) object;
            updateHeaderView(singerListBean.getAvatar(), singerListBean.getInfo());
        }
    }

    private void updateHeaderView(String imageUrl, String title) {
        Glide.with(this).load(imageUrl).bitmapTransform(new BlurTransformation(this)).into(headerBg);
        Glide.with(this).load(imageUrl).centerCrop().into(headerImage);
        headName.setText(title);
    }

    @Override
    protected boolean isNeedHeadLayout() {
        return true;
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
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("歌曲列表");
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
        DaggerSongListActivityComponent.builder().mainComponent(VideoApplication.getMainComponent())
                .songListModule(new SongListModule(this)).build().inject(this);
        from = getIntent().getIntExtra(MusicUtil.FROM, 0);
        display.setLayoutManager(linearManager = new WrappedLinearLayoutManager(this));
        if (from == MusicUtil.FROM_SONG_MENU) {
            listId = getIntent().getStringExtra(MusicUtil.LIST_ID);
        } else if (from == MusicUtil.FROM_ALBUM) {
            albumId = getIntent().getStringExtra(MusicUtil.ALBUM_ID);
        } else if (from == MusicUtil.FROM_RANK) {
            type = getIntent().getIntExtra(MusicUtil.RANK_TYPE, -1);
        } else if (from == MusicUtil.FROM_SINGER) {
            tingId = getIntent().getStringExtra(MusicUtil.TING_UID);
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
                    playBean.setAlbumUrl(bean.getSonginfo().getPic_premium());
                    CommonLogger.e("这里数据" + playBean.getAlbumUrl());
                    playBean.setSongUrl(bean.getBitrate().getFile_link());
                    playBean.setLrcUrl(bean.getSonginfo().getLrclink());
                    playBean.setTingId(bean.getSonginfo().getTing_uid());
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
        getData(true, true);
    }


    public void getData(boolean isRefresh, boolean isShowLoading) {
        if (from == MusicUtil.FROM_SONG_MENU) {
            presenter.getSongMenuData(listId, isRefresh, isShowLoading);
        } else if (from == MusicUtil.FROM_RANK) {
            presenter.getRankDetailInfo(type, isRefresh, isShowLoading);
        } else if (from == MusicUtil.FROM_ALBUM) {
            presenter.getAlbumInfoData(albumId, isRefresh, isShowLoading);
        } else if (from == MusicUtil.FROM_SINGER) {
            presenter.getSingerSongs(tingId, isRefresh, isShowLoading);
        }
    }

    private View getHeaderView() {
        View headerView = LayoutInflater.from(this).inflate(R.layout.view_activity_song_list_header_view, null);
        headerBg = (ImageView) headerView.findViewById(R.id.iv_view_activity_song_list_header_view_bg);
        headerImage = (ImageView) headerView.findViewById(R.id.iv_view_activity_song_list_header_view_image);
        headName = (TextView) headerView.findViewById(R.id.tv_view_activity_song_list_header_view_title);
        return headerView;
    }

    @Override
    public void onRefresh() {
        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        getData(true, false);
    }


    @Override
    public void loadMore() {
        CommonLogger.e("加载更多阿拉啦");
        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        getData(false, false);
    }


    @Override
    public void hideLoading() {
        super.hideLoading();
        if (refreshLayout.isRefreshing()) {
            if (loadMoreFooterView.getStatus() == LoadMoreFooterView.Status.LOADING) {
                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
            }
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        if (!refreshLayout.isRefreshing() && loadMoreFooterView.getStatus() == LoadMoreFooterView.Status.LOADING) {
            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.ERROR);
        } else {
            refreshLayout.setRefreshing(false);
            super.showError(errorMsg, listener);
        }
    }
}
