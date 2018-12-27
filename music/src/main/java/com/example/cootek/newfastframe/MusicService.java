package com.example.cootek.newfastframe;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.manager.music.IMusicPlayer;
import com.example.commonlibrary.manager.music.MusicPlayerManager;
import com.example.commonlibrary.manager.video.DefaultVideoPlayer;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.rxbus.event.PlayStateEvent;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.DensityUtil;
import com.example.cootek.newfastframe.mvp.lock.LockScreenActivity;
import com.example.cootek.newfastframe.ui.MainActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import io.reactivex.disposables.Disposable;


/**
 * Created by 陈锦军 on 2017/8/7.
 */

public class MusicService extends Service {


    private static final String PLAY_ACTION_PRE = "com.example.music.PRE";
    private static final String PLAY_ACTION_PLAY = "com.example.music.PLAY";
    private static final String PLAY_ACTION_NEXT = "com.example.music.NEXT";
    private static final int NOTIFICATION_ID = 10;
    private static final String PLAY_ACTION_CANCEL = "com.example.music.CANCEL";
    private MusicPlayerManager mMusicPlayerManager;
    private MusicBinder mMusicBinder;
    private RemoteViews remoteViews;
    private Disposable mDisposable;
    private MediaSessionCompat mediaSessionCompat;

    @Override
    public void onCreate() {
        super.onCreate();
        mMusicPlayerManager = MusicPlayerManager.getInstance();
        mMusicBinder = new MusicBinder();
        //        setUpMediaSession();
        updateNotification();
        mDisposable = RxBusManager.getInstance().registerEvent(PlayStateEvent.class, playStateEvent -> {

            if (playStateEvent.getPlayState() != MusicPlayerManager.PLAY_STATE_IDLE &&
                    playStateEvent.getPlayState() != MusicPlayerManager.PLAY_STATE_ERROR) {
                updateNotification();
            }

        });
        initLockScreen();
    }


