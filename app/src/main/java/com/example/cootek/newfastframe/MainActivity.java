package com.example.cootek.newfastframe;

import com.example.commonlibrary.mvp.BaseActivity;

/**
 * Created by COOTEK on 2017/8/7.
 */

public class MainActivity extends BaseActivity {


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
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
