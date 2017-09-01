package com.example.cootek.newfastframe.mvp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.commonlibrary.mvp.BaseActivity;
import com.example.commonlibrary.mvp.BaseFragment;
import com.example.commonlibrary.mvp.BasePresenter;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.MusicManager;
import com.example.cootek.newfastframe.MusicService;
import com.example.cootek.newfastframe.R;
import com.example.cootek.newfastframe.event.MusicStatusEvent;
import com.example.cootek.newfastframe.slidingpanel.SlidingPanelLayout;
import com.example.cootek.newfastframe.ui.BottomFragment;

/**
 * Created by COOTEK on 2017/8/11.
 */

public abstract class MainBaseActivity<T, P extends BasePresenter> extends BaseActivity<T, P> {


    protected MusicBroadCastReceiver receiver;
    protected SlidingPanelLayout slidingPanelLayout;
    private BottomFragment baseFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MusicManager.getInstance().bindService(this);
        receiver = new MusicBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicService.META_CHANGED);
        intentFilter.addAction(MusicService.PLAYSTATE_CHANGED);
        intentFilter.addAction(MusicService.BUFFER_UPDATE_CHANGED);
        registerReceiver(receiver, intentFilter);
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void initBaseView() {
        slidingPanelLayout = (SlidingPanelLayout) LayoutInflater.from(this).inflate(R.layout.view_music_content, null);
        slidingPanelLayout.addView(contentView, 0);
        setContentView(slidingPanelLayout);
        super.initBaseView();
    }


    private View contentView;


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        contentView = LayoutInflater.from(this).inflate(layoutResID, null);
        if (slidingPanelLayout != null) {
            super.setContentView(layoutResID);
        }
    }


    @Override
    public void setContentView(View view) {
        if (contentView == null) {
            contentView = view;
        }
        if (slidingPanelLayout != null) {
            super.setContentView(slidingPanelLayout);
        }
    }

    /**
     * @param show 显示或关闭底部播放控制栏
     */
    protected void showBottomFragment(boolean show) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (show) {
            if (baseFragment == null) {
                baseFragment = BottomFragment.newInstance();
                ft.add(R.id.fl_view_music_content_bottom, baseFragment).show(baseFragment).commitAllowingStateLoss();
            } else {
                ft.show(baseFragment).commitAllowingStateLoss();
            }
        } else {
            if (baseFragment != null)
                ft.hide(baseFragment).commitAllowingStateLoss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        MusicManager.getInstance().unBindService(this);
    }

    private class MusicBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                MusicStatusEvent musicStatusEvent = new MusicStatusEvent();
                MusicStatusEvent.MusicContent musicContent = new MusicStatusEvent.MusicContent();
                musicContent.setId(intent.getLongExtra("id", 0));
                musicContent.setAlbumName(intent.getStringExtra("albumName"));
                musicContent.setSongName(intent.getStringExtra("songName"));
                musicContent.setArtistName(intent.getStringExtra("artistName"));
                musicContent.setPlaying(intent.getBooleanExtra("isPlaying", false));
                musicContent.setMaxProgress(intent.getLongExtra("maxProgress", 0));
                musicContent.setAlbumUrl(intent.getStringExtra("albumUrl"));
                musicContent.setMode(intent.getIntExtra("mode", 0));
                musicContent.setSecondProgress(intent.getIntExtra("buffer_update", 0));
                musicStatusEvent.setMusicContent(musicContent);
                switch (action) {
                    case MusicService.META_CHANGED:
                        CommonLogger.e("状态" + MusicService.META_CHANGED);
                        musicStatusEvent.setCurrentStatus(MusicStatusEvent.META_CHANGED);
                        break;
                    case MusicService.PLAYSTATE_CHANGED:
                        CommonLogger.e("状态" + MusicService.PLAYSTATE_CHANGED);
                        musicStatusEvent.setCurrentStatus(MusicStatusEvent.PLAYSTATE_CHANGED);
                        break;
                    case MusicService.BUFFER_UPDATE_CHANGED:
                        CommonLogger.e("状态" + MusicService.BUFFER_UPDATE_CHANGED);
                        musicStatusEvent.setCurrentStatus(MusicStatusEvent.BUFFER_UPDATE_CHANGED);
                        break;
                }
                RxBusManager.getInstance().post(musicStatusEvent);
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (slidingPanelLayout.getPanelState() == SlidingPanelLayout.PanelState.EXPANDED) {
            slidingPanelLayout.setPanelState(SlidingPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }
}
