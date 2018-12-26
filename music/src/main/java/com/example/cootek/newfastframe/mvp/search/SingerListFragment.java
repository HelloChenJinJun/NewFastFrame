package com.example.cootek.newfastframe.mvp.search;

import android.os.Bundle;
import android.view.View;

import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.Constant;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.adapter.SingerListAdapter;
import com.example.cootek.newfastframe.base.MusicBaseFragment;
import com.example.cootek.newfastframe.bean.ArtistInfo;
import com.example.cootek.newfastframe.bean.SearchResultBean;
import com.example.cootek.newfastframe.mvp.singer.SingerDetailFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.core.view.ViewCompat;
import io.reactivex.functions.Consumer;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/26     11:14
 */
public class SingerListFragment extends MusicBaseFragment {
    private SuperRecyclerView display;


    private SingerListAdapter mSingerListAdapter;

    public static SingerListFragment newInstance(ArrayList<ArtistInfo> artistInfoList) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.DATA, artistInfoList);
        SingerListFragment singerListFragment = new SingerListFragment();
        singerListFragment.setArguments(bundle);
        return singerListFragment;
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
        return R.layout.fragment_singer_list;
    }

    @Override
    protected void initView() {
        display = (SuperRecyclerView) findViewById(R.id.srcv_fragment_singer_list_display);


    }

    @Override
    protected void initData() {
        display.setLayoutManager(new WrappedLinearLayoutManager(getContext()));
        mSingerListAdapter = new SingerListAdapter();
        display.setAdapter(mSingerListAdapter);
        mSingerListAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                View avatar = view.findViewById(R.id.iv_item_fragment_singer_list_image);
                View name = view.findViewById(R.id.tv_item_fragment_singer_list_name);
                ViewCompat.setTransitionName(avatar, "avatar");
                ViewCompat.setTransitionName(name, "name");
                addBackStackFragment(SingerDetailFragment.newInstance(mSingerListAdapter.getData(position)), avatar, name);


            }
        });
        addDisposable(RxBusManager.getInstance().registerEvent(SearchResultBean.class, new Consumer<SearchResultBean>() {
            @Override
            public void accept(SearchResultBean searchResultBean) throws Exception {
                if (searchResultBean.getArtistInfoList() != null) {
                    mSingerListAdapter.refreshData(searchResultBean.getArtistInfoList());
                }
            }
        }));
    }

    @Override
    protected void updateView() {
        mSingerListAdapter.refreshData((List<ArtistInfo>) getArguments().getSerializable(Constant.DATA));
    }

    @Override
    public void updateData(Object o) {

    }
}
