package com.example.cootek.newfastframe.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.cusotomview.ListViewDecoration;
import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.skin.LoadSkinListener;
import com.example.commonlibrary.skin.SkinManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.SkinUtil;
import com.example.cootek.newfastframe.VideoApplication;
import com.example.cootek.newfastframe.adapter.LocalListAdapter;
import com.example.cootek.newfastframe.MusicManager;
import com.example.cootek.newfastframe.MusicService;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.dagger.main.mainfragment.DaggerMainFragmentComponent;
import com.example.cootek.newfastframe.dagger.main.mainfragment.MainFragmentModule;
import com.example.cootek.newfastframe.mvp.main.MainPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by COOTEK on 2017/8/13.
 */

public class LocalListFragment extends BaseFragment<List<MusicPlayBean>, MainPresenter> implements SwipeRefreshLayout.OnRefreshListener {


    SuperRecyclerView display;
    SwipeRefreshLayout refresh;
    @Inject
    LocalListAdapter mainAdapter;

    @Override
    public void updateData(List<MusicPlayBean> musics) {
        if (musics != null && musics.size() > 0) {
            if (refresh.isRefreshing()) {
                mainAdapter.clearAllData();
                mainAdapter.notifyDataSetChanged();
                mainAdapter.getData().addAll(musics);
                mainAdapter.notifyDataSetChanged();
            } else {
                mainAdapter.addData(musics);
            }
        }
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
        return R.layout.fragment_local_list;
    }

    @Override
    protected void initView() {
        CommonLogger.e("初始化MainFragment");
        display = (SuperRecyclerView) findViewById(R.id.srcv_fragment_main_display);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_fragment_main_refresh);
        display.setLayoutManager(new LinearLayoutManager(getContext()));
        display.addItemDecoration(new ListViewDecoration(getActivity()));
//        loadMoreFooterView = new LoadMoreFooterView(getContext());
//        display.setLoadMoreFooterView(loadMoreFooterView);
//        display.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
    }


    @Override
    protected void initData() {
        DaggerMainFragmentComponent.builder().mainComponent(VideoApplication.getMainComponent()).mainFragmentModule(new MainFragmentModule(this)).build().inject(this);
        mainAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Toast.makeText(getActivity(), "position:" + position, Toast.LENGTH_SHORT).show();
                MusicManager.getInstance().play(mainAdapter.getData(), position, MusicService.MODE_NORMAL);
            }


            @Override
            public boolean onItemLongClick(int position, View view) {
                CommonLogger.e("长按");
                return true;

            }
        });
        display.setAdapter(mainAdapter);
    }

    @Override
    protected void updateView() {
        presenter.getAllMusic(true, true);
    }


    public static LocalListFragment newInstance() {
        return new LocalListFragment();
    }

    @Override
    public void onRefresh() {
        presenter.getAllMusic(true, false);
        refresh.setRefreshing(false);
    }
}
