package com.example.cootek.newfastframe;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.view.KeyEvent;

/**
 * Created by COOTEK on 2017/8/8.
 */

public class MediaButtonIntentReceiver extends WakefulBroadcastReceiver {


    private static boolean mDown = false;
    private static int mClickCounter = 0;
    private static long mLastClickTime = 0;
    private static final int LONG_PRESS_DELAY = 1000;
    private static final int DOUBLE_CLICK = 800;
    private static final int MSG_LONGPRESS_TIMEOUT = 1;
    private static final int MSG_HEADSET_DOUBLE_CLICK_TIMEOUT = 2;
    private static boolean mLaunched = false;
    private static PowerManager.WakeLock mWakeLock = null;
    private static Handler mHandler = new Handler() {

        /**
         * {@inheritDoc}
         */
        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case MSG_LONGPRESS_TIMEOUT:
                    if (!mLaunched) {
                        final Context context = (Context) msg.obj;
                        final Intent i = new Intent();
                        i.setClass(context, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                        mLaunched = true;
                    }
                    break;

                case MSG_HEADSET_DOUBLE_CLICK_TIMEOUT: //双击时间阈值内
                    final int clickCount = msg.arg1;
                    final String command;

                    switch (clickCount) {
                        case 1:
                            command = MusicService.MEDIA_BUTTON_TOGGLE_PAUSE;
                            break;
                        case 2:
                            command = MusicService.MEDIA_BUTTON_NEXT;
                            break;
                        case 3:
                            command = MusicService.MEDIA_BUTTON_PREVIOUS;
                            break;
                        default:
                            command = null;
                            break;
                    }

                    if (command != null) {
                        final Context context = (Context) msg.obj;
                        startService(context, command);
                    }
                    break;
            }
            releaseWakeLockIfHandlerIdle();
        }
    };

    private static void releaseWakeLockIfHandlerIdle() {
        if (mHandler.hasMessages(MSG_LONGPRESS_TIMEOUT)
                || mHandler.hasMessages(MSG_HEADSET_DOUBLE_CLICK_TIMEOUT)) {
            return;
        }

        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }

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
            int keyAction = keyEvent.getAction();
            long eventTime = keyEvent.getEventTime();
            String command = null;
            switch (keyCode) {
                case KeyEvent.KEYCODE_MEDIA_STOP:
                    command = MusicService.MEDIA_BUTTON_STOP;
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    command = MusicService.MEDIA_BUTTON_PAUSE;
                    break;
                case KeyEvent.KEYCODE_HEADSETHOOK:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    command = MusicService.MEDIA_BUTTON_TOGGLE_PAUSE;
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    command = MusicService.MEDIA_BUTTON_PLAY;
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    command = MusicService.MEDIA_BUTTON_NEXT;
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    command = MusicService.MEDIA_BUTTON_PREVIOUS;
                    break;

            }
            if (command != null) {
                if (keyAction == KeyEvent.ACTION_DOWN) {
                    if (mDown) {
                        if (MusicService.MEDIA_BUTTON_TOGGLE_PAUSE.equals(command)
                                || MusicService.MEDIA_BUTTON_PLAY.equals(command)) {
                            if (mLastClickTime != 0
                                    && eventTime - mLastClickTime > LONG_PRESS_DELAY) {
                                acquireWakeLockAndSendMessage(context,
                                        mHandler.obtainMessage(MSG_LONGPRESS_TIMEOUT, context), 0);
                            }
                        }
                    } else if (keyEvent.getRepeatCount() == 0) {

                        if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK) {
                            if (eventTime - mLastClickTime >= DOUBLE_CLICK) {
                                mClickCounter = 0;
                            }

                            mClickCounter++;
                            mHandler.removeMessages(MSG_HEADSET_DOUBLE_CLICK_TIMEOUT);

                            Message msg = mHandler.obtainMessage(
                                    MSG_HEADSET_DOUBLE_CLICK_TIMEOUT, mClickCounter, 0, context);

                            long delay = mClickCounter < 3 ? DOUBLE_CLICK : 0;
                            if (mClickCounter >= 3) {
                                mClickCounter = 0;
                            }
                            mLastClickTime = eventTime;
                            acquireWakeLockAndSendMessage(context, msg, delay);
                        } else {
                            startService(context, command);
                        }
                        mLaunched = false;
                        mDown = true;
                    }
                } else {
                    mHandler.removeMessages(MSG_LONGPRESS_TIMEOUT);
                    mDown = false;
                }
                if (isOrderedBroadcast()) {
                    abortBroadcast();
                }
                releaseWakeLockIfHandlerIdle();
            }
        }
    }

    private static void acquireWakeLockAndSendMessage(Context context, Message msg, long delay) {
        if (mWakeLock == null) {
            Context appContext = context.getApplicationContext();
            PowerManager pm = (PowerManager) appContext.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Listener headset button");
            mWakeLock.setReferenceCounted(false); //设置无论请求多少次vakelock,都只需一次释放
        }
        mWakeLock.acquire(10000); //防止无期限hold住wakelock

        mHandler.sendMessageDelayed(msg, delay);
    }

    private static void startService(Context context, String command) {
        Intent intent = new Intent(context, MusicService.class);
        intent.setAction(MusicService.MEDIA_BUTTON_ACTION);
        intent.putExtra(MusicService.COMMAND, command);
        startWakefulService(context, intent);
    }
}
