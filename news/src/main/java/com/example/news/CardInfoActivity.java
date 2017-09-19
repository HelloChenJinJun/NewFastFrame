package com.example.news;

import com.example.commonlibrary.BaseActivity;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/19      23:19
 * QQ:             1981367757
 */

public class CardInfoActivity extends BaseActivity<Object,CardInfoPresenter>{
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
        return R.layout.activity_card_info;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
