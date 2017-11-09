package com.example.live.ui.fragment;

import com.example.commonlibrary.BaseFragment;
import com.example.live.R;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/14      15:41
 * QQ:             1981367757
 */

public class LiveFragment extends BaseFragment {
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
        return R.layout.fragment_live;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void updateView() {

    }

    public static LiveFragment newInstance() {
        return new LiveFragment();
    }
}
