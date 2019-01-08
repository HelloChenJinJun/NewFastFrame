package com.snew.video;

import com.example.commonlibrary.manager.video.ListVideoManager;
import com.snew.video.base.VideoBaseActivity;
import com.snew.video.mvp.qq.QQVideoFragment;
import com.snew.video.util.VideoUtil;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/10     11:12
 */
public class VideoActivity extends VideoBaseActivity {


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
        return R.layout.activity_video;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        addOrReplaceFragment(QQVideoFragment.newInstance(VideoUtil.VIDEO_URL_TYPE_QQ), R.id.fl_activity_video_container);
    }

    @Override
    public void updateData(Object o) {

    }


    @Override
    public void onBackPressed() {
        if (!ListVideoManager.getInstance().onBackPressed()) {
            super.onBackPressed();
        }
    }
}
