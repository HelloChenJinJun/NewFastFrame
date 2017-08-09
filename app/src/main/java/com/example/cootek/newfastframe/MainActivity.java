package com.example.cootek.newfastframe;

import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.cusotomview.RoundAngleImageView;
import com.example.commonlibrary.mvp.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by COOTEK on 2017/8/7.
 */

public class MainActivity extends BaseActivity {


    @BindView(R.id.srcv_activity_main_display)
    SuperRecyclerView display;
    @BindView(R.id.riv_activity_main_bottom_image)
    RoundAngleImageView image;
    @BindView(R.id.tv_activity_main_bottom_name)
    TextView bottomName;
    @BindView(R.id.tv_activity_main_bottom_description)
    TextView bottomDescription;
    @BindView(R.id.iv_activity_main_bottom_play)
    ImageView bottomPlay;
    @BindView(R.id.iv_activity_main_bottom_previous)
    ImageView bottomPrevious;
    @BindView(R.id.iv_activity_main_bottom_next)
    ImageView bottomNext;
    @BindView(R.id.ll_activity_main_bottom_container)
    LinearLayout bottomContainer;
    private MainAdapter mainAdapter;

    @Override
    protected boolean isNeedHeadLayout() {
        return false;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return true;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        display.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_activity_main_bottom_play, R.id.iv_activity_main_bottom_previous, R.id.iv_activity_main_bottom_next, R.id.ll_activity_main_bottom_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_activity_main_bottom_play:
                break;
            case R.id.iv_activity_main_bottom_previous:
                break;
            case R.id.iv_activity_main_bottom_next:
                break;
            case R.id.ll_activity_main_bottom_container:
                break;
        }
    }
}
