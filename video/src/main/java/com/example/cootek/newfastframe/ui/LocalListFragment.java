package com.example.cootek.newfastframe.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.commonlibrary.baseadapter.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.bean.MusicPlayBean;
import com.example.commonlibrary.mvp.BaseFragment;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.VideoApplication;
import com.example.cootek.newfastframe.adapter.LocalListAdapter;
import com.example.cootek.newfastframe.MusicManager;
import com.example.cootek.newfastframe.MusicService;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.dagger.DaggerMainFragmentComponent;
import com.example.cootek.newfastframe.dagger.MainFragmentModule;
import com.example.cootek.newfastframe.mvp.MainPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by COOTEK on 2017/8/13.
 */

public class LocalListFragment extends BaseFragment<List<MusicPlayBean>, MainPresenter> implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {


    SuperRecyclerView display;
    SwipeRefreshLayout refresh;
    private LoadMoreFooterView loadMoreFooterView;
    @Inject
    LocalListAdapter mainAdapter;

    @Override
    public void updateData(List<MusicPlayBean> musics) {
        mainAdapter.addData(musics);
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
        loadMoreFooterView = new LoadMoreFooterView(getContext());
        display.setLoadMoreFooterView(loadMoreFooterView);
        display.setOnLoadMoreListener(this);
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
    public void loadMore() {
        CommonLogger.e("加载更多?");
        presenter.getAllMusic(false, false);
    }

    @Override
    public void onRefresh() {
        presenter.getAllMusic(true, false);
        refresh.setRefreshing(false);
    }
}
