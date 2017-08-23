package com.example.cootek.newfastframe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.commonlibrary.mvp.BaseActivity;
import com.example.commonlibrary.mvp.BasePresenter;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;

/**
 * Created by COOTEK on 2017/8/11.
 */

public abstract class MainBaseActivity<T, P extends BasePresenter> extends BaseActivity<T, P> {


    protected MusicBroadCastReceiver receiver;


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
}
