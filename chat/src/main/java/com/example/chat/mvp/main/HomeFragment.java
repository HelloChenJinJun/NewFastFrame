package com.example.chat.mvp.main;

import android.widget.TextView;

import com.example.chat.R;
import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.bean.chat.UserEntity;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/24     12:02
 * QQ:         1981367757
 */

public class HomeFragment extends BaseFragment {
    private int currentPosition;


    private UserEntity user;
    private TextView net;


    @Override
    public void initView() {

    }


    @Override
    public void initData() {
        currentPosition = 0;
    }




    @Override
    public void updateData(Object o) {

    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
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
        return R.layout.activity_main_chat;
    }


    @Override
    protected boolean needStatusPadding() {
        return false;
    }






    @Override
    protected void updateView() {

    }
}
