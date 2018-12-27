package com.example.cootek.newfastframe.mvp.search;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.decoration.GridSpaceDecoration;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.Constant;
import com.example.commonlibrary.utils.DensityUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.adapter.AlbumListAdapter;
import com.example.cootek.newfastframe.base.MusicBaseFragment;
import com.example.cootek.newfastframe.bean.AlbumWrappedBean;
import com.example.cootek.newfastframe.bean.SearchResultBean;
import com.example.cootek.newfastframe.dagger.album.AlbumListModule;
import com.example.cootek.newfastframe.dagger.album.DaggerAlbumListComponent;
import com.example.cootek.newfastframe.mvp.album.AlbumListPresenter;
import com.example.cootek.newfastframe.mvp.songlist.SongListFragment;
import com.example.cootek.newfastframe.util.MusicUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/26     11:10
 */
public class AlbumListFragment extends MusicBaseFragment<BaseBean, AlbumListPresenter> implements OnLoadMoreListener {


    private SuperRecyclerView display;


    private AlbumListAdapter mAlbumListAdapter;
    private String artistId;


    public static AlbumListFragment newInstance(ArrayList<AlbumWrappedBean> albumBeans) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.DATA, albumBeans);
        AlbumListFragment albumListFragment = new AlbumListFragment();
        albumListFragment.setArguments(bundle);
        return albumListFragment;
    }


    public static AlbumListFragment newInstance(String artistId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(MusicUtil.ARTIST_ID, artistId);
        AlbumListFragment albumListFragment = new AlbumListFragment();
        albumListFragment.setArguments(bundle);
        return albumListFragment;
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
        return R.layout.fragment_album_list;
    }

    @Override
    protected void initView() {
        display = findViewById(R.id.srvc_fragment_album_list_display);
    }

    @Override
    protected void initData() {
        DaggerAlbumListComponent.builder().mainComponent(getMainComponent()).albumListModule(new AlbumListModule(this))
                .build().inject(this);
        artistId = getArguments().getString(MusicUtil.ARTIST_ID);
        mAlbumListAdapter = new AlbumListAdapter();
        display.setLayoutManager(new WrappedGridLayoutManager(getContext(), 2));
        if (artistId != null) {
            display.setLoadMoreFooterView(new LoadMoreFooterView(getContext()));
            display.setOnLoadMoreListener(this);
        }
        root.setBackgroundColor(Color.parseColor("#3C5F78"));
        display.addItemDecoration(new GridSpaceDecoration(2, DensityUtil.toDp(10), true));
        display.setAdapter(mAlbumListAdapter);
        mAlbumListAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                addBackStackFragment(SongListFragment.newInstance(MusicUtil.FROM_ALBUM, mAlbumListAdapter.getData(position).getAlbumId()));
            }
        });
        addDisposable(RxBusManager.getInstance().registerEvent(SearchResultBean.class, searchResultBean -> {
            if (searchResultBean.getAlbumBeans() != null) {
                if (mAlbumListAdapter != null) {
                    mAlbumListAdapter.refreshData(searchResultBean.getAlbumBeans());
                } else {
                    data = searchResultBean.getAlbumBeans();
                }
            }
        }));
    }


    private List<AlbumWrappedBean> data = null;


    @Override
    protected void updateView() {
        if (artistId != null) {
            presenter.getAlbumListData(true, artistId);
        } else {
            if (data == null) {
                mAlbumListAdapter.refreshData((List<AlbumWrappedBean>) getArguments().getSerializable(Constant.DATA));
            } else {
                mAlbumListAdapter.refreshData(data);
            }
        }
    }


    @Override
    public void updateData(BaseBean baseBean) {
        if (baseBean.getCode() == 200) {
            if (baseBean.getType() == MusicUtil.BASE_TYPE_ALBUM_CONTENT) {
                List<AlbumWrappedBean> list = (List<AlbumWrappedBean>) baseBean.getData();
                if (getLayoutStatus() == EmptyLayout.STATUS_LOADING) {
                    mAlbumListAdapter.refreshData(list);
                } else {
                    mAlbumListAdapter.addData(list);
                }
            }
        } else {
            ToastUtils.showShortToast(baseBean.getDesc());
        }

    }

    @Override
    public void loadMore() {
        presenter.getAlbumListData(false, artistId);
    }
}
