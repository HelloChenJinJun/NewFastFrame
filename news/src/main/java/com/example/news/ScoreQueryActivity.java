package com.example.news;

import android.widget.TextView;

import com.example.commonlibrary.BaseActivity;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/17     18:15
 * QQ:         1981367757
 */

public class ScoreQueryActivity extends BaseActivity{
    private TextView result;
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
        return R.layout.activity_score_query;
    }

    @Override
    protected void initView() {
        result= (TextView) findViewById(R.id.tv_activity_score_query_result);
    }

    @Override
    protected void initData() {

    }
}
