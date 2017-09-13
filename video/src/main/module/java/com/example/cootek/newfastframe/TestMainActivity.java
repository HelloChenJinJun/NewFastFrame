package com.example.cootek.newfastframe;

import android.content.Intent;
import android.view.View;

import com.example.commonlibrary.BaseActivity;
import com.example.cootek.newfastframe.ui.MainActivity;

/**
 * Created by COOTEK on 2017/8/28.
 */

public class TestMainActivity extends BaseActivity {
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
        return R.layout.activity_test_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }


    public void jump(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
