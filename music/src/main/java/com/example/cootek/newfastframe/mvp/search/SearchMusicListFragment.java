package com.example.cootek.newfastframe.mvp.search;

import android.os.Bundle;
import android.view.View;

import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.Constant;
import com.example.cootek.newfastframe.MusicManager;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.adapter.RecentPlayListAdapter;
import com.example.cootek.newfastframe.base.MusicBaseFragment;
import com.example.cootek.newfastframe.bean.SearchResultBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/26     10:31
 */
public class SearchMusicListFragment extends MusicBaseFragment {


    private SuperRecyclerView display;


    RecentPlayListAdapter mRecentPlayListAdapter;


    public static SearchMusicListFragment newInstance(ArrayList<MusicPlayBean> list) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.DATA, list);
        SearchMusicListFragment searchMusicListFragment = new SearchMusicListFragment();
        searchMusicListFragment.setArguments(bundle);
        return searchMusicListFragment;
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
        return R.layout.fragment_search_music_list;
    }

    @Override
    protected void initView() {
        display = (SuperRecyclerView) findViewById(R.id.srcv_fragment_search_music_list_display);
    }

    @Override
    protected void initData() {
        display.setLayoutManager(new WrappedLinearLayoutManager(getContext()));
        mRecentPlayListAdapter = new RecentPlayListAdapter();
        display.setAdapter(mRecentPlayListAdapter);
        mRecentPlayListAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                MusicManager.getInstance().play(mRecentPlayListAdapter.getData(), position, 0);
            }
        });
        addDisposable(RxBusManager.getInstance().registerEvent(SearchResultBean.class, new Consumer<SearchResultBean>() {
            @Override
            public void accept(SearchResultBean searchResultBean) throws Exception {
                if (searchResultBean.getMusicPlayBeanList() != null) {
                    mRecentPlayListAdapter.refreshData(searchResultBean.getMusicPlayBeanList());
                }
            }
        }));
    }

    @Override
    protected void updateView() {
        mRecentPlayListAdapter.refreshData((List<MusicPlayBean>) getArguments().getSerializable(Constant.DATA));
    }

    @Override
    public void updateData(Object o) {

    }
}
