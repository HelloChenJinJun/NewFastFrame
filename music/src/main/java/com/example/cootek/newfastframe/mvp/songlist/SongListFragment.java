package com.example.cootek.newfastframe.mvp.songlist;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.cusotomview.ListViewDecoration;
import com.example.commonlibrary.cusotomview.swipe.CustomSwipeRefreshLayout;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.DensityUtil;
import com.example.cootek.newfastframe.MusicManager;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.adapter.SongListAdapter;
import com.example.cootek.newfastframe.base.MusicBaseFragment;
import com.example.cootek.newfastframe.bean.DownLoadMusicBean;
import com.example.cootek.newfastframe.bean.RankListBean;
import com.example.cootek.newfastframe.dagger.songlist.DaggerSongListComponent;
import com.example.cootek.newfastframe.dagger.songlist.SongListModule;
import com.example.cootek.newfastframe.util.MusicUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/7     10:03
 */
public class SongListFragment extends MusicBaseFragment<Object, SongListPresenter> implements CustomSwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
    private CustomSwipeRefreshLayout refresh;
    private SuperRecyclerView display;
    @Inject
    SongListAdapter songListAdapter;


    private int type;
    private int from;

    public static SongListFragment newInstance(int from, int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(MusicUtil.FROM, from);
        bundle.putInt(MusicUtil.TYPE, type);
        SongListFragment songListFragment = new SongListFragment();
        songListFragment.setArguments(bundle);
        return songListFragment;
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
        return R.layout.fragment_song_list;
    }

    @Override
    protected void initView() {
        refresh = findViewById(R.id.refresh_fragment_song_list_refresh);
        display = findViewById(R.id.srcv_fragment_song_list_display);
        refresh.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        DaggerSongListComponent.builder().mainComponent(getMainComponent())
                .songListModule(new SongListModule(this)).build().inject(this);
        from = getArguments().getInt(MusicUtil.FROM);
        type = getArguments().getInt(MusicUtil.TYPE);
        display.setLayoutManager(new WrappedLinearLayoutManager(getContext()));
        display.setLoadMoreFooterView(new LoadMoreFooterView(getContext()));
        display.setOnLoadMoreListener(this);
        display.addHeaderView(getHeaderView());
        display.addItemDecoration(new ListViewDecoration(DensityUtil.toDp(10)));
        display.setAdapter(songListAdapter);
        songListAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                List<DownLoadMusicBean> list = songListAdapter.getData();
                List<MusicPlayBean> result = new ArrayList<>(list.size());
                for (int i = 0; i < list.size(); i++) {
                    DownLoadMusicBean downLoadMusicBean = list.get(i);
                    MusicPlayBean musicPlayBean = new MusicPlayBean();
                    musicPlayBean.setIsLocal(false);
                    musicPlayBean.setSongId(Long.parseLong(downLoadMusicBean.getSonginfo().getSong_id()));
                    musicPlayBean.setAlbumId(Long.parseLong(downLoadMusicBean.getSonginfo().getAlbum_id()));
                    musicPlayBean.setAlbumName(downLoadMusicBean.getSonginfo().getAlbum_title());
                    musicPlayBean.setAlbumUrl(downLoadMusicBean.getSonginfo().getPic_huge());
                    musicPlayBean.setArtistId(downLoadMusicBean.getSonginfo().getArtist_id());
                    musicPlayBean.setArtistName(downLoadMusicBean.getSonginfo().getAuthor());
                    musicPlayBean.setLrcUrl(downLoadMusicBean.getSonginfo().getLrclink());
                    musicPlayBean.setSongUrl(downLoadMusicBean.getBitrate().getFile_link());
                    musicPlayBean.setSongName(downLoadMusicBean.getSonginfo().getTitle());
                    result.add(musicPlayBean);
                    CommonLogger.e(downLoadMusicBean.toString());
                }
                MusicManager.getInstance().play(result, position);
            }
        });
    }


    private ImageView headerCover;

    private View getHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.view_fragment_song_list_header, display.getHeaderContainer()
                , false);
        headerCover = view.findViewById(R.id.iv_view_fragment_song_list_header_image);
        return view;
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
    protected void updateView() {
        onRefresh();
    }

    @Override
    public void updateData(Object object) {
        if (object instanceof List) {
            List<DownLoadMusicBean> bean = ((List<DownLoadMusicBean>) object);
            if (refresh.isRefreshing()) {
                songListAdapter.refreshData(bean);
            } else {
                songListAdapter.addData(bean);
            }
        } else if (object instanceof RankListBean) {
            RankListBean rankListBean = (RankListBean) object;
            updateHeaderView(rankListBean.getBillboard().getPic_s260(), rankListBean.getBillboard().getName());
        }
    }

    private void updateHeaderView(String imageUrl, String title) {
        Glide.with(this).load(imageUrl).into(headerCover);
    }

    @Override
    public void onRefresh() {
        presenter.getRankDetailInfo(type, true);
    }

    @Override
    public void loadMore() {
        presenter.getRankDetailInfo(type, false);
    }
}