    private BroadcastReceiver lockScreenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                if (mMusicBinder.getUrl() != null) {
                    LockScreenActivity.start(getBaseContext());
                }
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

            }


        }
    };


    private void initLockScreen() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(lockScreenReceiver, intentFilter);
    }


    private void setUpMediaSession() {
        mediaSessionCompat = new MediaSessionCompat(this, "listener");
        mediaSessionCompat.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                CommonLogger.e("mediaSession:onPlay");
                mMusicBinder.play(0);

            }

            @Override
            public void onPause() {
                CommonLogger.e("mediaSession:onPause");
                mMusicBinder.pause();
            }

            @Override
            public void onSkipToNext() {
                CommonLogger.e("mediaSession:onSkipToNext");
                mMusicBinder.next();
            }

            @Override
            public void onSkipToPrevious() {
                CommonLogger.e("mediaSession:onSkipToPrevious");
                mMusicBinder.pre();
            }

            @Override
            public void onStop() {
                mMusicBinder.pause();
            }

            @Override
            public void onSeekTo(long pos) {
                CommonLogger.e("mediaSession:onSeekTo");
            }
        });
        mediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSessionCompat.setActive(true);
    }

    private void updateMediaSession(PlayStateEvent state) {
        int playState = PlaybackStateCompat.STATE_PLAYING;
        if (state.getPlayState() == DefaultVideoPlayer.PLAY_STATE_BUFFERING_PLAYING || state
                .getPlayState() == DefaultVideoPlayer.PLAY_STATE_PLAYING) {
            playState = PlaybackStateCompat.STATE_PLAYING;
        } else if (state.getPlayState() ==
                DefaultVideoPlayer.PLAY_STATE_BUFFERING_PAUSE || state.getPlayState() == DefaultVideoPlayer.PLAY_STATE_PAUSE) {
            playState = PlaybackStateCompat.STATE_PAUSED;
        }

        if (state.getPlayState() == DefaultVideoPlayer.
                PLAY_STATE_PAUSE || state.getPlayState()
                == DefaultVideoPlayer.PLAY_STATE_BUFFERING_PAUSE ||
                state.getPlayState() == DefaultVideoPlayer.PLAY_STATE_BUFFERING_PLAYING ||
                state.getPlayState() == DefaultVideoPlayer.PLAY_STATE_PLAYING) { //播放状态改变时
            mediaSessionCompat.setPlaybackState(new PlaybackStateCompat.Builder()
                    .setState(playState, mMusicBinder.getPosition(), 1.0f)
                    .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_PLAY_PAUSE |
                            PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                    .build());
        } else if (state.getPlayState() == DefaultVideoPlayer.PLAY_STATE_PREPARED) { //当前播放歌曲的信息或者播放队列改变
            MusicPlayBean musicPlayBean = MusicPlayerManager.getInstance().getMusicPlayBean();
            if (musicPlayBean.getIsLocal()) {
                refreshMediaSession(playState, BitmapFactory.decodeResource(getResources(), R.drawable.icon_album_default));
            } else {
                Glide.with(BaseApplication.getInstance())
                        .asBitmap().load(musicPlayBean.getAlbumUrl()).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        refreshMediaSession(PlaybackStateCompat.STATE_PLAYING, resource);
                    }
                });
            }
        }
    }


    private void refreshMediaSession(int playState, Bitmap resource) {
        if (resource == null) {
            resource = BitmapFactory.decodeResource(getResources(), R.drawable.icon_album_default);
        }
        MusicPlayBean musicPlayBean = MusicPlayerManager.getInstance().getMusicPlayBean();
        mediaSessionCompat.setMetadata(new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, musicPlayBean.getArtistName())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, musicPlayBean.getArtistName())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, musicPlayBean.getAlbumName())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, musicPlayBean.getSongName())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, musicPlayBean.getDuration())
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, resource)
                .build());
        mediaSessionCompat.setPlaybackState(new PlaybackStateCompat.Builder()
                .setState(playState, mMusicBinder.getPosition(), 1.0f)
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_PLAY_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                .build());
    }


    private int startId = -1;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startId = startId;
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case PLAY_ACTION_PRE:
                    mMusicBinder.pre();
                    break;
                case PLAY_ACTION_PLAY:
                    if (mMusicBinder.getCurrentState() == MusicPlayerManager.PLAY_STATE_PLAYING) {
                        mMusicBinder.pause();
                    } else {
                        mMusicBinder.play(0);
                    }
                    break;
                case PLAY_ACTION_NEXT:
                    mMusicBinder.next();
                    break;
                case PLAY_ACTION_CANCEL:
                    cancelNotification();
            }
        }


        return START_STICKY;
    }

    private void cancelNotification() {
        stopForeground(true);
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(NOTIFICATION_ID);
        mMusicBinder.release();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        CommonLogger.e("service:onUnbind");
        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        CommonLogger.e("service:onDestroy");
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            mDisposable = null;
        }
        if (mediaSessionCompat != null) {
            mediaSessionCompat.setActive(false);
            mediaSessionCompat.release();
        }

        if (lockScreenReceiver != null) {
            unregisterReceiver(lockScreenReceiver);
        }
        MusicPlayerManager.getInstance().release();
    }

    private void updateNotification() {
        CommonLogger.e("updateNotification");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(getContentIntent())
                .setCustomContentView(getContentView());
        startForeground(NOTIFICATION_ID, builder.build());

        //        if (playStateEvent != null) {
        //            updateMediaSession(playStateEvent);
        //        }

    }

    private RemoteViews getContentView() {
        if (remoteViews == null) {
            remoteViews = new RemoteViews(getPackageName(), R.layout.view_music_service_notification);
            remoteViews.setOnClickPendingIntent(R.id.iv_notification_pre, PendingIntent.getService(this, 0, new Intent(PLAY_ACTION_PRE), 0));
            remoteViews.setOnClickPendingIntent(R.id.iv_notification_play, PendingIntent.getService(this, 0, new Intent(PLAY_ACTION_PLAY), 0));
            remoteViews.setOnClickPendingIntent(R.id.iv_notification_next, PendingIntent.getService(this, 0, new Intent(PLAY_ACTION_NEXT), 0));
            remoteViews.setOnClickPendingIntent(R.id.iv_notification_cancel, PendingIntent.getService(this, 0, new Intent(PLAY_ACTION_CANCEL), 0));
        } else {
            MusicPlayBean musicPlayBean = MusicPlayerManager.getInstance().getMusicPlayBean();
            if (musicPlayBean != null) {
                remoteViews.setTextViewText(R.id.tv_notification_title, musicPlayBean.getSongName());
                remoteViews.setTextViewText(R.id.tv_notification_sub_title, musicPlayBean.getArtistName());
                remoteViews.setImageViewResource(R.id.iv_notification_play, mMusicPlayerManager.getCurrentState() == MusicPlayerManager.PLAY_STATE_PLAYING ?
                        R.drawable.ic_pause_black : R.drawable.ic_play_black);
                if (musicPlayBean.getIsLocal()) {
                    remoteViews.setImageViewBitmap(R.id.iv_notification_icon, MusicInfoProvider.getAlbumsBitmap(musicPlayBean.getAlbumId()));
                } else {
                    Glide.with(BaseApplication.getInstance())
                            .asBitmap().load(musicPlayBean.getAlbumUrl()).into(new SimpleTarget<Bitmap>(DensityUtil.toDp(50), DensityUtil.toDp(50)) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            remoteViews.setImageViewBitmap(R.id.iv_notification_icon, resource);
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(MusicService.this);
                            builder.setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentIntent(getContentIntent())
                                    .setCustomContentView(remoteViews)
                                    .setPriority(NotificationCompat.PRIORITY_MAX)
                                    .setOngoing(true);
                            startForeground(NOTIFICATION_ID, builder.build());
                        }
                    });
                }
            }

        }
        return remoteViews;
    }

    private PendingIntent getContentIntent() {
        return PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMusicBinder;
    }


    public class MusicBinder extends Binder implements IMusicPlayer {


        @Override
        public void play(MusicPlayBean musicPlayBean, long seekPosition) {
            //            mediaSessionCompat.setActive(true);
            mMusicPlayerManager.play(musicPlayBean, seekPosition);
        }

        @Override
        public void play(List<MusicPlayBean> musicPlayBeans, int position, long seekPosition) {
            //            mediaSessionCompat.setActive(true);
            mMusicPlayerManager.play(musicPlayBeans, position, seekPosition);
        }

        @Override
        public void play(long seekPosition) {
            //            mediaSessionCompat.setActive(true);
            mMusicPlayerManager.play(seekPosition);
        }

        @Override
        public void pause() {
            mMusicPlayerManager.pause();
        }

        @Override
        public void seekTo(int position) {
            mMusicPlayerManager.seekTo(position);
        }

        @Override
        public int getDuration() {
            return mMusicPlayerManager.getDuration();
        }

        @Override
        public int getPosition() {
            return mMusicPlayerManager.getPosition();
        }

        @Override
        public int getBufferedPercentage() {
            return mMusicPlayerManager.getBufferedPercentage();
        }

        @Override
        public void setPlayMode(int playMode) {
            mMusicPlayerManager.setPlayMode(playMode);
        }

        @Override
        public void next() {
            mMusicPlayerManager.next();
        }

        @Override
        public void pre() {
            mMusicPlayerManager.pre();
        }

        @Override
        public int getCurrentState() {
            return mMusicPlayerManager.getCurrentState();
        }

        @Override
        public void reset() {
            mMusicPlayerManager.reset();
        }

        @Override
        public void release() {
            mMusicPlayerManager.release();
        }

        @Override
        public String getUrl() {
            return mMusicPlayerManager.getUrl();
        }

        @Override
        public int getPlayMode() {
            return mMusicPlayerManager.getPlayMode();
        }
    }
}
