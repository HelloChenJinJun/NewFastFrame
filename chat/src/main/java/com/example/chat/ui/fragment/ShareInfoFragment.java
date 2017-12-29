package com.example.chat.ui.fragment;

import com.example.chat.R;
import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/29     22:33
 * QQ:         1981367757
 */

public class ShareInfoFragment extends BaseFragment {

    private SuperRecyclerView display;
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
        return R.layout.fragment_share_info;
    }

    @Override
    protected void initView() {
        display= (SuperRecyclerView) findViewById(R.id.srcv_fragment_share_info_display);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void updateView() {

    }
}
