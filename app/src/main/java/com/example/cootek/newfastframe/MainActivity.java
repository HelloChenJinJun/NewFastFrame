package com.example.cootek.newfastframe;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

import com.example.commonlibrary.baseadapter.OnLoadMoreListener;
import com.example.commonlibrary.utils.CommonLogger;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


import butterknife.BindView;


/**
 * Created by COOTEK on 2017/8/7.
 */

public class MainActivity extends MainBaseActivity implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.slide_activity_main_container)
    SlidingUpPanelLayout slideLayout;

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
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        CommonLogger.e("这里开始初始化fragment");
        addOrReplaceFragment(HolderFragment.newInstance(), R.id.fl_activity_main_container);
        Fragment fragment = BottomFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_activity_main_bottom_container, fragment).show(fragment).commitAllowingStateLoss();
    }

    @Override
    protected void initData() {

    }

    private void getData(boolean isRefresh) {
    }

    @Override
    public void onBackPressed() {
        if (slideLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            slideLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void loadMore() {
        getData(false);
    }

    @Override
    public void onRefresh() {
        getData(true);
    }

    @Override
    public void updateData(Object o) {

    }
}
