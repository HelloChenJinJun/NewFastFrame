package com.example.news;

import android.content.Intent;
import android.view.View;

import com.example.commonlibrary.BaseActivity;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/16      15:52
 * QQ:             1981367757
 */

public class NewsActivity extends BaseActivity{
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
        return R.layout.activity_news;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }


    public void jump(View view){
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
