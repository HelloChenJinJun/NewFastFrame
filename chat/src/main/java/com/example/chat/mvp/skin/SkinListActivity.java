package com.example.chat.mvp.skin;

import android.support.v4.widget.SwipeRefreshLayout;

import com.example.chat.R;
import com.example.chat.adapter.SkinListAdapter;
import com.example.chat.base.SlideBaseActivity;
import com.example.chat.bean.SkinBean;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;

import java.util.List;

import javax.inject.Inject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/5/22     23:39
 */

public class SkinListActivity extends SlideBaseActivity<List<SkinBean>,SkinListPresenter> {
    private SuperRecyclerView display;
    private SwipeRefreshLayout refresh;

    @Inject
    SkinListAdapter skinListAdapter;


    @Override
    public void updateData(List<SkinBean> skinBeans) {

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
        return R.layout.activity_skin_list;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
