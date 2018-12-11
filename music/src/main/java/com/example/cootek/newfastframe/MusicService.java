package com.example.cootek.newfastframe;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.manager.music.IMusicPlayer;
import com.example.commonlibrary.manager.music.MusicPlayerManager;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.rxbus.event.PlayStateEvent;
import com.example.commonlibrary.utils.DensityUtil;
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
    private MusicPlayerManager mMusicPlayerManager;
    private MusicBinder mMusicBinder;
    private RemoteViews remoteViews;
    private Disposable mDisposable;


    @Override
    public void onCreate() {
        super.onCreate();
        mMusicPlayerManager = MusicPlayerManager.getInstance();
        mMusicBinder = new MusicBinder();
        updateNotification();
        mDisposable = RxBusManager.getInstance().registerEvent(PlayStateEvent.class, playStateEvent -> updateNotification());
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

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
            }
        }


        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        RxBusManager.getInstance().post(new PlayStateEvent(MusicPlayerManager.PLAY_STATE_ERROR));
        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            mDisposable = null;
        }
        MusicPlayerManager.getInstance().release();
    }

    private void updateNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(getContentIntent())
                .setCustomContentView(getContentView())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true);
        startForeground(NOTIFICATION_ID, builder.build());
    }

    private RemoteViews getContentView() {
        if (remoteViews == null) {
            remoteViews = new RemoteViews(getPackageName(), R.layout.view_music_service_notification);
            remoteViews.setOnClickPendingIntent(R.id.iv_notification_pre, PendingIntent.getService(this, 0, new Intent(PLAY_ACTION_PRE), 0));
            remoteViews.setOnClickPendingIntent(R.id.iv_notification_play, PendingIntent.getService(this, 0, new Intent(PLAY_ACTION_PLAY), 0));
            remoteViews.setOnClickPendingIntent(R.id.iv_notification_next, PendingIntent.getService(this, 0, new Intent(PLAY_ACTION_NEXT), 0));
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
            mMusicPlayerManager.play(musicPlayBean, seekPosition);
        }

        @Override
        public void play(List<MusicPlayBean> musicPlayBeans, int position, long seekPosition) {
            mMusicPlayerManager.play(musicPlayBeans, position, seekPosition);
        }

        @Override
        public void play(long seekPosition) {
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
