package com.example.cootek.newfastframe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.MusicService;
import com.example.cootek.newfastframe.event.MusicStatusEvent;

/**
 * Created by COOTEK on 2017/9/2.
 */

public class MusicInfoReceiver extends BroadcastReceiver {
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
                case MusicService.REFRESH_DATA:
                    CommonLogger.e("状态" + MusicService.REFRESH_DATA);
                    musicStatusEvent.setCurrentStatus(MusicStatusEvent.REFRESH_DATA);
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
