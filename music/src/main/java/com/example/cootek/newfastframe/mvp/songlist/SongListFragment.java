package com.example.cootek.newfastframe.mvp.songlist;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.baseadapter.decoration.ListViewDecoration;
import com.example.commonlibrary.customview.RoundAngleImageView;
import com.example.commonlibrary.customview.swipe.CustomSwipeRefreshLayout;
import com.example.commonlibrary.manager.music.MusicPlayerManager;
import com.example.commonlibrary.utils.BlurBitmapUtil;
import com.example.commonlibrary.utils.DensityUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.cootek.newfastframe.MusicManager;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.adapter.RecentPlayListAdapter;
import com.example.cootek.newfastframe.base.MusicBaseFragment;
import com.example.cootek.newfastframe.bean.AlbumBean;
import com.example.cootek.newfastframe.bean.ArtistSongsBean;
import com.example.cootek.newfastframe.bean.RankListBean;
import com.example.cootek.newfastframe.dagger.songlist.DaggerSongListComponent;
import com.example.cootek.newfastframe.dagger.songlist.SongListModule;
import com.example.cootek.newfastframe.util.MusicUtil;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/7     10:03
 */
public class SongListFragment extends MusicBaseFragment<Object, SongListPresenter> implements CustomSwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener, View.OnClickListener {
    private CustomSwipeRefreshLayout refresh;
    private SuperRecyclerView display;
    @Inject
    RecentPlayListAdapter mRecentPlayListAdapter;


    private String data;
    private int from;

    public static SongListFragment newInstance(int from, String data) {
        Bundle bundle = new Bundle();
        bundle.putInt(MusicUtil.FROM, from);
        bundle.putString(MusicUtil.DATA, data);
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
        data = getArguments().getString(MusicUtil.DATA);
        display.setLayoutManager(new WrappedLinearLayoutManager(getContext()));
        if (from != MusicUtil.FROM_RECOMMEND) {
            display.setLoadMoreFooterView(new LoadMoreFooterView(getContext()));
            display.setOnLoadMoreListener(this);
        }
        if (from == MusicUtil.FROM_RANK || from == MusicUtil.FROM_ALBUM) {
            display.addHeaderView(getHeaderView());
            root.setBackgroundColor(Color.WHITE);
        } else {
            root.setBackgroundColor(Color.TRANSPARENT);
        }
        display.addItemDecoration(new ListViewDecoration(DensityUtil.toDp(10)));
        display.setAdapter(mRecentPlayListAdapter);
        mRecentPlayListAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                MusicManager.getInstance().play(mRecentPlayListAdapter.getData(), position, 0);
            }
        });
    }


    private View headerBg;
    private RoundAngleImageView artistAvatar;
    private TextView artistName;
    private TextView headerTitle;
    private TextView headerDesc;
    private ImageView headerImage;

    private View getHeaderView() {
        headerBg = getLayoutInflater().inflate(R.layout.view_fragment_song_list_header, display.getHeaderContainer()
                , false);
        artistAvatar = headerBg.findViewById(R.id.riv_view_fragment_song_list_header_avatar);
        artistName = headerBg.findViewById(R.id.tv_view_fragment_song_list_header_artist);
        headerTitle = headerBg.findViewById(R.id.tv_view_fragment_song_list_header_name);
        headerDesc = headerBg.findViewById(R.id.tv_view_fragment_song_list_header_detail);
        headerImage = headerBg.findViewById(R.id.iv_view_fragment_song_list_header_image);
        headerDesc.setOnClickListener(this);
        return headerBg;
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

        if (from == MusicUtil.FROM_ALBUM) {
            //            presenter.getSingerInfo();
        }
    }

    @Override
    public void updateData(Object object) {
        if (object == null) {
            return;
        }
        if (object instanceof List) {
            List<MusicPlayBean> bean = ((List<MusicPlayBean>) object);
            if (refresh.isRefreshing()) {
                mRecentPlayListAdapter.refreshData(bean);
            } else {
                mRecentPlayListAdapter.addData(bean);
            }
        } else if (object instanceof RankListBean) {
            RankListBean rankListBean = (RankListBean) object;
            updateHeaderView(rankListBean.getBillboard().getPic_s260(), rankListBean.getBillboard().getName(), rankListBean.getBillboard().getComment());
        } else if (object instanceof AlbumBean) {
            AlbumBean albumBean = (AlbumBean) object;
            updateHeaderView(albumBean.getAlbumInfo().getPic_s1000(), albumBean.getAlbumInfo().getTitle(), albumBean.getAlbumInfo()
                    .getInfo());
        } else if (object instanceof ArtistSongsBean) {
        }
    }

    private void updateHeaderView(String imageUrl, String title, String detailContent) {
        if (headerBg == null)
            return;
        //        RequestOptions requestOptions = RequestOptions.bitmapTransform(new BlurTransformation(12));
        Glide.with(this).asBitmap().load(imageUrl).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                headerBg.setBackground(BlurBitmapUtil.createBlurredImageFromBitmap(resource, getContext(), 20));
            }
        });
        headerTitle.setText(title);
        headerDesc.setText(detailContent);
        Glide.with(this).load(imageUrl).into(headerImage);
    }

    @Override
    public void onRefresh() {
        refreshData();
        if (from == MusicUtil.FROM_RANK) {
            presenter.getRankDetailInfo(Integer.parseInt(data), true);
        } else if (from == MusicUtil.FROM_ALBUM || from == MusicUtil.FROM_BOTTOM_ALBUM) {
            presenter.getAlbumInfoData(data, true);
        } else if (from == MusicUtil.FROM_RECOMMEND) {
            presenter.getRecommendData(data, true);
        }
    }

    private void refreshData() {
        if (from == MusicUtil.FROM_BOTTOM_ALBUM) {
            if (MusicPlayerManager.getInstance().getMusicPlayBean() != null) {
                data = MusicPlayerManager.getInstance().getMusicPlayBean().getAlbumId() + "";
            }
        } else if (from == MusicUtil.FROM_RECOMMEND) {
            if (MusicPlayerManager.getInstance().getMusicPlayBean() != null) {
                data = MusicPlayerManager.getInstance().getMusicPlayBean().getSongId() + "";
            }
        }
    }

    @Override
    public void loadMore() {
        refreshData();
        if (from == MusicUtil.FROM_RANK) {
            presenter.getRankDetailInfo(Integer.parseInt(data), false);
        } else if (from == MusicUtil.FROM_ALBUM || from == MusicUtil.FROM_BOTTOM_ALBUM) {
            presenter.getAlbumInfoData(data, false);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_view_fragment_song_list_header_detail) {
            ToastUtils.showShortToast("细节详情");
        }
    }
}
