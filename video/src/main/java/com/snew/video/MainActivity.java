package com.snew.video;

import android.app.Activity;
import android.content.Intent;

import com.example.commonlibrary.manager.video.ListVideoManager;
import com.snew.video.base.VideoBaseActivity;
import com.snew.video.mvp.qq.QQVideoFragment;

public class MainActivity extends VideoBaseActivity {


    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
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
        return R.layout.activity_main_video;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected boolean needStatusPadding() {
        return false;
    }

    @Override
    protected void initData() {
        addOrReplaceFragment(QQVideoFragment.newInstance(), R.id.fl_activity_video_container);
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
