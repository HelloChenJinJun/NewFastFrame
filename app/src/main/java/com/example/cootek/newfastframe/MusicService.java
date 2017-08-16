package com.example.cootek.newfastframe;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.NotificationCompat;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.util.Log;


import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ToastUtils;
import com.google.googlejavaformat.CommentsHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

/**
 * Created by COOTEK on 2017/8/7.
 */

public class MusicService extends Service {

    public static final int MAX_HISTORY_SIZE = 1000;
    public static final int TRACK_END = 0;
    public static final int NOTIFY_TYPE_NONE = 0;
    public static final int FOCUSCHANGE = 5;
    public static final int SERVICE_DIED = 4;
    public static final int NEXT = 10;
    public static final int RELEASE_WAKE_LOCK = 11;
    public static final String MEDIA_BUTTON_PAUSE = "pause";
    public static final String MEDIA_BUTTON_ACTION = "media_button_action";
    public static final String SHUTDOWN_ACTION = "shutdown_action";
    public static final String COMMAND = "command";
    public static final String MEDIA_BUTTON_TOGGLE_PAUSE = "toggle_pause";
    public static final String MEDIA_BUTTON_STOP = "stop";
    public static final String MEDIA_BUTTON_PLAY = "play";
    public static final String MEDIA_BUTTON_NEXT = "next";
    public static final String MEDIA_BUTTON_PREVIOUS = "previous";
    public static final String QUEUE_CHANGED = "queue_changed";
    public static final String META_CHANGED = "meta_changed";
    public static final String PLAYSTATE_CHANGED = "play_state_changed";
    public static final String SHUFFLEMODE_CHANGED = "shuffle_mode_changed";
    public static final String POSITION_CHANGED = "position_changed";
    public static final String REPEATMODE_CHANGED = "repeat_mode_changed";
    public static final String REFRESH_CHANGED = "refresh";
    public static final String PLAYLIST_CHANGED = "play_list_changed";
    public static final int REPEAT_NONE = 0;
    public static final int REPEAT_ALL = 1;
    public static final int REPEAT_CURRENT = 2;
    public static final int SHUFFLE_NONE = 0;
    public static final int SHUFFLE_AUTO = 1;
    public static final int SHUFFLE_NORMAL = 2;
    public static final long WAKE_UP_DELAY = 5 * 60 * 1000;
    //audio._id AS _id
    private static final String[] PROJECTION = new String[]{ //歌曲信息
            MediaStore.Audio.Media._ID, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST_ID
    };
    private static final String[] ALBUM_PROJECTION = new String[]{ //专辑信息
            MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.LAST_YEAR
    };
    private static final String[] PROJECTION_MATRIX = new String[]{
            "_id", MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST_ID
    };
    public static final String NAME_ERROR_ACTION = "name_error_action";
    public static final String ERROR_CODE = "ERROR_CODE";
    public static final int FADEDOWN = 6;
    public static final int FADEUP = 7;
    public static final int NOTIFY_MODE_FOREGROUND = 1;
    public static final int NOTIFY_MODE_BACKGROUND = 2;
    public static final int NOTIFY_MODE_NONE = 0;
    public static final String PREVIOUS_ACTION = "previous_action";
    public static final String NEXT_ACTION = "next_action";
    public static final String TOGGLEPAUSE_ACTION = "toggle_pause_action";
    public static final String PAUSE_ACTION = "pause_action";
    public static final String STOP_ACTION = "stop_action";
    public static final String REPEAT_ACTION = "repeat_action";
    public static final String PREVIOUS_FORCE_ACTION = "previous_force_action";
    public static final String SHUFFLE_ACTION = "shuffle_action";
    public static final String SERVICECMD = "service_cmd";
    private IBinder bind = new MusicServiceStub(this);
    private NotificationManagerCompat notificationManagerCompat;
    private DaoSession daoSession;
    private HandlerThread handlerThread;
    private MusicServiceHandler musicServiceHandler;
    private AudioManager audioManager;
    private MediaSessionCompat mediaSessionCompat;
    private boolean mPausedByTransientLossOfFocus = false;
    private long notificationPostTime = 0;
    private int mNotifyMode = NOTIFY_TYPE_NONE;
    private PendingIntent shutDownIntent;
    private final AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() { //监听,转发给mPlayerHandler处理
        @Override
        public void onAudioFocusChange(final int focusChange) {
            musicServiceHandler.obtainMessage(FOCUSCHANGE, focusChange, 0).sendToTarget();
        }
    };
    private PowerManager.WakeLock mWakeLock;
    private int repeatMode = REPEAT_CURRENT;
    private int mStartId;
    private WrappedMediaPlayer wrappedMediaPlayer;
    private BroadcastReceiver sdCardReceiver;
    private boolean mQueueIsSaveable = true;
    private int mMediaMountedCount = 0;
    private int mCardId;
    private SharedPreferences mPreferences;
    private ArrayList<MusicPlayInfo> mPlaylist = new ArrayList<>(100);
    private int playPosition = -1;
    private Cursor mCursor;
    private int mOpenFailedCounter = 0;
    private static LinkedList<Integer> mHistory = new LinkedList<>();
    private int mShufmode = SHUFFLE_NONE;
    private boolean mIsSupposedToBePlaying = false;
    private AlarmManager alarmManager;
    private boolean mShutdownSchedule = false;
    private Shuffler mShuffler = new Shuffler();
    private int nextPlayPosition = -1;
    private Cursor mAlbumCursor;
    private String mFileToPlay;
    private long[] mAutoShuffleList = null;
    private long mLastPlayedTime = 0;
    private boolean mShowAlbumArtOnLockscreen = true;
    private boolean mServiceInUse = false;
    private float mCurrentVolume = 1.0f;
    private MediaStoreObserver mMediaStoreObserver;
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleCommandIntent(intent);
        }
    };

    private void handleCommandIntent(Intent intent) {
        final String action = intent.getAction();
        final String command = SERVICECMD.equals(action) ? intent.getStringExtra(COMMAND) : null;
        if (NEXT_ACTION.equals(action)) {
            next(true);
        } else if (PREVIOUS_ACTION.equals(action)
                || PREVIOUS_FORCE_ACTION.equals(action)) {
            previous(PREVIOUS_FORCE_ACTION.equals(action));
        } else if (TOGGLEPAUSE_ACTION.equals(action)) {
            if (isPlaying()) {
                pause();
                mPausedByTransientLossOfFocus = false;
            } else {
                play();
            }
        } else if (PAUSE_ACTION.equals(action)) {
            pause();
            mPausedByTransientLossOfFocus = false;
        } else if (STOP_ACTION.equals(action)) {
            pause();
            mPausedByTransientLossOfFocus = false;
            seek(0);
            releaseServiceUiAndStop();
        } else if (REPEAT_ACTION.equals(action)) {
            cycleRepeat();
        } else if (SHUFFLE_ACTION.equals(action)) {
            cycleShuffle();
        }
    }

    private void cycleShuffle() {
        if (mShufmode == SHUFFLE_NONE) {
            setShuffleMode(SHUFFLE_NORMAL);
        } else if (mShufmode == SHUFFLE_NORMAL || mShufmode == SHUFFLE_AUTO) {
            setShuffleMode(SHUFFLE_NONE);
        }
    }


    private void cycleRepeat() {
        if (repeatMode == REPEAT_NONE) {
            setRepeatMode(REPEAT_CURRENT);
            if (repeatMode != SHUFFLE_NONE) {
                setShuffleMode(SHUFFLE_NONE);
            }
        } else {
            setRepeatMode(REPEAT_NONE);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        发送前台通知
        CommonLogger.e("服务的onCreate");
        notificationManagerCompat = NotificationManagerCompat.from(this);
        daoSession = MainApplication.getMainComponent().getDaoSession();
        handlerThread = new HandlerThread("music_handler_thread", Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();
        musicServiceHandler = new MusicServiceHandler(handlerThread.getLooper(), this);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.registerMediaButtonEventReceiver(new ComponentName(getPackageName(), MediaButtonIntentReceiver.class.getName()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setUpMediaSession();
        wrappedMediaPlayer = new WrappedMediaPlayer(this);
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
        mWakeLock.setReferenceCounted(false);
        registerExternalStorageListener();

        mMediaStoreObserver = new MediaStoreObserver(musicServiceHandler);
        getContentResolver().registerContentObserver(
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI, true, mMediaStoreObserver);
        getContentResolver().registerContentObserver(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, true, mMediaStoreObserver);

        mPreferences = getSharedPreferences("Service", 0);
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(SHUTDOWN_ACTION);
        shutDownIntent = PendingIntent.getService(this, 0, intent, 0);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        final IntentFilter filter = new IntentFilter();
        filter.addAction(SERVICECMD);
        filter.addAction(TOGGLEPAUSE_ACTION);
        filter.addAction(PAUSE_ACTION);
        filter.addAction(STOP_ACTION);
        filter.addAction(NEXT_ACTION);
        filter.addAction(PREVIOUS_ACTION);
        filter.addAction(PREVIOUS_FORCE_ACTION);
        filter.addAction(REPEAT_ACTION);
        filter.addAction(SHUFFLE_ACTION);
        // Attach the broadcast listener
        registerReceiver(mIntentReceiver, filter);

    }

    private void registerExternalStorageListener() {
        sdCardReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
                    CommonLogger.e("注册sd接收器收到的消息" + Intent.ACTION_MEDIA_EJECT);
                    saveQueue(true);
                    mQueueIsSaveable = false;
                    closeExternalStorageFiles(intent.getData().getPath());
                } else {
                    CommonLogger.e("注册sd接收器收到的消息");
                    mMediaMountedCount++;
                    mCardId = getCardId();
                    reloadQueueAfterPermissionCheck();
                    mQueueIsSaveable = true;
                    notifyChange(QUEUE_CHANGED);
                    notifyChange(META_CHANGED);

                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        intentFilter.addDataScheme("file");
        registerReceiver(sdCardReceiver, intentFilter);
    }

    private void reloadQueueAfterPermissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                reloadQueue();
            }
        } else {
            reloadQueue();
        }
    }

    private void reloadQueue() {
        int id = mCardId;
        if (mPreferences.contains("cardid")) {
            id = mPreferences.getInt("cardid", ~mCardId);
        }
        if (id == mCardId) {
            mPlaylist = (ArrayList<MusicPlayInfo>) daoSession.getMusicPlayInfoDao().queryBuilder().list();
        }
        if (mPlaylist.size() > 0) {
            final int pos = mPreferences.getInt("curpos", 0); //记录了上次退出时播放曲目在播放队列中的位置
            if (pos < 0 || pos >= mPlaylist.size()) {
                mPlaylist.clear();
                return;
            }
            playPosition = pos;
            updateCursor(mPlaylist.get(playPosition).getId()); //更新mCursor和amblumCursor
            if (mCursor == null) {
                SystemClock.sleep(3000);
                updateCursor(mPlaylist.get(playPosition).getId());
            }
            synchronized (this) {
                closeCursor();
                mOpenFailedCounter = 20;
                openCurrentAndNext();
            }
            if (!wrappedMediaPlayer.isInitialized()) { //说明setDataSource出现问题
                mPlaylist.clear();
                return;
            }
            final long seekpos = mPreferences.getLong("seekpos", 0);
            seek(seekpos >= 0 && seekpos < duration() ? seekpos : 0);
            int repmode = mPreferences.getInt("repeatmode", REPEAT_NONE);
            if (repmode != REPEAT_ALL && repmode != REPEAT_CURRENT) {
                repmode = REPEAT_NONE;
            }
            repeatMode = repmode;
            int shufmode = mPreferences.getInt("shufflemode", SHUFFLE_NONE);
            if (shufmode != SHUFFLE_AUTO && shufmode != SHUFFLE_NORMAL) {
                shufmode = SHUFFLE_NONE;
            }
            if (shufmode != SHUFFLE_NONE) {
                List<MusicHistoryInfo> list = daoSession.getMusicHistoryInfoDao().queryBuilder().where(MusicHistoryInfoDao.Properties.Position.le(mPlaylist.size())).build().list();
                for (MusicHistoryInfo info :
                        list) {
                    mHistory.add(info.getPosition());
                }
            }
            if (shufmode == SHUFFLE_AUTO) {
                if (!makeAutoShuffleList()) {
                    shufmode = SHUFFLE_NONE;
                }
            }
            mShufmode = shufmode;
        }
    }

    /**
     * 在mAutoShuffleList中保存设备中所有曲目的ID
     *
     * @return
     */
    private boolean makeAutoShuffleList() {
        CommonLogger.e("更新和保存自动随机的歌曲列表id");
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    new String[]{
                            MediaStore.Audio.Media._ID
                    }, MediaStore.Audio.Media.IS_MUSIC + "=1", null, null);
            if (cursor == null || cursor.getCount() == 0) {
                return false;
            }
            final int len = cursor.getCount();
            final long[] list = new long[len]; //保存设备上所有曲目的ID
            for (int i = 0; i < len; i++) {
                cursor.moveToNext();
                list[i] = cursor.getLong(0);
            }
            mAutoShuffleList = list;
            return true;
        } catch (final RuntimeException e) {
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return false;
    }

    private long duration() {
        CommonLogger.e("获取歌曲的时长");
        return wrappedMediaPlayer.duration();
    }

    private void closeCursor() {
        CommonLogger.e("关闭cursor");
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
        if (mAlbumCursor != null) {
            mAlbumCursor.close();
            mAlbumCursor = null;
        }
    }

    private void updateCursor(long songId) {
        CommonLogger.e("更新歌曲Id" + songId);
        updateCursor("_id=" + songId, null);
    }

    private void updateCursor(final String selection, final String[] selectionArgs) {
        synchronized (this) {
            closeCursor(); //关闭mCursor和mAlbumCursor
            mCursor = openCursorAndGoToFirst(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    PROJECTION, selection, selectionArgs);
        }
        updateAlbumCursor();
    }


    private int getCardId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                return getRealCardId();
            } else return 0;
        } else {
            return getRealCardId();
        }
    }

    private int getRealCardId() {
        final ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(Uri.parse("content://media/external/fs_id"), null, null,
                null, null);
        int mCardId = -1;
        if (cursor != null && cursor.moveToFirst()) {
            mCardId = cursor.getInt(0);
            cursor.close();
            cursor = null;
        }
        return mCardId;
    }

    private void closeExternalStorageFiles(String path) {
        CommonLogger.e("closeExternalStorageFiles" + path);
        stop(true);
        notifyChange(QUEUE_CHANGED);
        notifyChange(META_CHANGED);
    }

    private void notifyChange(String action) {

        // Update the lockscreen controls
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            updateMediaSession(action);
        if (action.equals(POSITION_CHANGED)) {
            return;
        }

        final Intent intent = new Intent(action);
        intent.putExtra("id", getAudioId());
        intent.putExtra("artistName", getArtistName());
        intent.putExtra("albumName", getAlbumName());
        intent.putExtra("isPlaying", isPlaying());
        intent.putExtra("songName", getSongName());
        intent.putExtra("maxProgress", duration());
        sendStickyBroadcast(intent);
//        final Intent musicIntent = new Intent(intent);
//        musicIntent.setAction(action);
//        sendStickyBroadcast(musicIntent);

        if (action.equals(META_CHANGED)) {
            daoSession.getMusicRecentInfoDao().insertOrReplace(new MusicRecentInfo(System.currentTimeMillis(), mPlaylist.get(playPosition).getId()));
        } else if (action.equals(QUEUE_CHANGED)) {
            saveQueue(true);
            if (isPlaying()) {//如果正在播放,则提前设置好下首播放的datasource
                if (nextPlayPosition >= 0 && nextPlayPosition < mPlaylist.size()
                        && mShufmode != SHUFFLE_NONE) {
                    setNextMusicPlayInfo(nextPlayPosition);
                } else { //SHUFFLE_NONE直接播放下一首
                    setNextMusicPlayInfo();
                }
            } else {
                if (mPlaylist.size() == 0) {
                    cancelNotification();
                }
            }
        } else { // REPEATMODE_CHANGE、PLAYSTATE_CHANGE等
            saveQueue(false);
        }

        if (action.equals(PLAYSTATE_CHANGED)) {
            updateNotification();
        }


    }

    private void updateMediaSession(String action) {
        CommonLogger.e("updateMediaSession：" + action);
        int playState = mIsSupposedToBePlaying
                ? PlaybackStateCompat.STATE_PLAYING
                : PlaybackStateCompat.STATE_PAUSED;

        if (action.equals(PLAYSTATE_CHANGED) || action.equals(POSITION_CHANGED)) { //播放状态改变时
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mediaSessionCompat.setPlaybackState(new PlaybackStateCompat.Builder()
                        .setState(playState, position(), 1.0f)
                        .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_PLAY_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                        .build());
            }
        } else if (action.equals(META_CHANGED) || action.equals(QUEUE_CHANGED)) { //当前播放歌曲的信息或者播放队列改变
            Bitmap albumArt = ImageLoader.getInstance().loadImageSync(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), getAlbumId()).toString());
            if (albumArt != null) {

                Bitmap.Config config = albumArt.getConfig();
                if (config == null) {
                    config = Bitmap.Config.ARGB_8888;
                }
                albumArt = albumArt.copy(config, false);
            } else {
                CommonLogger.e("加载专辑的封面失败设置默认的图片");
                albumArt = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mediaSessionCompat.setMetadata(new MediaMetadataCompat.Builder()
                        .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, getArtistName())
                        .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, getArtistName())
                        .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, getAlbumName())
                        .putString(MediaMetadataCompat.METADATA_KEY_TITLE, getSongName())
                        .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration())
                        .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, getQueuePosition())
                        .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, getQueue().length)
                        .putString(MediaMetadataCompat.METADATA_KEY_GENRE, getGenreName())
                        .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART,
                                mShowAlbumArtOnLockscreen ? albumArt : null)
                        .build());
                mediaSessionCompat.setPlaybackState(new PlaybackStateCompat.Builder()
                        .setState(playState, position(), 1.0f)
                        .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_PLAY_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                        .build());
            }
        }
    }


    public void setLockscreenAlbumArt(boolean enabled) {
        mShowAlbumArtOnLockscreen = enabled;
        notifyChange(META_CHANGED);
    }

    /**
     * 获取歌曲所属类型
     *
     * @return
     */
    private String getGenreName() {
        synchronized (this) {
            if (mCursor == null || playPosition < 0 || playPosition >= mPlaylist.size()) {
                return null;
            }
            String[] genreProjection = {MediaStore.Audio.Genres.NAME};
            Uri genreUri = MediaStore.Audio.Genres.getContentUriForAudioId("external",
                    (int) mPlaylist.get(playPosition).getId());
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
                list[i] = mPlaylist.get(i).getId();
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
        if (!mQueueIsSaveable) {
            return;
        }

        final SharedPreferences.Editor editor = mPreferences.edit();
        if (isAll) {
//            清除旧数据，保持新数据
            daoSession.getMusicPlayInfoDao().deleteAll();
            if (mShufmode != SHUFFLE_NONE) {
                daoSession.getMusicHistoryInfoDao().deleteAll();
            }
            editor.putInt("cardid", mCardId);
        }
        editor.putInt("curpos", playPosition);
        if (wrappedMediaPlayer.isInitialized()) {
            editor.putLong("seekpos", wrappedMediaPlayer.position());
        }
        editor.putInt("repeatmode", repeatMode);
        editor.putInt("shufflemode", mShufmode);
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
                mPausedByTransientLossOfFocus = false;
            }

            @Override
            public void onSkipToNext() {
                CommonLogger.e("mediaSession:onSkipToNext");
                next(true);
            }

            @Override
            public void onSkipToPrevious() {
                CommonLogger.e("mediaSession:onSkipToPrevious");
                previous(true);
            }

            @Override
            public void onStop() {
                CommonLogger.e("mediaSession:onStop");
                pause();
                mPausedByTransientLossOfFocus = false;
                seek(0);
                releaseServiceUiAndStop();
            }

            @Override
            public void onSeekTo(long pos) {
                CommonLogger.e("mediaSession:onSeekTo");
            }
        });
        mediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
    }

    private void releaseServiceUiAndStop() {
        if (isPlaying()
                || mPausedByTransientLossOfFocus
                || musicServiceHandler.hasMessages(TRACK_END)) {
            return;
        }

        cancelNotification();
        audioManager.abandonAudioFocus(mAudioFocusListener); // 不需要 audio focus
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mediaSessionCompat.setActive(false); //不再接受媒体按钮事件
        if (!mServiceInUse) {
            saveQueue(true);
            stopSelf(mStartId);
        }
    }

    private void cancelNotification() {
        stopForeground(true);
        notificationManagerCompat.cancel(hashCode());
        notificationPostTime = 0;
        mNotifyMode = NOTIFY_TYPE_NONE;
    }

    private boolean isPlaying() {
        return mIsSupposedToBePlaying;
    }

    private long seek(long position) {
        if (wrappedMediaPlayer.isInitialized()) {
            if (position < 0) {
                position = 0;
            } else if (position > wrappedMediaPlayer.duration()) {
                position = wrappedMediaPlayer.duration();
            }
            long result = wrappedMediaPlayer.seek(position);
            notifyChange(POSITION_CHANGED);
            return result;
        }
        return -1;
    }

    private void next(boolean force) {
        CommonLogger.e("next下一首" + force);
        synchronized (this) {
            if (mPlaylist.size() <= 0) {
                scheduleDelayedShutdown();
                return;
            }
            int pos;
            if (force) {
                pos = getNextPosition(force);
            } else {
                pos = nextPlayPosition;
                if (pos < 0) {
                    pos = getNextPosition(force);
                }
            }
            if (pos < 0) { //无法选取下一首歌
                setIsSupposedToBePlaying(false, true);
                return;
            }
            stop(false);
            setAndRecordPlayPos(pos); //设置当前播放曲目的ID
            openCurrentAndNext();
            play(); //不重新产生mNextPlayPos
//            notifyChange(META_CHANGED);
        }
    }


    /**
     * 给playPosition设置播放的曲目ID
     *
     * @param nextPos
     */
    public void setAndRecordPlayPos(int nextPos) {
        CommonLogger.e("给playPosition设置播放的曲目ID" + nextPos);
        synchronized (this) {

            if (mShufmode != SHUFFLE_NONE) {
                mHistory.add(playPosition);
                if (mHistory.size() > MAX_HISTORY_SIZE) {
                    mHistory.remove(0);
                }
            }
            playPosition = nextPos;
        }
    }

    private void previous(boolean force) {
        synchronized (this) {
            boolean goPrevious = (position() < 3000 || force);

            if (goPrevious) {
                int pos = getPreviousPlayPosition(true);
                CommonLogger.e("前一首的位置" + pos);
                if (pos < 0) {
                    return;
                }
                nextPlayPosition = playPosition;
                playPosition = pos;
                stop(false);
                openCurrent();
                play(false); //不产生新的下首序号
//                notifyChange(META_CHANGED);
            } else {
                seek(0);
                play(false);
            }
        }

    }

    private void openCurrent() {
        openCurrentAndMaybeNext(false);
    }

    /**
     * 获取播放列表中的前一首歌曲
     *
     * @param removeFromHistory 是否从历史中删除最近一个播放记录
     * @return
     */
    public int getPreviousPlayPosition(boolean removeFromHistory) {
        CommonLogger.e("获取播放列表中的前一首歌曲");
        synchronized (this) {
            if (mShufmode == SHUFFLE_NORMAL) { //普通随机模式,从历史中获取
                CommonLogger.e("这里1");
                final int histsize = mHistory.size();
                if (histsize == 0) {
                    return -1;
                }
                final Integer pos = mHistory.get(histsize - 1);
                if (removeFromHistory) {
                    mHistory.remove(histsize - 1);
                }
                return pos.intValue();
            } else { //其他模式取播放列表中的前一个
                CommonLogger.e("这里");
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
        mFileToPlay = null;
        closeCursor();
        if (died) {
            setIsSupposedToBePlaying(false, false);
        } else {
            CommonLogger.e("停止前台");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                stopForeground(false);
            else stopForeground(true);
        }

    }


    private void play() {
        play(true);
    }

    /**
     * 是否重新获取下一首歌的位置,当播放前一首歌的时候，就不需要重新获取下一首歌的位置了
     *
     * @param createNewNextMusicInfo
     */
    private void play(boolean createNewNextMusicInfo) {
        CommonLogger.e("play" + createNewNextMusicInfo);
        int status = audioManager.requestAudioFocus(mAudioFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (status != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            return;
        }
        Intent intent = new Intent(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION);
        intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, getAudioSessionId());
        intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
        sendBroadcast(intent);
        CommonLogger.e("play的时候注册MediaButtonIntentReceiver");
        audioManager.registerMediaButtonEventReceiver(new ComponentName(getPackageName(),
                MediaButtonIntentReceiver.class.getName()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mediaSessionCompat.setActive(true);
        if (createNewNextMusicInfo) {
            setNextMusicPlayInfo(); //重新产生mNextPlayPos
        } else {
            setNextMusicPlayInfo(nextPlayPosition);//不重新产生mNextPlayPos
        }
        if (wrappedMediaPlayer.isInitialized()) {
            final long duration = wrappedMediaPlayer.duration();
            CommonLogger.e("这里判断是否播放已经到最后两秒了，如果是提前准备下一首1");
//            if (repeatMode != REPEAT_CURRENT && duration > 2000
//                    && wrappedMediaPlayer.position() >= duration - 2000) {
//                CommonLogger.e("提前准备下一首");
//                next(true);
//            }
            wrappedMediaPlayer.start();
            CommonLogger.e("这里通过移除减弱的声音的消息和发送增加声音的消息来调整到正常的音量");
            musicServiceHandler.removeMessages(FADEDOWN);
            musicServiceHandler.sendEmptyMessage(FADEUP); //组件调到正常音量
            CommonLogger.e("设置播放状态,并通知播放状态的改变");
            setIsSupposedToBePlaying(true, true);
            cancelShutdown();
            updateNotification();
            notifyChange(META_CHANGED);
        } else if (mPlaylist.size() <= 0) {
            setShuffleMode(SHUFFLE_AUTO);
        }
    }

    /**
     * 设置随机模式
     *
     * @param shuffleMode
     */
    private void setShuffleMode(int shuffleMode) {
        CommonLogger.e("设置随机模式" + shuffleMode);
        synchronized (this) {
            if (mShufmode == shuffleMode && mPlaylist.size() > 0) {
                return;
            }

            mShufmode = shuffleMode;
            if (mShufmode == SHUFFLE_AUTO) {
                if (makeAutoShuffleList()) {
                    mPlaylist.clear();
                    doAutoShuffleUpdate();
                    playPosition = 0;
                    openCurrentAndNext();
                    play();
//                    notifyChange(META_CHANGED);
                    return;
                } else {
                    mShufmode = SHUFFLE_NONE;
                }
            } else {
                setNextMusicPlayInfo();
            }
            saveQueue(false);
            notifyChange(SHUFFLEMODE_CHANGED);
        }
    }

    /**
     * 更新通知栏
     */
    private void updateNotification() {
        CommonLogger.e("更新通知栏");
        final int newNotifyMode;
        if (isPlaying()) {
            newNotifyMode = NOTIFY_MODE_FOREGROUND;
        } else if (recentlyPlayed()) {
            newNotifyMode = NOTIFY_MODE_BACKGROUND;
        } else {
            newNotifyMode = NOTIFY_MODE_NONE;
        }

        int notificationId = hashCode();
        if (mNotifyMode != newNotifyMode) {
            if (mNotifyMode == NOTIFY_MODE_FOREGROUND) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    stopForeground(newNotifyMode == NOTIFY_MODE_NONE);
                else
                    stopForeground(newNotifyMode == NOTIFY_MODE_NONE || newNotifyMode == NOTIFY_MODE_BACKGROUND);
            } else if (newNotifyMode == NOTIFY_MODE_NONE) {
                notificationManagerCompat.cancel(notificationId);
                notificationPostTime = 0;
            }
        }
        if (newNotifyMode == NOTIFY_MODE_FOREGROUND) {
            startForeground(notificationId, buildNotification());
        } else if (newNotifyMode == NOTIFY_MODE_BACKGROUND) {
            notificationManagerCompat.notify(notificationId, buildNotification());
        }
        mNotifyMode = newNotifyMode;
    }

    private Notification buildNotification() {
        final String albumName = getAlbumName();
        final String artistName = getArtistName();
        final boolean isPlaying = isPlaying();
        String text = TextUtils.isEmpty(albumName)
                ? artistName : artistName + " - " + albumName;

        int playButtonResId = isPlaying
                ? R.drawable.ic_pause_black_24dp : R.drawable.ic_play_arrow_black_24dp;
        Intent nowPlayingIntent = new Intent(this, MainActivity.class);
        nowPlayingIntent.setAction(Constants.NAVIGATE_LIBRARY);
        PendingIntent clickIntent = PendingIntent.getActivity(this, 0, nowPlayingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap artwork;
        artwork = ImageLoader.getInstance().loadImageSync(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), getAlbumId()).toString());

        if (artwork == null) {
            artwork = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        }
        if (notificationPostTime == 0) {
            notificationPostTime = System.currentTimeMillis();
        }
        android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setLargeIcon(artwork)
                .setSmallIcon(R.drawable.ic_music_note_white_24dp)
                .setContentIntent(clickIntent)
                .setContentTitle(getSongName())
                .setContentText(text)
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
        if (artwork != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setColor(Palette.from(artwork).generate().getMutedColor(Color.parseColor("#455A64")));
        }
        return builder.build();
    }

    private PendingIntent getPendingIntent(String action) {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(action);
        return PendingIntent.getService(this, 0, intent, 0);
    }

    private String getArtistName() {
        CommonLogger.e("获取歌手名");
        synchronized (this) {
            if (mCursor == null) {
                return null;
            }
            return mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST));
        }
    }

    private String getAlbumName() {
        CommonLogger.e("获取专辑名");
        synchronized (this) {
            if (mCursor == null) {
                return null;
            }
            return mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM));
        }
    }

    private boolean recentlyPlayed() {
        return isPlaying() || System.currentTimeMillis() - mLastPlayedTime < WAKE_UP_DELAY;
    }

    private void cancelShutdown() {
        if (mShutdownSchedule) {
            alarmManager.cancel(shutDownIntent);
            mShutdownSchedule = false;
        }
    }

    private void setIsSupposedToBePlaying(boolean isPlaying, boolean notify) {
        if (mIsSupposedToBePlaying != isPlaying) {
            mIsSupposedToBePlaying = isPlaying;


            if (!mIsSupposedToBePlaying) {
                CommonLogger.e("通知服务的销毁");
                scheduleDelayedShutdown();
                mLastPlayedTime = System.currentTimeMillis();
            }

            if (notify) {
                CommonLogger.e("通知状态的改变");
                notifyChange(PLAYSTATE_CHANGED);
            }
        }
    }

    private int getAudioSessionId() {
        synchronized (this) {
            return wrappedMediaPlayer.getAudioSessionId();
        }
    }

    private void pause() {
        CommonLogger.e("暂停");
        synchronized (this) {
            musicServiceHandler.removeMessages(FADEUP);
            musicServiceHandler.sendEmptyMessage(FADEDOWN);
            if (mIsSupposedToBePlaying) {
//                notifyChange(META_CHANGED);
                setIsSupposedToBePlaying(false, true);
                TimerTask task = new TimerTask() {
                    public void run() {
                        final Intent intent = new Intent(
                                AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION);
                        intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, getAudioSessionId());
                        intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
                        sendBroadcast(intent); //由系统接收,通知系统audio_session将关闭,不再使用音效
                        CommonLogger.e("发送广播给AudioSession");
                        wrappedMediaPlayer.pause();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 200);
            }
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
                if (action.equals(MEDIA_BUTTON_ACTION)) {
                    switch (value) {
                        case MEDIA_BUTTON_PAUSE:
                            pause();
                            mPausedByTransientLossOfFocus = false;
                            break;
                        case MEDIA_BUTTON_TOGGLE_PAUSE:
                            if (isPlaying()) {
                                pause();
                                mPausedByTransientLossOfFocus = false;
                            } else {
                                play();
                            }
                            break;
                        case MEDIA_BUTTON_NEXT:
                            CommonLogger.e("MEDIA_BUTTON_NEXT_next");
                            next(false);
                            break;
                        case MEDIA_BUTTON_PREVIOUS:
                            previous(false);
                            break;
                        case MEDIA_BUTTON_STOP:
                            pause();
                            mPausedByTransientLossOfFocus = false;
                            seek(0);
                            releaseServiceUiAndStop();
                            break;
                        case MEDIA_BUTTON_PLAY:
                            play();
                            break;
                    }
                    MediaButtonIntentReceiver.completeWakefulIntent(intent);
                } else if (action.equals(SHUTDOWN_ACTION)) {
                    mShutdownSchedule = false;
                    releaseServiceUiAndStop();
                } else if (action.equals(PREVIOUS_ACTION)) {
                    ToastUtils.showShortToast("前一首");
                    previous(true);
                } else if (action.equals(NEXT_ACTION)) {
                    next(true);
                } else if (action.equals(TOGGLEPAUSE_ACTION)) {
                    if (isPlaying()) {
                        pause();
                        mPausedByTransientLossOfFocus = false;
                    } else {
                        play();
                    }
                } else {

                }
            }
        }
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove any sound effects
        final Intent audioEffectsIntent = new Intent(
                AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION);
        audioEffectsIntent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, getAudioSessionId());
        audioEffectsIntent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
        sendBroadcast(audioEffectsIntent);
        alarmManager.cancel(shutDownIntent);

        musicServiceHandler.removeCallbacksAndMessages(null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            handlerThread.quitSafely();
        } else {
            handlerThread.quit();
        }
        wrappedMediaPlayer.release();
        wrappedMediaPlayer = null;

        audioManager.abandonAudioFocus(mAudioFocusListener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mediaSessionCompat.release();

        getContentResolver().unregisterContentObserver(mMediaStoreObserver);

        closeCursor();

        unregisterReceiver(mIntentReceiver);
        if (sdCardReceiver != null) {
            unregisterReceiver(sdCardReceiver);
            sdCardReceiver = null;
        }

        mWakeLock.release();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        CommonLogger.e("服务的onBind");
        cancelShutdown(); //取消延时关闭
        mServiceInUse = true;
        return bind;
    }


    @Override
    public void onRebind(Intent intent) {
        cancelShutdown();
        mServiceInUse = true;

    }

    @Override
    public boolean onUnbind(Intent intent) {
        mServiceInUse = false;
        saveQueue(true);
        if (mIsSupposedToBePlaying || mPausedByTransientLossOfFocus) {
            return true;
        } else if (mPlaylist.size() > 0 || musicServiceHandler.hasMessages(TRACK_END)) {
            scheduleDelayedShutdown();
            return true;
        }
        stopSelf(mStartId);
        return true;
    }

    private class MusicServiceStub extends IMusicService.Stub {
        private WeakReference<MusicService> musicServiceWeakReference;

        public MusicServiceStub(MusicService service) {
            musicServiceWeakReference = new WeakReference<>(service);
        }

        @Override
        public void openFile(String path) throws RemoteException {
            musicServiceWeakReference.get().openFile(path);

        }

        @Override
        public void open(long[] list, int position, long sourceId, int sourceType) throws RemoteException {
            CommonLogger.e("3");
            musicServiceWeakReference.get().open(list, position, sourceId, sourceType);
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
        public void prev(boolean forcePrevious) throws RemoteException {
            musicServiceWeakReference.get().previous(forcePrevious);

        }

        @Override
        public void next() throws RemoteException {
            musicServiceWeakReference.get().next(true);
        }

        @Override
        public void enqueue(long[] list, int action, long sourceId, int sourceType) throws RemoteException {
            musicServiceWeakReference.get().enqueue(list, action, sourceId, sourceType);

        }

        @Override
        public void setQueuePosition(int index) throws RemoteException {
            musicServiceWeakReference.get().stQueuePosition(index);

        }

        @Override
        public void setShuffleMode(int shufflemode) throws RemoteException {
            musicServiceWeakReference.get().setShuffleMode(shufflemode);
        }

        @Override
        public void setRepeatMode(int repeatmode) throws RemoteException {
            musicServiceWeakReference.get().setRepeatMode(repeatmode);
        }

        @Override
        public void moveQueueItem(int from, int to) throws RemoteException {
            musicServiceWeakReference.get().moveQueueItem(from, to);
        }

        @Override
        public void refresh() throws RemoteException {
            musicServiceWeakReference.get().refresh();

        }

        @Override
        public void playlistChanged() throws RemoteException {
            musicServiceWeakReference.get().playlistChanged();

        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return mIsSupposedToBePlaying;
        }

        @Override
        public long[] getQueue() throws RemoteException {
            return musicServiceWeakReference.get().getQueue();
        }

        @Override
        public long getQueueItemAtPosition(int position) throws RemoteException {
            return musicServiceWeakReference.get().getQueueAtPosition(position);
        }

        @Override
        public int getQueueSize() throws RemoteException {
            return musicServiceWeakReference.get().getQueueSize();
        }

        @Override
        public int getQueuePosition() throws RemoteException {
            return musicServiceWeakReference.get().getQueuePosition();
        }

        @Override
        public int getQueueHistoryPosition(int position) throws RemoteException {
            return musicServiceWeakReference.get().getQueueHistoryPosition(position);
        }

        @Override
        public int getQueueHistorySize() throws RemoteException {
            return musicServiceWeakReference.get().getQueueHistorySize();
        }

        @Override
        public int[] getQueueHistoryList() throws RemoteException {
            return musicServiceWeakReference.get().getQueueHistoryList();
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
        public void seekRelative(long deltaInMs) throws RemoteException {
            musicServiceWeakReference.get().seekRelative(deltaInMs);
        }

        @Override
        public long getAudioId() throws RemoteException {
            return musicServiceWeakReference.get().getAudioId();
        }

        @Override
        public MusicPlayInfo getCurrentPlayInfo() throws RemoteException {
            return musicServiceWeakReference.get().getCurrentMusicPlayInfo();
        }

        @Override
        public MusicPlayInfo getPlayInfo(int index) throws RemoteException {
            return musicServiceWeakReference.get().getMusicPlayInfo(index);
        }

        @Override
        public long getNextAudioId() throws RemoteException {
            return musicServiceWeakReference.get().getNextAudioId();
        }

        @Override
        public long getPreviousAudioId() throws RemoteException {
            return musicServiceWeakReference.get().getPreviousAudioId();
        }

        @Override
        public long getArtistId() throws RemoteException {
            return musicServiceWeakReference.get().getArtistId();
        }

        @Override
        public long getAlbumId() throws RemoteException {
            return musicServiceWeakReference.get().getAlbumId();
        }

        @Override
        public String getArtistName() throws RemoteException {
            return musicServiceWeakReference.get().getArtistName();
        }

        @Override
        public String getPlayInfoName() throws RemoteException {
            return musicServiceWeakReference.get().getSongName();
        }

        @Override
        public String getAlbumName() throws RemoteException {
            return musicServiceWeakReference.get().getAlbumName();
        }

        @Override
        public String getPath() throws RemoteException {
            return musicServiceWeakReference.get().getPath();
        }

        @Override
        public int getShuffleMode() throws RemoteException {
            return musicServiceWeakReference.get().getShuffleMode();
        }

        @Override
        public int removePlayInfos(int first, int last) throws RemoteException {
            return musicServiceWeakReference.get().removeMusicInfo(first, last);
        }

        @Override
        public int removePlayInfo(long id) throws RemoteException {
            return musicServiceWeakReference.get().removeMusicInfo(id);
        }

        @Override
        public boolean removePlayInfoAtPosition(long id, int position) throws RemoteException {
            return musicServiceWeakReference.get().removePlayInfoAtPosition(id, position);
        }

        @Override
        public int getRepeatMode() throws RemoteException {
            return musicServiceWeakReference.get().getRepeatMode();
        }

        @Override
        public int getMediaMountedCount() throws RemoteException {
            return musicServiceWeakReference.get().getMediaMountedCount();
        }

        @Override
        public int getAudioSessionId() throws RemoteException {
            return musicServiceWeakReference.get().getAudioSessionId();
        }

        @Override
        public void setLockscreenAlbumArt(boolean enabled) throws RemoteException {
            musicServiceWeakReference.get().setLockscreenAlbumArt(enabled);
        }
    }

    private boolean removePlayInfoAtPosition(long id, int position) {
        synchronized (this) {
            if (position >= 0 &&
                    position < mPlaylist.size() &&
                    mPlaylist.get(position).getId() == id) {
                return removeMusicInfo(position, position) > 0;
            }
        }
        return false;
    }

    private void seekRelative(long deltaInMs) {
        synchronized (this) {
            if (wrappedMediaPlayer.isInitialized()) {
                final long newPos = position() + deltaInMs;
                final long duration = duration();
                if (newPos < 0) {
                    previous(true);
                    // seek to the new duration + the leftover position
                    seek(duration() + newPos);
                } else if (newPos >= duration) {
                    next(true);
                    // seek to the leftover duration
                    seek(newPos - duration);
                } else {
                    seek(newPos);
                }
            }
        }
    }

    private int[] getQueueHistoryList() {
        synchronized (this) {
            int[] history = new int[mHistory.size()];
            for (int i = 0; i < mHistory.size(); i++) {
                history[i] = mHistory.get(i);
            }

            return history;
        }
    }

    private int getQueueHistorySize() {
        synchronized (this) {
            return mHistory.size();
        }
    }

    private int getQueueHistoryPosition(int position) {
        synchronized (this) {
            if (position >= 0 && position < mHistory.size()) {
                return mHistory.get(position);
            }
        }
        return -1;
    }

    private int getQueueSize() {
        synchronized (this) {
            return mPlaylist.size();
        }
    }

    private long getQueueAtPosition(int position) {
        synchronized (this) {
            return playPosition;
        }
    }

    private void playlistChanged() {
        notifyChange(PLAYLIST_CHANGED);
    }

    private void refresh() {
        notifyChange(REFRESH_CHANGED);
    }

    private void moveQueueItem(int index1, int index2) {
        synchronized (this) {
            if (index1 >= mPlaylist.size()) {
                index1 = mPlaylist.size() - 1;
            }
            if (index2 >= mPlaylist.size()) {
                index2 = mPlaylist.size() - 1;
            }

            if (index1 == index2) {
                return;
            }

            final MusicPlayInfo track = mPlaylist.remove(index1);
            if (index1 < index2) {
                mPlaylist.add(index2, track);
                if (playPosition == index1) {
                    playPosition = index2;
                } else if (playPosition >= index1 && playPosition <= index2) {
                    playPosition--;
                }
            } else if (index2 < index1) {
                mPlaylist.add(index2, track);
                if (playPosition == index1) {
                    playPosition = index2;
                } else if (playPosition >= index2 && playPosition <= index1) {
                    playPosition++;
                }
            }
            notifyChange(QUEUE_CHANGED);
        }
    }

    private void setRepeatMode(int repeatmode) {
        synchronized (this) {
            this.repeatMode = repeatmode;
            setNextMusicPlayInfo();
            saveQueue(false);
            notifyChange(REPEATMODE_CHANGED);
        }
    }

    private long getNextAudioId() {
        synchronized (this) {
            if (nextPlayPosition >= 0 && nextPlayPosition < mPlaylist.size() && wrappedMediaPlayer.isInitialized()) {
                return mPlaylist.get(nextPlayPosition).getId();
            }
        }
        return -1;
    }

    private long getPreviousAudioId() {
        synchronized (this) {
            if (wrappedMediaPlayer.isInitialized()) {
                int pos = getPreviousPlayPosition(false);
                if (pos >= 0 && pos < mPlaylist.size()) {
                    return mPlaylist.get(pos).getId();
                }
            }
        }
        return -1;
    }

    private long getArtistId() {
        synchronized (this) {
            if (mCursor == null) {
                return -1;
            }
            return mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_ID));
        }
    }

    private String getPath() {
        synchronized (this) {
            if (mCursor == null) {
                return null;
            }
            return mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA));
        }
    }

    private int getShuffleMode() {
        return mShufmode;
    }

    private int getRepeatMode() {
        return repeatMode;
    }

    private void stQueuePosition(int position) {
        synchronized (this) {
            stop(false);
            playPosition = position;
            openCurrentAndNext();
            play();
//            notifyChange(META_CHANGED);
            if (mShufmode == SHUFFLE_AUTO) {
                doAutoShuffleUpdate();
            }
        }
    }

    private void enqueue(long[] list, int action, long sourceId, int sourceType) {
        synchronized (this) {
            if (action == NEXT && playPosition + 1 < mPlaylist.size()) {
                addToPlayList(list, playPosition + 1, sourceId, sourceType);
                nextPlayPosition = playPosition + 1;
                notifyChange(QUEUE_CHANGED);
            } else {
                addToPlayList(list, Integer.MAX_VALUE, sourceId, sourceType);
                notifyChange(QUEUE_CHANGED);
            }

            if (playPosition < 0) {
                playPosition = 0;
                openCurrentAndNext();
                play();
//                notifyChange(META_CHANGED);
            }
        }
    }

    private void stop() {
        stop(true);
    }

    private void open(long[] list, int position, long sourceId, int sourceType) {
        if (mShufmode == SHUFFLE_AUTO) {
            mShufmode = SHUFFLE_NORMAL;
        }
        final long oldId = getAudioId();
        final int listlength = list.length;
        boolean newlist = true;
        if (mPlaylist.size() == listlength) {
            newlist = false;
            for (int i = 0; i < listlength; i++) {
                if (list[i] != mPlaylist.get(i).getId()) {
                    newlist = true;
                    break;
                }
            }
        }
        if (newlist) {
            addToPlayList(list, -1, sourceId, sourceType); //添加到播放列表,并清空原播放列表
            CommonLogger.e("清空之前的播放队列后通知队列的改变");
            notifyChange(QUEUE_CHANGED);
        }
        if (position >= 0) {
            playPosition = position;
        } else {
            playPosition = mShuffler.nextInt(mPlaylist.size());
        }
        mHistory.clear();
//        openCurrentAndNext();
        openCurrent();
//        if (oldId != getAudioId()) {
//            notifyChange(META_CHANGED);
//        }
    }


    /**
     * 添加所有的item到播放队列中
     *
     * @param list
     * @param position   -1表示清除之前的播放列表
     * @param sourceId
     * @param sourceType
     */
    private void addToPlayList(long[] list, int position, long sourceId, int sourceType) {
        final int addlen = list.length;
        if (position < 0) {
            mPlaylist.clear();
            position = 0;
        }
        mPlaylist.ensureCapacity(mPlaylist.size() + addlen);
        if (position > mPlaylist.size()) {
            position = mPlaylist.size();
        }

        final ArrayList<MusicPlayInfo> arrayList = new ArrayList<>(addlen);
        for (int i = 0; i < list.length; i++) {
            arrayList.add(new MusicPlayInfo(list[i], sourceId, sourceType, i));
        }

        mPlaylist.addAll(position, arrayList);

        if (mPlaylist.size() == 0) {
            closeCursor();
//            notifyChange(META_CHANGED);
        }
    }

    /**
     * 根据path更新Cursor,和准备播放资源
     *
     * @param path
     * @return
     */
    private boolean openFile(String path) {
        CommonLogger.e("打开文件的路径" + path);
        synchronized (this) {
            if (path == null) {
                return false;
            }

            if (mCursor == null) {
                Uri uri = Uri.parse(path);
                boolean shouldAddToPlaylist = true;
                long id = -1;
                try {
                    id = Long.valueOf(uri.getLastPathSegment());
                } catch (NumberFormatException ex) {
                    // Ignore
                }

                if (id != -1 && path.startsWith(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString())) {
                    updateCursor(uri);

                } else if (id != -1 && path.startsWith(
                        MediaStore.Files.getContentUri("external").toString())) {
                    updateCursor(id);

                } else if (path.startsWith("content://downloads/")) {
                    String mpUri = getValueForDownloadedFile(this, uri, "mediaprovider_uri");
                    if (!TextUtils.isEmpty(mpUri)) {
                        if (openFile(mpUri)) {
//                            notifyChange(META_CHANGED);
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        updateCursorForDownloadedFile(this, uri);
                        shouldAddToPlaylist = false;
                    }
                } else {
                    String where = MediaStore.Audio.Media.DATA + "=?";
                    String[] selectionArgs = new String[]{path};
                    updateCursor(where, selectionArgs);
                }
                try {
                    if (mCursor != null && shouldAddToPlaylist) {
                        mPlaylist.clear();
                        mPlaylist.add(new MusicPlayInfo(
                                mCursor.getLong(0), -1, MusicIdType.NORMAL, -1));
                        notifyChange(QUEUE_CHANGED);
                        playPosition = 0;
                        mHistory.clear();
                    }
                } catch (final UnsupportedOperationException ex) {
                    // Ignore
                }
            }
            mFileToPlay = path;
            wrappedMediaPlayer.setDataSource(mFileToPlay);
            if (wrappedMediaPlayer.isInitialized()) {
                mOpenFailedCounter = 0;
                return true;
            }
            CommonLogger.e("设置播放的出错啦啦，这里通知结束服务");
            String trackName = getSongName();
            if (TextUtils.isEmpty(trackName)) {
                trackName = path;
            }
            sendErrorMessage(trackName);

            stop(true);
            return false;
        }
    }

    private void sendErrorMessage(String trackName) {
        final Intent i = new Intent(NAME_ERROR_ACTION);
        i.putExtra(ERROR_CODE, trackName);
        sendBroadcast(i); //发送广播,被PlaybackStatus接收
    }

    private String getValueForDownloadedFile(Context context, Uri uri, String column) {

        Cursor cursor = null;
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(0);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return null;
    }

    private void updateCursorForDownloadedFile(Context context, Uri uri) {
        synchronized (this) {
            closeCursor();
            MatrixCursor cursor = new MatrixCursor(PROJECTION_MATRIX);
            String title = getValueForDownloadedFile(this, uri, "title");
            cursor.addRow(new Object[]{
                    null,
                    null,
                    null,
                    title,
                    null,
                    null,
                    null,
                    null
            });
            mCursor = cursor;
            mCursor.moveToFirst();
        }
    }

    private void updateCursor(Uri uri) {
        synchronized (this) {
            closeCursor();
            mCursor = openCursorAndGoToFirst(uri, PROJECTION, null, null);
        }
        updateAlbumCursor();
    }

    private void updateAlbumCursor() {
        long albumId = getAlbumId();
        if (albumId >= 0) {
            mAlbumCursor = openCursorAndGoToFirst(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    ALBUM_PROJECTION, "_id=" + albumId, null);
        } else {
            mAlbumCursor = null;
        }
    }

    public long getAlbumId() {
        CommonLogger.e("获取当前的专辑图片的ID");
        synchronized (this) {
            if (mCursor == null) {
                return -1;
            }
            return mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID));
        }
    }

    private Cursor openCursorAndGoToFirst(Uri uri, String[] projection,
                                          String selection, String[] selectionArgs) {
        Cursor c = getContentResolver().query(uri, projection,
                selection, selectionArgs, null);
        if (c == null) {
            return null;
        }
        if (!c.moveToFirst()) {
            c.close();
            return null;
        }
        return c;
    }

    private class MusicServiceHandler extends Handler {
        private WeakReference<MusicService> musicServiceWeakReference;

        public MusicServiceHandler(Looper looper, MusicService musicService) {
            super(looper);
            musicServiceWeakReference = new WeakReference<>(musicService);
        }


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SERVICE_DIED:
                    if (isPlaying()) {
                        MusicErrorInfo info = (MusicErrorInfo) msg.obj;
                        musicServiceWeakReference.get().sendErrorMessage(info.getName());
                        musicServiceWeakReference.get().removeMusicInfo(info.getSongId());
                    } else {
//                        如果不是正在播放，直接切换到下一首
                        musicServiceWeakReference.get().openCurrentAndNext();
                    }
                    break;
                case TRACK_END:
//                    全部播放完成
//                    重复播放
                    if (repeatMode == REPEAT_CURRENT) {
                        CommonLogger.e("循环默认，播放现在的歌曲");
                        seek(0);
                        play();
                    } else {
//                        播放下一首
                        CommonLogger.e("播放完next");
                        next(false);
                    }
                    break;
                case RELEASE_WAKE_LOCK:
                    musicServiceWeakReference.get().getWeakLock().release();
                    break;
                case NEXT:
                    CommonLogger.e("通知next");
                    next(false);
                    break;
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
                case FOCUSCHANGE:
                    MusicService service = musicServiceWeakReference.get();
                    switch (msg.arg1) {
                        case AudioManager.AUDIOFOCUS_LOSS:
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                            if (service.isPlaying()) {
                                service.mPausedByTransientLossOfFocus =
                                        msg.arg1 == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
                            }
                            service.pause();
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                            removeMessages(FADEUP);
                            sendEmptyMessage(FADEDOWN);
                            break;
                        case AudioManager.AUDIOFOCUS_GAIN:
                            if (!service.isPlaying()
                                    && service.mPausedByTransientLossOfFocus) {
                                service.mPausedByTransientLossOfFocus = false;
                                mCurrentVolume = 0f;
                                service.setVolume(mCurrentVolume);
                                service.play();
                            } else {
                                removeMessages(FADEDOWN);
                                sendEmptyMessage(FADEUP);
                            }
                            break;
                        default:
                    }
                default:
                    break;
            }

        }
    }

    private void setVolume(float volume) {
        wrappedMediaPlayer.setVolume(volume);
    }

    private int removeMusicInfo(long songId) {
        int numremoved = 0;
        synchronized (this) {
            for (int i = 0; i < mPlaylist.size(); i++) {
                if (mPlaylist.get(i).getId() == songId) {
                    numremoved += removeMusicInfosInternal(i, i);
                    i--;
                }
            }
        }
        if (numremoved > 0) {
            notifyChange(QUEUE_CHANGED);
        }
        return numremoved;
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
//            closeCursor();
            if (mPlaylist.size() == 0) {
                return;
            }
            stop(false);
            boolean shutdown = false;
            updateCursor(mPlaylist.get(playPosition).getId());
            while (true) {

                if (mCursor != null && openFile(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "/" + mCursor.getLong(0))) {
//                    打开资源成功
                    break;
                }
                CommonLogger.e("上面打开文件失败，进行下面的操作");
                closeCursor();
                if (mOpenFailedCounter++ < 10 && mPlaylist.size() > 1) {
                    CommonLogger.e("更新下一首歌曲的Cursor");
//                    切换下一首
                    int position = getNextPosition(false);
                    if (position < 0) {
                        shutdown = true;
                        break;
                    }
                    stop(false);
                    playPosition = position;
                    updateCursor(mPlaylist.get(playPosition).getId());
                } else {
//                    多次重复失败，重置关闭
                    mOpenFailedCounter = 0;
                    shutdown = true;
                    break;
                }
            }
            if (shutdown) {
                CommonLogger.e("在打开文件的位置通知关闭服务，可能是多次打开文件失败");
                scheduleDelayedShutdown();
                if (mIsSupposedToBePlaying) {
                    mIsSupposedToBePlaying = false;
                    notifyChange(PLAYSTATE_CHANGED);
                }
            } else if (openNext) {
                setNextMusicPlayInfo();
            }
        }
    }

    private void setNextMusicPlayInfo() {
        setNextMusicPlayInfo(getNextPosition(false));
    }

    private void setNextMusicPlayInfo(int nextPosition) {
        CommonLogger.e("设置下一首的资源");
        nextPlayPosition = nextPosition;
        if (nextPlayPosition > 0 && mPlaylist != null && nextPlayPosition < mPlaylist.size()) {
            long id = mPlaylist.get(nextPlayPosition).getId();
            wrappedMediaPlayer.setNextDataSource(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "/" + id);
        } else {
            wrappedMediaPlayer.setNextDataSource(null);
        }
    }

    private void scheduleDelayedShutdown() {
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + WAKE_UP_DELAY, shutDownIntent);
        mShutdownSchedule = true;
    }

    /**
     * 根据选择的播放顺序类型来决定下一首播放的位置
     *
     * @param force
     * @return
     */
    private int getNextPosition(boolean force) {
        CommonLogger.e("获取下一首歌曲的位置" + force);
        if (mPlaylist == null || mPlaylist.size() == 0) {
            return -1;
        }
        if (!force && repeatMode == REPEAT_CURRENT) {
//            循环播放
            if (playPosition < 0) {
                return 0;
            } else {
                return playPosition;
            }
        } else if (force && repeatMode == REPEAT_CURRENT) {
            if (playPosition >= mPlaylist.size() - 1) {
                return 0;
            } else {
                return playPosition + 1;
            }
        } else if (mShufmode == SHUFFLE_AUTO) {
            doAutoShuffleUpdate();
            return playPosition + 1;
        } else if (mShufmode == SHUFFLE_NORMAL) {
            final int numTracks = mPlaylist.size();
            final int[] trackNumPlays = new int[numTracks]; //用来记录每首歌播放的次数
            for (int i = 0; i < numTracks; i++) {
                trackNumPlays[i] = 0;
            }
            final int numHistory = mHistory.size();
            for (int i = 0; i < numHistory; i++) { //遍历历史记录,记录每首歌播放的次数
                final int idx = mHistory.get(i).intValue();
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
            if (minNumPlays > 0 && numTracksWithMinNumPlays == numTracks
                    && repeatMode != REPEAT_ALL && !force) {
                return -1; //当前模式为不循环,不选取下一首歌曲
            }
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
                if (repeatMode == REPEAT_NONE && !force) {
                    return -1;
                } else if (repeatMode == REPEAT_ALL || force) {
                    return 0;
                }
                return -1;
            } else {
                return playPosition + 1;
            }
        }
    }


    /**
     * 在设备上随机若干选择曲目组成插入在播放列表尾部,控制播放列表最多在15首
     */
    private void doAutoShuffleUpdate() {
        boolean notify = false;
        if (playPosition > 10) {
            removeMusicInfo(0, playPosition - 9);
            notify = true;
        }
        final int toAdd = 7 - (mPlaylist.size() - (playPosition < 0 ? -1 : playPosition)); //使播放列表最多保持在15首
        for (int i = 0; i < toAdd; i++) {
            int lookback = mHistory.size();
            int idx = -1;
            while (true) {
                idx = mShuffler.nextInt(mAutoShuffleList.length);
                if (!wasRecentlyUsed(idx, lookback)) {
                    break;
                }
                lookback /= 2;
            }
            mHistory.add(idx);
            if (mHistory.size() > MAX_HISTORY_SIZE) {
                mHistory.remove(0);
            }
            mPlaylist.add(new MusicPlayInfo(mAutoShuffleList[idx], -1, MusicIdType.NORMAL, -1));
            notify = true;
        }
        if (notify) {
            notifyChange(QUEUE_CHANGED);
        }
    }

    /**
     * 在最近播放的lookback次中查找是否包含idx位置的item
     *
     * @param idx
     * @param lookback
     * @return
     */
    private boolean wasRecentlyUsed(int idx, int lookback) {
        CommonLogger.e("判断是否是最近播放的位置" + idx + " 是否在最近的" + lookback + "首");
        if (lookback == 0) {
            return false;
        }
        final int histsize = mHistory.size();
        if (histsize < lookback) {
            lookback = histsize;
        }
        final int maxidx = histsize - 1;
        for (int i = 0; i < lookback; i++) {
            final long entry = mHistory.get(maxidx - i);
            if (entry == idx) {
                return true;
            }
        }
        return false;
    }

    private int removeMusicInfo(int first, int last) {
        CommonLogger.e("移除前面的歌曲" + first + last);
        final int numremoved = removeMusicInfosInternal(first, last);
        if (numremoved > 0) {
            notifyChange(QUEUE_CHANGED);
        }
        return numremoved;
    }


    /**
     * 移除first~last位置的音乐信息,调整现在播放的位置
     *
     * @param first
     * @param last
     * @return
     */
    private int removeMusicInfosInternal(int first, int last) {
        synchronized (this) {
            if (last < first) {
                return 0;
            } else if (first < 0) {
                first = 0;
            } else if (last >= mPlaylist.size()) {
                last = mPlaylist.size() - 1;
            }

            boolean gotonext = false;
            if (first <= playPosition && playPosition <= last) { //如果当前播放位置在删除区间,则播放位置调为删除后的下一首
                playPosition = first;
                gotonext = true;
            } else if (playPosition > last) { //如果当前播放位置在删除区间之后,则往前移
                playPosition -= last - first + 1;
            }
            final int numToRemove = last - first + 1;

            if (first == 0 && last == mPlaylist.size() - 1) {//如果是整个播放队列删除
                playPosition = -1;
                nextPlayPosition = -1;
                mPlaylist.clear();
                mHistory.clear();
            } else {
                for (int i = 0; i < numToRemove; i++) {
                    mPlaylist.remove(first);
                }

                ListIterator<Integer> positionIterator = mHistory.listIterator();
                while (positionIterator.hasNext()) { //调整播放历史记录
                    int pos = positionIterator.next();
                    if (pos >= first && pos <= last) {
                        positionIterator.remove();
                    } else if (pos > last) {
                        positionIterator.set(pos - numToRemove);
                    }
                }
            }
            if (gotonext) {
                if (mPlaylist.size() == 0) {
                    stop(true);
                    playPosition = -1;
                    closeCursor();
                } else {
                    if (mShufmode != SHUFFLE_NONE) {
                        playPosition = getNextPosition(true);
                    } else if (playPosition >= mPlaylist.size()) {
                        playPosition = 0;
                    }
                    boolean wasPlaying = isPlaying();
                    stop(false);
                    openCurrentAndNext();
                    if (wasPlaying) { //如果当前正在播放,则先停止再切换到下一首
                        play();
                    }
                }
//                notifyChange(META_CHANGED);
            }
            return last - first + 1;
        }
    }

    private class WrappedMediaPlayer implements MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
        private WeakReference<MusicService> musicServiceWeakReference;
        private MediaPlayer mediaPlayer;
        private MediaPlayer nextMediaPlayer;
        private boolean isInitialized;

        public WrappedMediaPlayer(MusicService musicService) {
            musicServiceWeakReference = new WeakReference<MusicService>(musicService);
            mediaPlayer = new MediaPlayer();
//            设置低耗电模式
            mediaPlayer.setWakeMode(musicServiceWeakReference.get(), PowerManager.PARTIAL_WAKE_LOCK);
        }


        public void setDataSource(String path) {
            isInitialized = realSetDataSource(mediaPlayer, path);
        }


        public void setNextDataSource(String path) {
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
            if (realSetDataSource(nextMediaPlayer, path)) {
                mediaPlayer.setNextMediaPlayer(nextMediaPlayer);
            } else {
                nextMediaPlayer.release();
                nextMediaPlayer = null;
            }
        }

        private boolean realSetDataSource(MediaPlayer mediaPlayer, String path) {
            CommonLogger.e("设置播放的资源路径" + path);
            if (path == null) {
                return false;
            }
            try {
                mediaPlayer.reset();
                mediaPlayer.setOnPreparedListener(null);
                if (path.startsWith("content://")) {
                    mediaPlayer.setDataSource(musicServiceWeakReference.get(), Uri.parse(path));
                } else {
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
                    MusicErrorInfo musicErrorInfo = new MusicErrorInfo();
                    musicErrorInfo.setSongId(musicServiceWeakReference.get().getAudioId());
                    musicErrorInfo.setName(musicServiceWeakReference.get().getSongName());
                    musicServiceHandler.sendMessageDelayed(musicServiceHandler.obtainMessage(SERVICE_DIED, musicErrorInfo), 2000);
                    break;
                default:
                    break;
            }
            return true;
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            if (mp == mediaPlayer && nextMediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = nextMediaPlayer;
                nextMediaPlayer = null;
                musicServiceHandler.sendEmptyMessage(NEXT);
                CommonLogger.e("播放完");
            } else {
//                已经播放完所有的音乐
                CommonLogger.e("播放完了？");
                musicServiceWeakReference.get().getWeakLock().acquire(30000);
                musicServiceHandler.sendEmptyMessage(TRACK_END);
                musicServiceHandler.sendEmptyMessage(RELEASE_WAKE_LOCK);
            }
        }

        public boolean isInitialized() {
            return isInitialized;
        }

        public long duration() {
            return mediaPlayer.getDuration();
        }

        public void start() {
            CommonLogger.e("播放真正开始啦啦");
            mediaPlayer.start();
        }

        public long position() {
            return mediaPlayer.getCurrentPosition();
        }

        public void stop() {
            mediaPlayer.stop();
        }

        public long seek(long position) {
            mediaPlayer.seekTo((int) position);
            return position;
        }

        public int getAudioSessionId() {
            return mediaPlayer.getAudioSessionId();
        }

        public void pause() {
            CommonLogger.e("真正停止");
            mediaPlayer.pause();
        }

        public void setVolume(float volume) {
            mediaPlayer.setVolume(volume, volume);
        }

        public void release() {
            mediaPlayer.release();
        }
    }

    private PowerManager.WakeLock getWeakLock() {
        return mWakeLock;
    }

    private String getSongName() {
        CommonLogger.e("获取歌名");
        synchronized (this) {
            if (mCursor == null) {
                return null;
            }
            return mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE));
        }
    }

    private long getAudioId() {
        CommonLogger.e("getAudioId");
        MusicPlayInfo musicPlayInfo = getCurrentMusicPlayInfo();
        if (musicPlayInfo != null) {
            return musicPlayInfo.getId();
        }
        return -1;
    }

    //外部存储卡的数量
    public int getMediaMountedCount() {
        return mMediaMountedCount;
    }

    private MusicPlayInfo getCurrentMusicPlayInfo() {
        CommonLogger.e("getCurrentMusicPlayInfo");
        return getMusicPlayInfo(playPosition);
    }

    private MusicPlayInfo getMusicPlayInfo(int position) {
        if (position >= 0 && position < mPlaylist.size() && wrappedMediaPlayer.isInitialized()) {
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


        public Shuffler() {
            super();
        }


        public int nextInt(final int interval) {
            CommonLogger.e("随机播放的时候获取下一首歌的位置" + interval);
            int next;
            do {
                next = mRandom.nextInt(interval);
            } while (next == mPrevious && interval > 1
                    && !mPreviousNumbers.contains(next));
            mPrevious = next;
            mHistoryOfNumbers.add(mPrevious);
            mPreviousNumbers.add(mPrevious);
            cleanUpHistory();
            return next;
        }


        private void cleanUpHistory() {
            if (!mHistoryOfNumbers.isEmpty() && mHistoryOfNumbers.size() >= MAX_HISTORY_SIZE) {
                for (int i = 0; i < Math.max(1, MAX_HISTORY_SIZE / 2); i++) {
                    mPreviousNumbers.remove(mHistoryOfNumbers.removeFirst());
                }
            }
        }
    }

    private class MediaStoreObserver extends ContentObserver implements Runnable {


        private Handler handler;

        public MediaStoreObserver(MusicServiceHandler musicServiceHandler) {
            super(musicServiceHandler);
            handler = musicServiceHandler;
        }


        @Override
        public void onChange(boolean selfChange) {
            handler.removeCallbacks(this);
            handler.postDelayed(this, 500);
        }

        @Override
        public void run() {
            refresh();
        }
    }
}
