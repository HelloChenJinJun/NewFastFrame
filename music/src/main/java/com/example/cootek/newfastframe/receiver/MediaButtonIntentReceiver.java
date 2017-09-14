package com.example.cootek.newfastframe.receiver;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.view.KeyEvent;

import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.MusicService;

/**
 * Created by COOTEK on 2017/8/8.
 */

public class MediaButtonIntentReceiver extends WakefulBroadcastReceiver {


    /**
     * 音频按钮事前监听器
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
//            耳机拔出的时候停止播放
            startService(context, MusicService.MEDIA_BUTTON_PAUSE);
        } else if (action.equals(Intent.ACTION_MEDIA_BUTTON)) {
//            耳机按钮action
            KeyEvent keyEvent = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (keyEvent == null) {
                return;
            }
            int keyCode = keyEvent.getKeyCode();
            String command = null;
            switch (keyCode) {
                case KeyEvent.KEYCODE_MEDIA_STOP:
                    CommonLogger.e("耳机按停止");
                    command = MusicService.MEDIA_BUTTON_STOP;
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    CommonLogger.e("耳机按暂停");
                    command = MusicService.MEDIA_BUTTON_PAUSE;
                    break;
                case KeyEvent.KEYCODE_HEADSETHOOK:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    CommonLogger.e("耳机按切换");
                    command = MusicService.MEDIA_BUTTON_TOGGLE_PAUSE;
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    CommonLogger.e("耳机按播放");
                    command = MusicService.MEDIA_BUTTON_PLAY;
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    CommonLogger.e("耳机按下一首");
                    command = MusicService.MEDIA_BUTTON_NEXT;
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    CommonLogger.e("耳机按前一首");
                    command = MusicService.MEDIA_BUTTON_PREVIOUS;
                    break;
            }
            startService(context, command);
        }
    }

    private static void startService(Context context, String command) {
        Intent intent = new Intent(context, MusicService.class);
        intent.setAction(MusicService.MEDIA_BUTTON_ACTION);
        intent.putExtra(MusicService.COMMAND, command);
        startWakefulService(context, intent);
    }
}
