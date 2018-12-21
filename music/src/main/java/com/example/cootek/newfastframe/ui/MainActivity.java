package com.example.cootek.newfastframe.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.commonlibrary.SlideBaseActivity;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.cusotomview.swipe.CustomSwipeRefreshLayout;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.ui.fragment.BottomFragment;
import com.example.cootek.newfastframe.ui.fragment.HolderFragment;
import com.example.commonlibrary.cusotomview.draglayout.DragLayout;
import com.example.commonlibrary.cusotomview.draglayout.OnDragDeltaChangeListener;
import com.example.cootek.newfastframe.view.slide.SlidingPanelLayout;


/**
 * Created by COOTEK on 2017/8/7.
 */
public class MainActivity extends SlideBaseActivity implements OnLoadMoreListener, CustomSwipeRefreshLayout.OnRefreshListener, OnDragDeltaChangeListener {

    private SlidingPanelLayout mSlidingPanelLayout;
    private DragLayout dragLayout;
    private HolderFragment mHolderFragment;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }


    public SlidingPanelLayout getSlidingPanelLayout() {
        return mSlidingPanelLayout;
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
        return R.layout.video_activity_main;
    }

    @Override
    protected void initView() {
        mSlidingPanelLayout = findViewById(R.id.sl_activity_main_container);
        mHolderFragment = HolderFragment.newInstance();
        addBackStackFragment(mHolderFragment, R.id.fl_activity_main_content, false);
        addOrReplaceFragment(BottomFragment.newInstance(), R.id.fl_activity_main_bottom);
        dragLayout = findViewById(R.id.dl_activity_main_drag);
        dragLayout.setListener(this);
    }

    @Override
    protected void initData() {
    }



    @Override
    public void onBackPressed() {
        if (mSlidingPanelLayout.getPanelState() == SlidingPanelLayout.PanelState.EXPANDED) {
            mSlidingPanelLayout.setPanelState(SlidingPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
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


    public void switchMenu() {
        dragLayout.switchMenu();
    }


    public void notifyIntercept(boolean isIntercept) {
        if (dragLayout != null) {
            dragLayout.setIntercept(isIntercept);
        }
    }

    @Override
    public void onDrag(View view, float delta) {
        //        ((HolderFragment) currentFragment).onDrag(delta);
        if (mHolderFragment != null) {
            mHolderFragment.onDrag(delta);
        }
    }

    @Override
    public void onCloseMenu() {

    }

    @Override
    public void onOpenMenu() {

    }


}
