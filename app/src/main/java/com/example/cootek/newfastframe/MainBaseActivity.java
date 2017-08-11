package com.example.cootek.newfastframe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.commonlibrary.mvp.BaseActivity;
import com.example.commonlibrary.utils.CommonLogger;

/**
 * Created by COOTEK on 2017/8/11.
 */

public abstract class MainBaseActivity<T> extends BaseActivity<T> {


    protected MusicBroadCastReceiver receiver;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MusicManager.getInstance().bindService(this);
        receiver = new MusicBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicService.META_CHANGED);
        intentFilter.addAction(MusicService.PLAYLIST_CHANGED);
        intentFilter.addAction(MusicService.PLAYSTATE_CHANGED);
        intentFilter.addAction(MusicService.POSITION_CHANGED);
        intentFilter.addAction(MusicService.QUEUE_CHANGED);
        intentFilter.addAction(MusicService.REFRESH_CHANGED);
        intentFilter.addAction(MusicService.REPEATMODE_CHANGED);
        intentFilter.addAction(MusicService.SHUFFLEMODE_CHANGED);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        MusicManager.getInstance().unBindService(this);
    }

    private class MusicBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case MusicService.META_CHANGED:
                        CommonLogger.e("状态" + MusicService.META_CHANGED);
                        break;
                    case MusicService.PLAYLIST_CHANGED:
                        CommonLogger.e("状态" + MusicService.PLAYLIST_CHANGED);
                        break;
                    case MusicService.PLAYSTATE_CHANGED:
                        CommonLogger.e("状态" + MusicService.PLAYSTATE_CHANGED);
                        break;
                    case MusicService.POSITION_CHANGED:
                        CommonLogger.e("状态" + MusicService.POSITION_CHANGED);
                        break;
                    case MusicService.QUEUE_CHANGED:
                        CommonLogger.e("状态" + MusicService.QUEUE_CHANGED);
                        break;
                    case MusicService.REFRESH_CHANGED:
                        CommonLogger.e("状态" + MusicService.REFRESH_CHANGED);
                        break;
                    case MusicService.REPEATMODE_CHANGED:
                        CommonLogger.e("状态" + MusicService.REPEATMODE_CHANGED);
                        break;
                    case MusicService.SHUFFLEMODE_CHANGED:
                        CommonLogger.e("状态" + MusicService.SHUFFLEMODE_CHANGED);
                        break;
                }
            }
        }
    }
}
