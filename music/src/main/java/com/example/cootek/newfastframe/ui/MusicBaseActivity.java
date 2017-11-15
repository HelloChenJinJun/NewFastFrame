package com.example.cootek.newfastframe.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.cootek.newfastframe.MusicManager;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.ui.fragment.BottomFragment;
import com.example.cootek.newfastframe.view.ContentView;
import com.example.cootek.newfastframe.view.DragLayout;
import com.example.cootek.newfastframe.view.slide.SlidingPanelLayout;

/**
 * Created by COOTEK on 2017/8/11.
 */

public abstract class MusicBaseActivity<T, P extends BasePresenter> extends BaseActivity<T, P> {


    protected SlidingPanelLayout slidingPanelLayout;
    private BottomFragment baseFragment;
    protected DragLayout dragLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MusicManager.getInstance().bindService(this);
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void initBaseView() {
        slidingPanelLayout = (SlidingPanelLayout) LayoutInflater.from(this).inflate(R.layout.view_music_content, null,false);
        slidingPanelLayout.addView(contentView, 0);
        if (this instanceof MainActivity) {
            dragLayout=new DragLayout(this);
            dragLayout.setMode(DragLayout.LEFT_MODE);
            dragLayout.setBackgroundResource(R.drawable.wallpaper);
            ViewGroup menu= (ViewGroup) LayoutInflater.from(this).inflate(R.layout.view_activity_main_menu,null,false);
            dragLayout.addView(menu);
            ContentView contentView=new ContentView(this);
            contentView.addView(slidingPanelLayout);
            dragLayout.addView(contentView);
            setContentView(dragLayout);
        }else if (this instanceof LocalMusicActivity){
            dragLayout=new DragLayout(this);
            dragLayout.setMode(DragLayout.RIGHT_MODE);
            dragLayout.setBackgroundResource(R.drawable.wallpaper);
            ViewGroup menu= (ViewGroup) LayoutInflater.from(this).inflate(R.layout.view_activity_main_menu,null,false);
//            int width=(int) (DensityUtil.getScreenWidth(this)*0.8f);
//            menu.setLayoutParams(new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
            dragLayout.addView(menu);
            ContentView contentView=new ContentView(this);
            contentView.addView(slidingPanelLayout);
            dragLayout.addView(contentView);
            setContentView(dragLayout);
        }else {
            setContentView(slidingPanelLayout);
        }
        super.initBaseView();
        showBottomFragment(true);
    }



    private View contentView;


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        contentView = LayoutInflater.from(this).inflate(layoutResID, null);
        if (slidingPanelLayout != null) {
            super.setContentView(layoutResID);
        }
    }


    @Override
    public void setContentView(View view) {
        if (contentView == null) {
            contentView = view;
        }
        if (slidingPanelLayout != null) {
            super.setContentView(view);
        }
    }





    /**
     * @param show 显示或关闭底部播放控制栏
     */
    protected void showBottomFragment(boolean show) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (show) {
            if (baseFragment == null) {
                baseFragment = BottomFragment.newInstance();
                ft.add(R.id.fl_view_music_content_bottom, baseFragment).show(baseFragment).commitAllowingStateLoss();
            } else {
                ft.show(baseFragment).commitAllowingStateLoss();
            }
        } else {
            if (baseFragment != null)
                ft.hide(baseFragment).commitAllowingStateLoss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicManager.getInstance().unBindService(this);
    }

    @Override
    public void onBackPressed() {
        if (slidingPanelLayout.getPanelState() == SlidingPanelLayout.PanelState.EXPANDED) {
            slidingPanelLayout.setPanelState(SlidingPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }




}
