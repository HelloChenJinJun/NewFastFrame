package com.snew.video.mvp.qq.detail;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.manager.video.DefaultVideoController;
import com.example.commonlibrary.manager.video.DefaultVideoPlayer;
import com.example.commonlibrary.manager.video.ListVideoManager;
import com.snew.video.R;
import com.snew.video.base.VideoBaseActivity;
import com.snew.video.bean.VideoBean;
import com.snew.video.dagger.qq.detail.DaggerQQVideoDetailComponent;
import com.snew.video.dagger.qq.detail.QQVideoDetailModule;
import com.snew.video.util.VideoUtil;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/14     14:29
 */
public class QQVideoDetailActivity extends VideoBaseActivity<Object, QQVideoDetailPresenter> implements DefaultVideoController.OnItemClickListener {


    private DefaultVideoPlayer display;
    private VideoBean data;

    public static void start(Activity activity, VideoBean data) {
        Intent intent = new Intent(activity, QQVideoDetailActivity.class);
        intent.putExtra(VideoUtil.DATA, data);
        activity.startActivity(intent);
    }

    @Override
    protected boolean isNeedHeadLayout() {
        return true;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_qq_video_detail;
    }

    @Override
    protected void initView() {
        display = findViewById(R.id.dvp_activity_qq_video_detail_display);
    }


    @Override
    protected void initData() {
        DaggerQQVideoDetailComponent.builder().qQVideoDetailModule(new QQVideoDetailModule(this))
                .videoComponent(getComponent()).build().inject(this);
        data = (VideoBean) getIntent().getSerializableExtra(VideoUtil.DATA);
        display.setTitle(data.getTitle())
                .setImageCover(data.getImageCover())
                .setUp(data.getUrl(), null);
        ((DefaultVideoController) display.getController()).setOnItemClickListener(this);
        runOnUiThread(() -> display.start());
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setNeedNavigation(true);
        toolBarOption.setTitle(data.getTitle());
        setToolBar(toolBarOption);
    }

    @Override
    public void updateData(Object o) {

    }

    @Override
    public boolean onStartClick(View view, String url) {
        if (url.contains("html")) {
            presenter.getDetailData(url);
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onBackPressed() {
        if (!ListVideoManager.getInstance().onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        super.finish();
        ListVideoManager.getInstance().release();
    }
}
