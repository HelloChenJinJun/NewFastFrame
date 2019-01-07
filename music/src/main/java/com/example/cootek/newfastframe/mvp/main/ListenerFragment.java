package com.example.cootek.newfastframe.mvp.main;

import android.view.View;

import com.example.commonlibrary.utils.ToastUtils;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.base.MusicBaseFragment;
import com.example.cootek.newfastframe.mvp.recent.RecentPlayListFragment;
import com.example.cootek.newfastframe.util.MusicUtil;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/11/5      16:03
 * QQ:             1981367757
 */

public class ListenerFragment extends MusicBaseFragment implements View.OnClickListener {


    @Override
    public void updateData(Object o) {

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
    protected int getContentLayout() {
        return R.layout.fragment_listener;
    }

    @Override
    protected void initView() {
        findViewById(R.id.ll_fragment_listener_local_music).setOnClickListener(this);
        findViewById(R.id.tv_fragment_music_repository).setOnClickListener(this);
        findViewById(R.id.tv_fragment_listener_recent).setOnClickListener(this);
    }

    @Override
    protected void initData() {


    }

    @Override
    protected void updateView() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_fragment_listener_local_music) {
            addBackStackFragment(RecentPlayListFragment.newInstance(MusicUtil.FROM_LOCAL));
        } else if (id == R.id.tv_fragment_music_repository) {
            //            addBackStackFragment(RecommendFragment.newInstance());
//            addBackStackFragment(SingerListFragment.newInstance(null));
            ToastUtils.showShortToast("推荐界面后期开发");

        } else if (id == R.id.tv_fragment_listener_recent) {
            addBackStackFragment(RecentPlayListFragment.newInstance(MusicUtil.FROM_RECENT));
        }
    }

    public static ListenerFragment newInstance() {
        return new ListenerFragment();
    }
}
