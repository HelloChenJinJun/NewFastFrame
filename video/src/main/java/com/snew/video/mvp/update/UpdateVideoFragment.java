package com.snew.video.mvp.update;

import com.example.commonlibrary.bean.BaseBean;
import com.snew.video.base.VideoBaseFragment;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/17     16:27
 */
public class UpdateVideoFragment extends VideoBaseFragment<BaseBean, UpdateVideoPresenter> {
    @Override
    protected boolean isNeedHeadLayout() {
        return false;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
    }

    @Override
    protected boolean needStatusPadding() {
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

    @Override
    protected void updateView() {

    }

    @Override
    public void updateData(BaseBean baseBean) {

    }
}
