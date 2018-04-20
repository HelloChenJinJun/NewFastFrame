package com.example.cootek.newfastframe;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import com.example.commonlibrary.bean.chat.DaoSession;
import com.example.commonlibrary.bean.music.MusicHistoryInfo;
import com.example.commonlibrary.bean.music.MusicHistoryInfoDao;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.bean.music.MusicPlayBeanDao;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.FileUtil;
import com.example.commonlibrary.utils.Httputil;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.cootek.newfastframe.receiver.MediaButtonIntentReceiver;
import com.example.cootek.newfastframe.ui.MainActivity;
import com.example.cootek.newfastframe.util.MusicUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;


/**
 * Created by 陈锦军 on 2017/8/7.
 */

public class MusicService extends Service {

    public static final int MAX_HISTORY_SIZE = 1000;
    public static final String MEDIA_BUTTON_PAUSE = "pause";
    public static final String MEDIA_BUTTON_ACTION = "media_button_action";
    public static final String COMMAND = "command";
    public static final String MEDIA_BUTTON_TOGGLE_PAUSE = "toggle_pause";
    public static final String MEDIA_BUTTON_STOP = "stop";
    public static final String MEDIA_BUTTON_PLAY = "play";
    public static final String MEDIA_BUTTON_NEXT = "next";
    public static final String MEDIA_BUTTON_PREVIOUS = "previous";
    public static final String META_CHANGED = "meta_changed";
    public static final String REFRESH_DATA = "refresh_data";
    public static final String PLAYSTATE_CHANGED = "play_state_changed";
    public static final int MODE_NORMAL = 0;
    public static final int MODE_LOOP = 1;
    public static final int MODE_SHUFFLE = 2;
    public static final String BUFFER_UPDATE_CHANGED = "butter_update_changed";

    private int mode = MODE_NORMAL;
    public static final int FADEDOWN = 6;
    public static final int FADEUP = 7;
    public static final String PREVIOUS_ACTION = "previous_action";
    public static final String NEXT_ACTION = "next_action";
    public static final String TOGGLEPAUSE_ACTION = "toggle_pause_action";
    private MusicServiceStub bind = new MusicServiceStub(this);
    private NotificationManagerCompat notificationManagerCompat;
    private DaoSession daoSession;
    private HandlerThread handlerThread;
    private MusicServiceHandler musicServiceHandler;
    private MediaSessionCompat mediaSessionCompat;
    private long notificationPostTime = 0;
    private PowerManager.WakeLock mWakeLock;
    private int mStartId;
    private WrappedMediaPlayer wrappedMediaPlayer;
    private SharedPreferences mPreferences;
    private ArrayList<MusicPlayBean> mPlaylist = new ArrayList<>(100);
    private int playPosition = -1;
    private int mOpenFailedCounter = 0;
    private static LinkedList<Integer> mHistory = new LinkedList<>();
    private boolean isPlaying = false;
    private Shuffler mShuffler = new Shuffler();
    private int nextPlayPosition = -1;
    private float mCurrentVolume = 1.0f;
    private int notificationId;

