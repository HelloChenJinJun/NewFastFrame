package com.example.cootek.newfastframe.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonlibrary.baseadapter.OnLoadMoreListener;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.mvp.MainBaseActivity;
import com.example.cootek.newfastframe.slidingpanel.SlidingPanelLayout;


/**
 * Created by COOTEK on 2017/8/7.
 */
@Route(path = "/video/main")
public class MainActivity extends MainBaseActivity implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        return R.layout.video_activity_main;
    }

    @Override
    protected void initView() {
        CommonLogger.e("这里开始初始化fragment");
        addOrReplaceFragment(HolderFragment.newInstance(), R.id.fl_activity_main_container);
        showBottomFragment(true);
    }

    @Override
    protected void initData() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void loadMore() {
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void updateData(Object o) {

    }


}
