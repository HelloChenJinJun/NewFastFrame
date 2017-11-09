package com.example.live.ui;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.imageloader.glide.GlideImageLoaderConfig;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.live.LiveApplication;
import com.example.live.R;
import com.example.live.bean.LiveRoomBean;
import com.example.live.dagger.video.DaggerVideoComponent;
import com.example.live.dagger.video.VideoModule;
import com.example.live.mvp.video.VideoPresenter;
import com.example.live.util.LiveUtil;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.pili.pldroid.player.widget.PLVideoView;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/14      20:19
 * QQ:             1981367757
 */

public class VideoActivity extends BaseActivity<LiveRoomBean, VideoPresenter> implements View.OnClickListener {
    private boolean isFull;
    private PLVideoTextureView display;
    private ImageView expend;
    private String uid;
    private FrameLayout contentContainer;
    private ProgressBar loading;
    private ImageView thumb;

    @Override
    public void updateData(LiveRoomBean liveRoomBean) {
        if (isFull) {
            loading.setVisibility(View.GONE);
            thumb.setVisibility(View.GONE);
        }
        if (liveRoomBean != null && liveRoomBean.getLive() != null && liveRoomBean.getLive().getWs() != null) {
            LiveRoomBean.LiveEntity.WsEntity.FlvEntity bean = liveRoomBean.getLive().getWs().getFlv();
            LiveRoomBean.LiveEntity.WsEntity.FlvEntity._$5Entity entity;
            entity = bean.get_$5();
            if (entity != null) {
                display.setVideoPath(entity.getSrc());
            } else {
                ToastUtils.showShortToast("URL为空");
            }
        }
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
        isFull = getIntent().getBooleanExtra(LiveUtil.IS_FULL, false);
        if (isFull) {
            return R.layout.activity_video_full;
        }
        return R.layout.activity_video;
    }


    @Override
    protected void onResume() {
        super.onResume();
        display.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        display.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        display.stopPlayback();
    }

    @Override
    protected void initView() {
        if (isFull) {
            display = (PLVideoTextureView) findViewById(R.id.pv_activity_video_full_display);
            display.setDisplayOrientation(PLVideoView.ASPECT_RATIO_PAVED_PARENT);
            thumb = (ImageView) findViewById(R.id.iv_activity_video_thumb);
            loading = (ProgressBar) findViewById(R.id.pb_activity_video_loading);
        } else {
            display = (PLVideoTextureView) findViewById(R.id.pv_activity_video_display);
            display.setDisplayOrientation(PLVideoView.ASPECT_RATIO_16_9);
            expend = (ImageView) findViewById(R.id.iv_activity_video_expend);
            contentContainer = (FrameLayout) findViewById(R.id.fl_activity_video_content_container);
            expend.setOnClickListener(this);
        }
    }

    @Override
    protected void initData() {
        updateVideoViewLayout();
        uid = getIntent().getStringExtra(LiveUtil.UID);
        if (isFull) {
            String url = getIntent().getStringExtra(LiveUtil.THUMB);
            BaseApplication.getAppComponent().getImageLoader().loadImage(this, new GlideImageLoaderConfig.Builder()
                    .url(url).bitmapTransformation(new BlurTransformation(this, 18, 3)).imageView(thumb).build());
        }
        DaggerVideoComponent.builder().videoModule(new VideoModule(this)).mainComponent(LiveApplication.getMainComponent())
                .build().inject(this);
        display.setOnPreparedListener(new PLMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(PLMediaPlayer plMediaPlayer) {
                display.start();
            }
        });
        display.post(new Runnable() {
            @Override
            public void run() {
                presenter.enterRoom(uid);
            }
        });
    }


    public boolean isLandscape() {
        return getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (!isFull) {
            if (isLandscape()) {
                contentContainer.setVisibility(View.GONE);
                expend.setImageResource(R.drawable.ic_compare_arrows_white_24dp);
            } else {
                contentContainer.setVisibility(View.VISIBLE);
                expend.setImageResource(R.drawable.ic_zoom_out_map_white_24dp);
            }
        }
        updateVideoViewLayout();
    }

    private void updateVideoViewLayout() {
        if (!isFull) {
            ViewGroup.LayoutParams layoutParams = display.getLayoutParams();
            if (isLandscape()) {
                layoutParams.height = getResources().getDisplayMetrics().heightPixels;
            } else {
                layoutParams.height = (int) (getResources().getDisplayMetrics().widthPixels * 9.0f / 16.0f);
            }
            display.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_activity_video_expend) {
            if (isLandscape()) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (isLandscape()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else {
            super.onBackPressed();
        }
    }
}