    @Override
    public void onCreate() {
        super.onCreate();
//        发送前台通知
        CommonLogger.e("服务的onCreate1");
        notificationManagerCompat = NotificationManagerCompat.from(this);
        daoSession = VideoApplication.getMainComponent().getDaoSession();
        handlerThread = new HandlerThread("music_handler_thread", Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();
        musicServiceHandler = new MusicServiceHandler(handlerThread.getLooper(), this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//            桌面锁屏的会话回调
            setUpMediaSession();
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.registerMediaButtonEventReceiver(new ComponentName(getPackageName(),
                MediaButtonIntentReceiver.class.getName()));
        wrappedMediaPlayer = new WrappedMediaPlayer(this);
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
        mWakeLock.setReferenceCounted(false);
        mPreferences = getSharedPreferences(MusicUtil.SHARED_PREFERENCES_NAME, 0);
    }


    private void reloadQueue() {
        List<MusicPlayBean> list = daoSession.getMusicPlayBeanDao().queryBuilder().where(MusicPlayBeanDao.Properties.IsRecent.eq(Boolean.TRUE)).list();
        if (list.size() > 0) {
            mPlaylist.clear();
            mPlaylist.addAll(list);
        }
        if (mPlaylist.size() > 0) {
            final int pos = mPreferences.getInt(MusicUtil.POSITION, 0); //记录了上次退出时播放曲目在播放队列中的位置
            if (pos < 0 || pos >= mPlaylist.size()) {
                mPlaylist.clear();
                return;
            }
            playPosition = pos;
            openCurrentAndNext();
            long seekpos = mPreferences.getLong(MusicUtil.SEEK, 0);
            seek(seekpos >= 0 && seekpos < duration() ? seekpos : 0);
            mode = mPreferences.getInt(MusicUtil.PLAY_MODE, 0);
            if (mode == MODE_SHUFFLE) {
//                取出历史播放列表
                mHistory.clear();
                List<MusicHistoryInfo> historyInfos = daoSession.getMusicHistoryInfoDao().queryBuilder().where(MusicHistoryInfoDao.Properties.Position.le(mPlaylist.size())).build().list();
                for (MusicHistoryInfo info :
                        historyInfos) {
                    mHistory.add(info.getPosition());
                }
            }
        }
    }

    private long duration() {
        CommonLogger.e("获取歌曲的时长");
        return wrappedMediaPlayer.duration();
    }


    private void notifyChange(String action) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            updateMediaSession(action);
        final Intent intent = new Intent(action);
        if (!action.equals(BUFFER_UPDATE_CHANGED)) {
            intent.putExtra("id", getAudioId());
            intent.putExtra("artistName", getArtistName());
            intent.putExtra("albumUrl", getAlbumUrl());
            intent.putExtra("albumName", getAlbumName());
            intent.putExtra("isPlaying", isPlaying());
            intent.putExtra("songName", getSongName());
            intent.putExtra("maxProgress", duration());
            intent.putExtra("mode", getPlayMode());
        } else {
            intent.putExtra("buffer_update", wrappedMediaPlayer.getSecondProgress());
        }
        sendBroadcast(intent);
        CommonLogger.e("已经发送广播");
        switch (action) {
            case META_CHANGED:
            case REFRESH_DATA:
                updateNotification();
                break;
            case PLAYSTATE_CHANGED:
                updateNotification();
                saveQueue(false);
                break;
            default:
                saveQueue(false);
                break;
        }
    }

    private int getPlayMode() {
        return mode;
    }

    private String getAlbumUrl() {
        String result = getCurrentMusicPlayInfo() != null ? getCurrentMusicPlayInfo().getAlbumUrl() : null;
        CommonLogger.e("service:" + result);
        return result;
    }

    private void updateMediaSession(String action) {
        CommonLogger.e("updateMediaSession：" + action);
        final int playState = isPlaying
                ? PlaybackStateCompat.STATE_PLAYING
                : PlaybackStateCompat.STATE_PAUSED;
        if (action.equals(PLAYSTATE_CHANGED)) { //播放状态改变时
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mediaSessionCompat.setPlaybackState(new PlaybackStateCompat.Builder()
                        .setState(playState, position(), 1.0f)
                        .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_PLAY_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                        .build());
            }
        } else if (action.equals(META_CHANGED)) { //当前播放歌曲的信息或者播放队列改变
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (getCurrentMusicPlayInfo() != null) {
                    if (getCurrentMusicPlayInfo().isLocal()) {
                        refreshMediaSession(playState, BitmapFactory.decodeResource(getResources(), R.drawable.icon_album_default));
                    } else {
//                        这里不能用Glide加载，在into方法中会出现判主线程的判断从而出错
                        String path = mPreferences.getString(String.valueOf(getAudioId()), null);
                        if (path == null || !new File(path).exists()) {
                            loadAlbum(new AlbumCallBack() {
                                @Override
                                public void onCallBack(Bitmap bitmap) {
                                    refreshMediaSession(playState, bitmap);
                                }
                            });
                        } else {
                            try {
                                refreshMediaSession(playState, BitmapFactory.decodeStream(new FileInputStream(new File(path))));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                CommonLogger.e("文件没有被找到" + e.getMessage());
                            }
                        }
                    }
                }
            }
        }
    }

    private void refreshMediaSession(int playState, Bitmap resource) {
        if (resource == null) {
            resource = BitmapFactory.decodeResource(getResources(), R.drawable.icon_album_default);
        }
        mediaSessionCompat.setMetadata(new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, getArtistName())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, getArtistName())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, getAlbumName())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, getSongName())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration())
                .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, getQueuePosition())
                .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, getQueue().length)
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, getGenreName())
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, resource)
                .build());
        mediaSessionCompat.setPlaybackState(new PlaybackStateCompat.Builder()
                .setState(playState, position(), 1.0f)
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_PLAY_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                .build());
    }


    /**
     * 获取歌曲所属类型
     *
     * @return GenreName
     */
    private String getGenreName() {
        synchronized (this) {
            if (getCurrentMusicPlayInfo() != null && !getCurrentMusicPlayInfo().isLocal()) {
                return null;
            }
            String[] genreProjection = {MediaStore.Audio.Genres.NAME};
            Uri genreUri = MediaStore.Audio.Genres.getContentUriForAudioId("external",
                    (int) mPlaylist.get(playPosition).getSongId());
            Cursor genreCursor = getContentResolver().query(genreUri, genreProjection,
                    null, null, null);
            if (genreCursor != null) {
                try {
                    if (genreCursor.moveToFirst()) {
                        return genreCursor.getString(
                                genreCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME));
                    }
                } finally {
                    genreCursor.close();
                }
            }
            return null;
        }

    }

    private long[] getQueue() {
        CommonLogger.e("获取现在的播放的队列");
        synchronized (this) {
            final int len = mPlaylist.size();
            final long[] list = new long[len];
            for (int i = 0; i < len; i++) {
                list[i] = mPlaylist.get(i).getSongId();
            }
            return list;
        }
    }

    private int getQueuePosition() {
        CommonLogger.e("获取现在播放队列的位置");
        synchronized (this) {
            return playPosition;
        }
    }

    private void saveQueue(boolean isAll) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        if (isAll) {
//            清除旧数据，保持新数据
            if (mode == MODE_SHUFFLE) {
                List<MusicHistoryInfo> list = new ArrayList<>();
                for (int i = 0; i < mHistory.size(); i++) {
                    MusicHistoryInfo musicHistoryInfo = new MusicHistoryInfo(mHistory.get(i));
                    list.add(musicHistoryInfo);
                }
                daoSession.getMusicHistoryInfoDao().deleteAll();
                if (list.size() > 0) {
                    daoSession.getMusicHistoryInfoDao().insertInTx(list);
                }
            }
            List<MusicPlayBean> lastPlayList = daoSession.getMusicPlayBeanDao().queryBuilder().where(MusicPlayBeanDao.Properties.IsRecent.eq(Boolean.TRUE)).list();

            if (lastPlayList.size() > 0) {
                for (MusicPlayBean bean :
                        lastPlayList) {
                    bean.setIsRecent(false);
                }
                daoSession.getMusicPlayBeanDao().updateInTx(lastPlayList);
            }
            daoSession.getMusicPlayBeanDao().insertOrReplaceInTx(mPlaylist);
        }
        editor.putInt("position", playPosition);
        if (wrappedMediaPlayer.isInitialized()) {
            editor.putLong("seek", wrappedMediaPlayer.position());
        }
        editor.putInt("play_mode", mode);
        editor.apply();
    }

    private void setUpMediaSession() {
        mediaSessionCompat = new MediaSessionCompat(this, "listener");
        mediaSessionCompat.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                CommonLogger.e("mediaSession:onPlay");
                play();

            }

            @Override
            public void onPause() {
                CommonLogger.e("mediaSession:onPause");
                pause();
            }

            @Override
            public void onSkipToNext() {
                CommonLogger.e("mediaSession:onSkipToNext");
                next(true);
            }

            @Override
            public void onSkipToPrevious() {
                CommonLogger.e("mediaSession:onSkipToPrevious");
                previous();
            }

            @Override
            public void onStop() {

            }

            @Override
            public void onSeekTo(long pos) {
                CommonLogger.e("mediaSession:onSeekTo");
            }
        });
        mediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
    }


    private void cancelNotification() {
        stopForeground(false);
        notificationManagerCompat.cancel(hashCode());
        notificationPostTime = 0;
    }

    private boolean isPlaying() {
        return isPlaying;
    }

    private long seek(long position) {
        if (wrappedMediaPlayer.isInitialized()) {
            if (position < 0) {
                position = 0;
            } else if (position > wrappedMediaPlayer.duration()) {
                position = wrappedMediaPlayer.duration();
            }
            return wrappedMediaPlayer.seek(position);
        }
        return -1;
    }

    private void next(boolean force) {
        stop(false);
        prepareNext(force);
        if (nextPlayPosition < 0) {
//            没有下一首
//            设置停止状态并通知
            setIsSupposedToBePlaying(false, true);
        } else {
            play();
        }
    }


    private void previous() {
        synchronized (this) {
            stop(false);
            int previousPosition = playPosition;
            playPosition = getPreviousPlayPosition(true);
            if (playPosition >= 0) {
                openCurrentAndMaybeNext(false);
                setNextMusicPlayInfo(previousPosition);
                play();
            } else {
                setIsSupposedToBePlaying(false, true);
            }
        }
    }


    /**
     * 获取播放列表中的前一首歌曲
     *
     * @param removeFromHistory 是否从历史中删除最近一个播放记录
     * @return 位置
     */
    public int getPreviousPlayPosition(boolean removeFromHistory) {
        CommonLogger.e("获取播放列表中的前一首歌曲");
        synchronized (this) {
            if (mode == MODE_SHUFFLE) { //普通随机模式,从历史中获取
                final int size = mHistory.size();
                if (size == 0) {
                    return -1;
                }
                final Integer pos = mHistory.get(size - 1);
                if (removeFromHistory) {
                    mHistory.remove(size - 1);
                }
                return pos;
            } else if (mode == MODE_LOOP) {
                return playPosition;
            } else {
                if (playPosition > 0) {
                    return playPosition - 1;
                } else {
                    return mPlaylist.size() - 1;
                }
            }
        }
    }

    private long position() {
        if (wrappedMediaPlayer.isInitialized) {
            return wrappedMediaPlayer.position();
        }
        return -1;
    }


    private void stop(boolean died) {
        CommonLogger.e("停止播放");
        if (wrappedMediaPlayer.isInitialized()) {
            wrappedMediaPlayer.stop();
        }
        if (died) {
            setIsSupposedToBePlaying(false, false);
        } else {
            CommonLogger.e("停止前台");
            stopForeground(false);
        }

    }


    /**
     * 是否重新获取下一首歌的位置,当播放前一首歌的时候，就不需要重新获取下一首歌的位置了
     */
    private void play() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mediaSessionCompat.setActive(true);
        if (wrappedMediaPlayer.isInitialized()) {
            CommonLogger.e("开始播放");
            wrappedMediaPlayer.start();
            CommonLogger.e("这里通过移除减弱的声音的消息和发送增加声音的消息来调整到正常的音量");
            musicServiceHandler.removeMessages(FADEDOWN);
            musicServiceHandler.sendEmptyMessage(FADEUP); //组件调到正常音量
            CommonLogger.e("设置播放状态,并通知播放状态的改变");
            setIsSupposedToBePlaying(true, true);
            notifyChange(META_CHANGED);
        } else {
            CommonLogger.e("播放的时候资源还没有准备或出错");
        }
    }


    /**
     * 更新通知栏
     */
    private void updateNotification() {
        if (!isPlaying()) {
            stopForeground(false);
        }
        notificationId = hashCode();
        if (notificationPostTime == 0) {
            if (getCurrentMusicPlayInfo() != null) {
                if (getCurrentMusicPlayInfo().isLocal()) {
                    startForeground(notificationId, getBuildNotification(BitmapFactory.decodeResource(getResources(), R.drawable.icon_album_default)));
                } else {
                    String imagePath = mPreferences.getString(String.valueOf(getAudioId()), null);
                    if (imagePath == null || !new File(imagePath).exists()) {
                        loadAlbum(new AlbumCallBack() {
                            @Override
                            public void onCallBack(Bitmap bitmap) {
                                startForeground(notificationId, getBuildNotification(bitmap));
                            }
                        });
                    } else {
                        startForeground(notificationId, getBuildNotification(BitmapFactory.decodeFile(MusicUtil.getMusicImageCacheDir() + getCurrentMusicPlayInfo().getSongId() + ".jpg")));
                    }
                }
            }
        } else {
            if (getCurrentMusicPlayInfo() != null) {
                if (getCurrentMusicPlayInfo().isLocal()) {
                    notificationManagerCompat.notify(notificationId, getBuildNotification(BitmapFactory.decodeResource(getResources(), R.drawable.icon_album_default)));
                } else {
                    String imagePath = mPreferences.getString(String.valueOf(getAudioId()), null);
                    if (imagePath == null || !new File(imagePath).exists()) {
                        loadAlbum(new AlbumCallBack() {
                            @Override
                            public void onCallBack(Bitmap bitmap) {
                                notificationManagerCompat.notify(notificationId, getBuildNotification(bitmap));
                            }
                        });
                    } else {
                        notificationManagerCompat.notify(notificationId, getBuildNotification(BitmapFactory.decodeFile(MusicUtil.getMusicImageCacheDir() + getCurrentMusicPlayInfo().getSongId() + ".jpg")));
                    }
                }
            }
        }
    }

    interface AlbumCallBack {
        void onCallBack(Bitmap bitmap);
    }

    private void loadAlbum(final AlbumCallBack albumCallBack) {
        new AsyncTask<String, Void, Bitmap>() {
            String songId;

            @Override
            protected Bitmap doInBackground(String... params) {
                try {
                    songId = params[1];
                    URL url = new URL(params[0]);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setConnectTimeout(3000);
                    httpURLConnection.setRequestMethod("GET");
                    if (httpURLConnection.getResponseCode() == 200) {
                        return BitmapFactory.decodeStream(httpURLConnection.getInputStream());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (albumCallBack != null) {
                    albumCallBack.onCallBack(bitmap);
                }
                try {
                    if (bitmap != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(FileUtil.newFile(MusicUtil.getMusicImageCacheDir() + songId + ".jpg")));
                        SharedPreferences.Editor editor = mPreferences.edit();
                        editor.putString(songId, MusicUtil.getMusicImageCacheDir() + songId + ".jpg");
                        editor.apply();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }.execute(getCurrentMusicPlayInfo().getAlbumUrl(), String.valueOf(getCurrentMusicPlayInfo().getSongId()));
    }

    private Notification getBuildNotification(Bitmap bitmap) {
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_album_default);
        }
        String albumName = getAlbumName();
        String artistName = getArtistName();
        boolean isPlaying = isPlaying();
        String text = TextUtils.isEmpty(albumName)
                ? artistName : artistName + " - " + albumName;

        int playButtonResId = isPlaying
                ? R.drawable.ic_pause_black_24dp : R.drawable.ic_play_arrow_black_24dp;
        if (notificationPostTime == 0) {
            notificationPostTime = System.currentTimeMillis();
        }
        android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext())
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.ic_music_note_white_24dp)
                .setContentIntent(getMainIntent())
                .setContentTitle(getSongName())
                .setContentText(text)
                .setOngoing(true)
                .setWhen(notificationPostTime)
                .addAction(R.drawable.ic_skip_previous_black_24dp,
                        "",
                        getPendingIntent(PREVIOUS_ACTION))
                .addAction(playButtonResId, "",
                        getPendingIntent(TOGGLEPAUSE_ACTION))
                .addAction(R.drawable.ic_skip_next_black_24dp,
                        "",
                        getPendingIntent(NEXT_ACTION));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            builder.setShowWhen(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationCompat.MediaStyle style = new NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSessionCompat.getSessionToken())
                    .setShowActionsInCompactView(0, 1, 2, 3);
            builder.setStyle(style);
        }
        return builder.build();
    }

    private PendingIntent getMainIntent() {
        Intent nowPlayingIntent = new Intent(this, MainActivity.class);
//        nowPlayingIntent.setComponent(new ComponentName("com.example.cootek.newfastframe","com.example.cootek.newfastframe."));
//        nowPlayingIntent.setAction(Constants.NAVIGATE_LIBRARY);
        return PendingIntent.getActivity(this, 0, nowPlayingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getPendingIntent(String action) {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(action);
        return PendingIntent.getService(this, 0, intent, 0);
    }

    private String getArtistName() {
        CommonLogger.e("获取歌手名");
        synchronized (this) {
            if (getCurrentMusicPlayInfo() != null) {
                return getCurrentMusicPlayInfo().getArtistName();
            }
            return null;
        }
    }

    private String getAlbumName() {
        CommonLogger.e("获取专辑名");
        synchronized (this) {
            if (getCurrentMusicPlayInfo() != null) {
                return getCurrentMusicPlayInfo().getAlbumName();
            }
            return null;
        }
    }


    private void setIsSupposedToBePlaying(boolean isPlaying, boolean notify) {
        if (this.isPlaying != isPlaying) {
            this.isPlaying = isPlaying;

            if (notify) {
                notifyChange(PLAYSTATE_CHANGED);
            }
        }
    }


    private void pause() {
        CommonLogger.e("暂停");
        synchronized (this) {
            musicServiceHandler.removeMessages(FADEUP);
            musicServiceHandler.sendEmptyMessage(FADEDOWN);
            wrappedMediaPlayer.pause();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        CommonLogger.e("服务的onStartCommand");
        mStartId = startId;
        if (intent != null) {
            String action = intent.getAction();
            String value = intent.getStringExtra(COMMAND);
            if (action != null) {
                switch (action) {
//                    媒体按钮
                    case MEDIA_BUTTON_ACTION:
                        switch (value) {
                            case MEDIA_BUTTON_PAUSE:
                                CommonLogger.e("MEDIA_BUTTON_PAUSE");
                                pause();
                                break;
                            case MEDIA_BUTTON_TOGGLE_PAUSE:
                                CommonLogger.e("MEDIA_BUTTON_TOGGLE_PAUSE");
                                if (isPlaying()) {
                                    pause();
                                } else {
                                    play();
                                }
                                break;
                            case MEDIA_BUTTON_NEXT:
                                CommonLogger.e("MEDIA_BUTTON_NEXT_next");
                                next(true);
                                break;
                            case MEDIA_BUTTON_PREVIOUS:
                                previous();
                                break;
                            case MEDIA_BUTTON_STOP:
                                pause();
                                seek(0);
                                break;
                            case MEDIA_BUTTON_PLAY:
                                play();
                                break;
                        }
                        break;
                    case PREVIOUS_ACTION:
                        ToastUtils.showShortToast("前一首");
                        previous();
                        break;
                    case NEXT_ACTION:
                        next(true);
                        break;
                    case TOGGLEPAUSE_ACTION:
                        if (isPlaying()) {
                            pause();
                        } else {
                            play();
                        }
                        break;
                }
            }
        }
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        saveQueue(true);
        musicServiceHandler.removeCallbacksAndMessages(null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            handlerThread.quitSafely();
        } else {
            handlerThread.quit();
        }
        wrappedMediaPlayer.release();
        wrappedMediaPlayer = null;
        cancelNotification();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mediaSessionCompat.setActive(false);
        mediaSessionCompat.release();
        mWakeLock.release();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        CommonLogger.e("1服务的onBind");
        reloadQueue();
        return bind;
    }


    @Override
    public void onRebind(Intent intent) {
    }

    @Override
    public boolean onUnbind(Intent intent) {
        CommonLogger.e("onUnbind");
        if (isPlaying()) {
            return true;
        }
        saveQueue(true);
        stopSelf(mStartId);

        return true;
    }

    private class MusicServiceStub extends IMusicService.Stub {
        private WeakReference<MusicService> musicServiceWeakReference;

        MusicServiceStub(MusicService service) {
            musicServiceWeakReference = new WeakReference<>(service);
        }


        @Override
        public void open(List<MusicPlayBean> list, int position) throws RemoteException {
            musicServiceWeakReference.get().open(list, position);
        }

        @Override
        public void stop() throws RemoteException {
            musicServiceWeakReference.get().stop();
        }

        @Override
        public void pause() throws RemoteException {
            musicServiceWeakReference.get().pause();
        }

        @Override
        public void play() throws RemoteException {
            musicServiceWeakReference.get().play();
        }

        @Override
        public void prev() throws RemoteException {
            musicServiceWeakReference.get().previous();
        }

        @Override
        public void next() throws RemoteException {
            musicServiceWeakReference.get().next(true);
        }

        @Override
        public void setPlayMode(int mode) throws RemoteException {
            musicServiceWeakReference.get().setPlayMode(mode);
        }


        @Override
        public void refresh() throws RemoteException {
            musicServiceWeakReference.get().refresh();
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return musicServiceWeakReference.get().isPlaying();
        }

        @Override
        public long[] getQueue() throws RemoteException {
            return musicServiceWeakReference.get().getQueue();
        }

        @Override
        public int getQueuePosition() throws RemoteException {
            return musicServiceWeakReference.get().getQueuePosition();
        }

        @Override
        public long duration() throws RemoteException {
            return musicServiceWeakReference.get().duration();
        }

        @Override
        public long position() throws RemoteException {
            return musicServiceWeakReference.get().position();
        }

        @Override
        public long seek(long pos) throws RemoteException {
            return musicServiceWeakReference.get().seek(pos);
        }

        @Override
        public MusicPlayBean getCurrentPlayInfo() throws RemoteException {
            return musicServiceWeakReference.get().getCurrentMusicPlayInfo();
        }

        @Override
        public MusicPlayBean getPlayInfo(int index) throws RemoteException {
            return musicServiceWeakReference.get().getMusicPlayInfo(index);
        }


        @Override
        public void remove(int position) throws RemoteException {
            musicServiceWeakReference.get().remove(position);
        }

        @Override
        public int getAudioSessionId() throws RemoteException {
            return musicServiceWeakReference.get().getAudioSessionId();
        }
    }

    private int getAudioSessionId() {
        return wrappedMediaPlayer.getAudioSessionId();
    }

    private void setPlayMode(int mode) {
        if (this.mode != mode) {
            this.mode = mode;
//            重新选择下一首歌曲
            setNextMusicPlayInfo();
        }
    }

    private void remove(int position) {
        if (mPlaylist != null && position >= 0 && position < mPlaylist.size()) {
            if (playPosition == position) {
                if (mPlaylist.size() == 1) {
                    mPlaylist.remove(position);
                    stop(true);
                } else {
                    mPlaylist.remove(position);
                    next(true);
                }
            } else if (nextPlayPosition == position) {
                if (mPlaylist.size() == 1) {
                    mPlaylist.remove(position);
                    stop(true);
                } else {
                    mPlaylist.remove(position);
//                    更新下一首歌资源
                    setNextMusicPlayInfo();
                }
            } else {
                mPlaylist.remove(position);
            }
        }
    }


    private void refresh() {
        notifyChange(REFRESH_DATA);
    }


    private void stop() {
        stop(true);
    }

    private void open(List<MusicPlayBean> list, int position) {
        if (!list.equals(mPlaylist)) {
            addToPlayList(list, -1); //添加到播放列表,并清空原播放列表
            CommonLogger.e("清空之前的播放队列后通知队列的改变");
        }
        if (position >= 0) {
            playPosition = position;
        } else {
            playPosition = mShuffler.nextInt(mPlaylist.size());
        }
        mHistory.clear();
        openCurrentAndMaybeNext(true);
    }


    /**
     * 添加所有的item到播放队列中
     *
     * @param list     播放列表
     * @param position -1表示清除之前的播放列表
     */
    private void addToPlayList(List<MusicPlayBean> list, int position) {
        final int size = list.size();
        if (position < 0) {
            mPlaylist.clear();
            position = 0;
        }
        mPlaylist.ensureCapacity(mPlaylist.size() + size);
        if (position > mPlaylist.size()) {
            position = mPlaylist.size();
        }
        mPlaylist.addAll(position, list);
        daoSession.getMusicPlayBeanDao().insertOrReplaceInTx(list);
    }

    /**
     * 根据path更新Cursor,和准备播放资源
     *
     * @param path   本地路径或网络路径
     * @param lrcUrl url
     * @return 成功与否
     */
    private boolean openFile(String path, String lrcUrl, long songId) {
        CommonLogger.e("打开文件的路径" + path);
        synchronized (this) {
            if (path == null) {
                return false;
            }
            wrappedMediaPlayer.setDataSource(path, lrcUrl, songId);
            if (wrappedMediaPlayer.isInitialized()) {
//                只要有一次成功，重置可失败的次数
                mOpenFailedCounter = 0;
                return true;
            }
            return false;
        }
    }


    private class MusicServiceHandler extends Handler {
        private WeakReference<MusicService> musicServiceWeakReference;

        MusicServiceHandler(Looper looper, MusicService musicService) {
            super(looper);
            musicServiceWeakReference = new WeakReference<>(musicService);
        }


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FADEDOWN:
                    mCurrentVolume -= 0.05f;
                    if (mCurrentVolume > 0.2f) {
                        sendEmptyMessageDelayed(FADEDOWN, 10);
                    } else {
                        mCurrentVolume = 0.2f;
                    }
                    musicServiceWeakReference.get().setVolume(mCurrentVolume);
                    break;
                case FADEUP:
                    mCurrentVolume += 0.01f;
                    if (mCurrentVolume < 1.0f) {
                        sendEmptyMessageDelayed(FADEUP, 10);
                    } else {
                        mCurrentVolume = 1.0f;
                    }
                    musicServiceWeakReference.get().setVolume(mCurrentVolume);
                    break;
                default:
                    break;
            }

        }
    }

    private void setVolume(float volume) {
        wrappedMediaPlayer.setVolume(volume);
    }


    /**
     * 获取并准备现在的曲目资源和下一首资源
     */
    private void openCurrentAndNext() {
        openCurrentAndMaybeNext(true);
    }

    private void openCurrentAndMaybeNext(boolean openNext) {
        CommonLogger.e("获取并准备现在的曲目资源和下一首资源");
        synchronized (this) {
            if (mPlaylist.size() == 0) {
                return;
            }
            boolean shutdown = false;
            final MusicPlayBean musicPlayBean = mPlaylist.get(playPosition);
            if (musicPlayBean.isLocal()) {
                while (true) {
                    if (openFile(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "/" + musicPlayBean.getSongId(), musicPlayBean.getLrcUrl(), musicPlayBean.getSongId())) {
                        break;
                    }
                    CommonLogger.e("上面打开文件失败，进行下面的操作");
                    if (mOpenFailedCounter++ < 10 && mPlaylist.size() > 1) {
                        CommonLogger.e("更新下一首歌曲的Cursor");
//                    切换下一首
                        int position = getNextPosition(true);
                        if (position < 0) {
                            shutdown = true;
                            break;
                        }
                        playPosition = position;
                    } else {
//                    多次重复失败，重置关闭
                        mOpenFailedCounter = 0;
                        shutdown = true;
                        break;
                    }
                }
            } else {
                shutdown = !openFile(musicPlayBean.getSongUrl(), musicPlayBean.getLrcUrl(), musicPlayBean.getSongId());
            }
            if (shutdown) {
                stop(true);
            } else if (openNext) {
                setNextMusicPlayInfo();
            }
        }
    }

    private void loadLrc(final long songId, String url) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                return Httputil.getContent(params[0]);
            }

            @Override
            protected void onPostExecute(String content) {
                if (content != null) {
                    FileUtil.writeToFile(MusicUtil.getLyricPath(songId), content);
                } else {
                    CommonLogger.e("内容为空?");
                }
            }
        }.execute(url);
    }

    private void setNextMusicPlayInfo() {
        setNextMusicPlayInfo(getNextPosition(true));
    }

    private void setNextMusicPlayInfo(int nextPosition) {
        CommonLogger.e("设置下一首的资源");
        nextPlayPosition = nextPosition;
        if (nextPlayPosition >= 0 && mPlaylist != null && nextPlayPosition < mPlaylist.size()) {
            MusicPlayBean nextBean = mPlaylist.get(nextPosition);
            if (nextBean.isLocal()) {
                wrappedMediaPlayer.setNextDataSource(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "/" + nextBean.getSongId(), nextBean.getLrcUrl(), nextBean.getSongId());
            } else {
                wrappedMediaPlayer.setNextDataSource(nextBean.getSongUrl(), nextBean.getLrcUrl(), nextBean.getSongId());
            }
        }
    }

    /**
     * 根据选择的播放顺序类型来决定下一首播放的位置
     *
     * @param force 是否强制获取下一个播放位置
     * @return 播放位置
     */
    private int getNextPosition(boolean force) {
        CommonLogger.e("获取下一首歌曲的位置" + force);
        if (mPlaylist == null || mPlaylist.size() == 0) {
            return -1;
        }
        if (mode == MODE_LOOP) {
//            循环播放
            return playPosition;
        } else if (mode == MODE_SHUFFLE) {
//            随机选择模式
            final int numTracks = mPlaylist.size();
            final int[] trackNumPlays = new int[numTracks]; //用来记录每首歌播放的次数
            for (int i = 0; i < numTracks; i++) {
                trackNumPlays[i] = 0;
            }
            final int numHistory = mHistory.size();
            for (int i = 0; i < numHistory; i++) { //遍历历史记录,记录每首歌播放的次数
                final int idx = mHistory.get(i);
                if (idx >= 0 && idx < numTracks) {
                    trackNumPlays[idx]++;
                }
            }
            if (playPosition >= 0 && playPosition < numTracks) {
                trackNumPlays[playPosition]++;
            }
            int minNumPlays = Integer.MAX_VALUE; //所有歌曲中播放次数最少的数目
            int numTracksWithMinNumPlays = 0; //播放次数一样最少的歌曲的数目
            for (int trackNumPlay : trackNumPlays) {
                if (trackNumPlay < minNumPlays) {
                    minNumPlays = trackNumPlay;
                    numTracksWithMinNumPlays = 1;
                } else if (trackNumPlay == minNumPlays) {
                    numTracksWithMinNumPlays++;
                }
            }
//            在相同播放次数最少的歌曲中随机选择
            int skip = mShuffler.nextInt(numTracksWithMinNumPlays);
            for (int i = 0; i < trackNumPlays.length; i++) {//遍历,根据随机数字选取对应的歌曲
                if (trackNumPlays[i] == minNumPlays) {
                    if (skip == 0) {
                        return i;
                    } else {
                        skip--;
                    }
                }
            }
            return -1;
        } else {
//            正常的顺序播放
            if (playPosition >= mPlaylist.size() - 1) {
                return 0;
            } else {
                return playPosition + 1;
            }
        }
    }


    private class WrappedMediaPlayer implements MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {
        private WeakReference<MusicService> musicServiceWeakReference;
        private MediaPlayer mediaPlayer;
        private MediaPlayer nextMediaPlayer;
        private boolean isInitialized;
        private int secondProgress;

        WrappedMediaPlayer(MusicService musicService) {
            musicServiceWeakReference = new WeakReference<>(musicService);
            mediaPlayer = new MediaPlayer();
//            设置低耗电模式
            mediaPlayer.setWakeMode(musicServiceWeakReference.get(), PowerManager.PARTIAL_WAKE_LOCK);
        }


        void setDataSource(String path, String lrcUrl, long songId) {
            isInitialized = realSetDataSource(mediaPlayer, path, lrcUrl, songId);
        }


        void setNextDataSource(String path, String lrcUrl, long songId) {
            CommonLogger.e("设置下一首歌的资源" + path);
            if (path == null) {
                return;
            }
            try {
                mediaPlayer.setNextMediaPlayer(null);
            } catch (Exception e) {
                e.printStackTrace();
                CommonLogger.e("设置下一个播放器出错" + e.getMessage());
            }
            if (nextMediaPlayer != null) {
                nextMediaPlayer.release();
                nextMediaPlayer = null;
            }
            nextMediaPlayer = new MediaPlayer();
            nextMediaPlayer.setWakeMode(musicServiceWeakReference.get(), PowerManager.PARTIAL_WAKE_LOCK);
            if (realSetDataSource(nextMediaPlayer, path, lrcUrl, songId)) {
                mediaPlayer.setNextMediaPlayer(nextMediaPlayer);
            } else {
                nextMediaPlayer.release();
                nextMediaPlayer = null;
            }
        }

        private boolean realSetDataSource(MediaPlayer mediaPlayer, String path, String lrcUrl, long songId) {
            CommonLogger.e("设置播放的资源路径" + path);
            if (path == null) {
                return false;
            }
            try {
                if (lrcUrl != null) {
                    loadLrc(songId, lrcUrl);
                }
                mediaPlayer.reset();
                mediaPlayer.setOnPreparedListener(null);
                if (path.startsWith("content://")) {
                    mediaPlayer.setDataSource(musicServiceWeakReference.get(), Uri.parse(path));
                    mediaPlayer.setOnBufferingUpdateListener(null);
                } else {
                    mediaPlayer.setOnBufferingUpdateListener(this);
                    mediaPlayer.setDataSource(path);
                }
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
                CommonLogger.e("准备播放资源的时候出错" + e.getMessage());
                return false;
            } catch (IllegalStateException e) {
                CommonLogger.e("准备播放资源的时候出错12" + e.getMessage());
            }
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnErrorListener(this);
            return true;
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
//            这里返回true,防止直接调用onCompletion方法，导致不断切换下一首
            CommonLogger.e("12播放声音出错" + what + "  " + extra);
            switch (what) {
                case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                    break;
                default:
                    break;
            }
            return true;
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            if (mp == mediaPlayer && nextMediaPlayer != null) {
                prepareNext(false);
                notifyChange(META_CHANGED);
                CommonLogger.e("播放完,这里会继续播放下一首吗");
            }
        }

        boolean isInitialized() {
            return isInitialized;
        }

        public long duration() {
            if (mediaPlayer!=null) {
                return mediaPlayer.getDuration();
            }else {
                return -1;
            }
        }

        void start() {
            CommonLogger.e("播放真正开始啦啦");
            if (mediaPlayer!=null) {
                mediaPlayer.start();
            }
        }

        public long position() {
            return mediaPlayer.getCurrentPosition();
        }

        void stop() {
            mediaPlayer.stop();
        }

        long seek(long position) {
            mediaPlayer.seekTo((int) position);
            return position;
        }


        void pause() {
            CommonLogger.e("真正停止");
            mediaPlayer.pause();
            setIsSupposedToBePlaying(false, true);
        }

        void setVolume(float volume) {
            mediaPlayer.setVolume(volume, volume);
        }

        void release() {
            if (mediaPlayer!=null) {
                mediaPlayer.release();
            }
        }

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            CommonLogger.e("这里缓存");
            if (mp == mediaPlayer) {
//                只获取现在播放的加载进度条,因为播放一首时会提前加载下一首
                secondProgress = (int) (percent * (mp.getDuration()) / 100.0);
                CommonLogger.e("加载缓存进度" + secondProgress);
                notifyChange(BUFFER_UPDATE_CHANGED);
            }
        }

        int getSecondProgress() {
            return secondProgress;
        }

        int getAudioSessionId() {
            return mediaPlayer.getAudioSessionId();
        }
    }

    private void prepareNext(boolean force) {
        synchronized (this) {
            if (wrappedMediaPlayer.mediaPlayer != null) {
                wrappedMediaPlayer.mediaPlayer.release();
                wrappedMediaPlayer.mediaPlayer = wrappedMediaPlayer.nextMediaPlayer;
                wrappedMediaPlayer.nextMediaPlayer = null;
                if (mode != MODE_SHUFFLE) {
                    //                如果是随机需要添加到历史播放记录中
                    mHistory.add(playPosition);
                    if (mHistory.size() > MAX_HISTORY_SIZE) {
                        mHistory.remove(0);
                    }
                }
                playPosition = nextPlayPosition;
                nextPlayPosition = getNextPosition(force);
                CommonLogger.e("下一首位置" + nextPlayPosition);
                setNextMusicPlayInfo(nextPlayPosition);
                play();
            }
        }
    }


    private String getSongName() {
        CommonLogger.e("获取歌名");
        synchronized (this) {
            if (getCurrentMusicPlayInfo() != null) {
                return getCurrentMusicPlayInfo().getSongName();
            }
            return null;
        }
    }

    private long getAudioId() {
        CommonLogger.e("getAudioId");
        MusicPlayBean musicPlayInfo = getCurrentMusicPlayInfo();
        CommonLogger.e("这里到了吗");
        if (musicPlayInfo != null) {
            CommonLogger.e("1这里到了吗");
            return musicPlayInfo.getSongId();
        }
        return -1;
    }


    private MusicPlayBean getCurrentMusicPlayInfo() {
        CommonLogger.e("getCurrentMusicPlayInfo" + playPosition);
        return getMusicPlayInfo(playPosition);
    }

    private MusicPlayBean getMusicPlayInfo(int position) {
        if (position >= 0 && mPlaylist != null && position < mPlaylist.size()) {
            return mPlaylist.get(position);
        }
        CommonLogger.e("getMusicPlayInfo" + position);
        return null;
    }


    private static final class Shuffler {


        private final LinkedList<Integer> mHistoryOfNumbers = new LinkedList<>();

        private final TreeSet<Integer> mPreviousNumbers = new TreeSet<>();

        private final Random mRandom = new Random();

        private int mPrevious;


        Shuffler() {
            super();
        }

        int nextInt(final int interval) {
            CommonLogger.e("随机播放的时候获取下一首歌的位置" + interval);
            int next;
            do {
                next = mRandom.nextInt(interval);
            } while (!(next != mPrevious && interval > 1
                    && !mPreviousNumbers.contains(next)));
            mPrevious = next;
            mHistoryOfNumbers.add(mPrevious);
            mPreviousNumbers.add(mPrevious);
            if (!mHistoryOfNumbers.isEmpty() && mHistoryOfNumbers.size() >= MAX_HISTORY_SIZE) {
                for (int i = 0; i < Math.max(1, MAX_HISTORY_SIZE / 2); i++) {
                    mPreviousNumbers.remove(mHistoryOfNumbers.removeFirst());
                }
            }
            return next;
        }
    }
}
