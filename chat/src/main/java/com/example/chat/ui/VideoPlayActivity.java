package com.example.chat.ui;

import android.annotation.TargetApi;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.chat.R;
import com.example.chat.util.LogUtil;

import mabeijianxi.camera.MediaRecorderBase;
import mabeijianxi.camera.util.DeviceUtils;
import mabeijianxi.camera.util.StringUtils;
import mabeijianxi.camera.views.SurfaceVideoView;

import static mabeijianxi.camera.util.DeviceUtils.getScreenWidth;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/23      20:50
 * QQ:             1981367757
 */

public class VideoPlayActivity extends SlideBaseActivity implements
        SurfaceVideoView.OnPlayStateListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener, View.OnClickListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnInfoListener {
        /**
         * 播放控件
         */
        private SurfaceVideoView mVideoView;
        /**
         * 暂停按钮
         */
        private View mPlayerStatus;
        private View mLoading;

        /**
         * 播放路径
         */
        private String mPath;
        /**
         * 是否需要回复播放
         */
        private boolean mNeedResume;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                super.onCreate(savedInstanceState);
//                防止锁屏

        }


        @Override
        public void initView() {
                mVideoView = (SurfaceVideoView) findViewById(R.id.videoview);
                int screenWidth = getScreenWidth(this);
                mVideoView.getLayoutParams().height = (int) (screenWidth / (MediaRecorderBase.SMALL_VIDEO_WIDTH / (MediaRecorderBase.SMALL_VIDEO_HEIGHT * 1.0f)));
                mVideoView.requestLayout();
                mPlayerStatus = findViewById(R.id.play_status);
                mLoading = findViewById(R.id.loading);
                mVideoView.setOnPreparedListener(this);
                mVideoView.setOnPlayStateListener(this);
                mVideoView.setOnErrorListener(this);
                mVideoView.setOnClickListener(this);
                mVideoView.setOnInfoListener(this);
                mVideoView.setOnCompletionListener(this);
                findViewById(R.id.root).setOnClickListener(this);
        }



        @Override
        public void initData() {
                mPath = getIntent().getStringExtra("path");
                if (StringUtils.isEmpty(mPath)) {
                        finish();
                }
                mVideoView.setVideoPath(mPath);

        }


        @Override
        protected void onResume() {
                super.onResume();
                if (mVideoView != null && mNeedResume) {
                        mNeedResume = false;
                        if (mVideoView.isRelease())
                                mVideoView.reOpen();
                        else
                                mVideoView.start();
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
                return R.layout.activity_video_play;
        }


        @Override
        protected void onPause() {
                super.onPause();
                if (mVideoView != null) {
                        if (mVideoView.isPlaying()) {
                                mNeedResume = true;
                                mVideoView.pause();
                        }
                }
        }

        @Override
        protected void onDestroy() {
                if (mVideoView != null) {
                        mVideoView.release();
                        mVideoView = null;
                }
                super.onDestroy();
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
                if (!isFinishing()) {
                        Toast.makeText(this, "播放完成", Toast.LENGTH_SHORT).show();
                        mPlayerStatus.setVisibility(View.VISIBLE);
                }
        }


        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
                if (!isFinishing()) {
                        // 播放失败
                        LogUtil.e("播放失败");
                }
                finish();
                return false;
        }

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                        case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                                // 音频和视频数据不正确
                                break;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                                if (!isFinishing())
                                        mVideoView.pause();
                                break;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                                if (!isFinishing())
                                        mVideoView.start();
                                break;
                        case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                                if (DeviceUtils.hasJellyBean()) {
                                        mVideoView.setBackground(null);
                                } else {
                                        mVideoView.setBackgroundDrawable(null);
                                }
                                break;
                }
                return false;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
                mVideoView.setVolume(SurfaceVideoView.getSystemVolumn(this));
                mVideoView.start();
                mLoading.setVisibility(View.GONE);

        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
                switch (event.getKeyCode()) {// 跟随系统音量走
                        case KeyEvent.KEYCODE_VOLUME_DOWN:
                        case KeyEvent.KEYCODE_VOLUME_UP:
                                mVideoView.dispatchKeyEvent(this, event);
                                break;
                }
                return super.dispatchKeyEvent(event);
        }

        @Override
        public void onClick(View v) {
                switch (v.getId()) {
                        case R.id.videoview:
                                if (mVideoView.isPlaying()) {
                                        Toast.makeText(this, "1正在播放中", Toast.LENGTH_SHORT).show();
                                        mVideoView.pause();
                                } else {
                                        if (mVideoView.isRelease())
                                                mVideoView.reOpen();
                                        else
                                                mVideoView.start();
                                }
                                break;
                        case R.id.root:
                                break;
                }
        }

        @Override
        public void onStateChanged(boolean isPlaying) {
                mPlayerStatus.setVisibility(isPlaying ? View.GONE : View.VISIBLE);
        }

        @Override
        public void updateData(Object o) {

        }
}
