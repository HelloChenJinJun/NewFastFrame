package com.example.cootek.newfastframe;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.commonlibrary.utils.CommonLogger;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by COOTEK on 2017/8/7.
 */

public class MusicService extends Service {


    public static final int SHUFFLE_NORMAL = 0;
    private static final int TRACK_END = 0;
    private static final int NOTIFY_TYPE_NONE = 0;
    private static final int FOCUSCHANGE = 5;
    private static final int SERVICE_DIED = 4;
    private static final int NEXT = 10;
    private static final int RELEASE_WAKE_LOCK = 11;
    public static final String MEDIA_BUTTON_PAUSE = "pause";
    public static final String MEDIA_BUTTON_ACTION = "media_button_action";
    public static final String COMMAND = "command";
    public static final String MEDIA_BUTTON_TOGGLE_PAUSE = "toggle_pause";
    public static final String MEDIA_BUTTON_STOP = "stop";
    public static final String MEDIA_BUTTON_PLAY = "play";
    public static final String MEDIA_BUTTON_NEXT = "next";
    public static final String MEDIA_BUTTON_PREVIOUS = "previous";
    private static final String QUEUE_CHANGED = "queue_changed";
    private static final String META_CHANGED = "meta_changed";
    private static final int REPEAT_NONE = 0;
    private static final int REPEAT_ALL = 1;
    private static final int REPEAT_CURRENT = 2;
    private static final int SUFFLE_NONE = 0;
    private IBinder bind = new MusicServiceStub(this);
    private NotificationManagerCompat notificationManagerCompat;
    private DaoSession daoSession;
    private HandlerThread handlerThread;
    private MusicServiceHandler musicServiceHandler;
    private AudioManager audioManager;
    private MediaSessionCompat mediaSessionCompat;
    private boolean mPausedByTransientLossOfFocus = false;
    private long notificationPostTime = 0;
    private int notifyType = NOTIFY_TYPE_NONE;
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
    private List<MusicPlayInfo> mPlaylist = new ArrayList<>(100);
    private int playPosition = -1;
    private Cursor mCursor;

    @Override
    public void onCreate() {
        super.onCreate();
//        发送前台通知
        notificationManagerCompat = NotificationManagerCompat.from(this);
        daoSession = MainApplication.getMainComponent().getDaoSession();
        handlerThread = new HandlerThread("music_handler_thread", Process.THREAD_PRIORITY_BACKGROUND);
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
        mPreferences = getSharedPreferences("Service", 0);
    }

    private void registerExternalStorageListener() {
        sdCardReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
                    saveQueue(true);
                    mQueueIsSaveable = false;
                    closeExternalStorageFiles(intent.getData().getPath());
                } else {
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
//        int id = mCardId;
//        if (mPreferences.contains("cardid")) {
//            id = mPreferences.getInt("cardid", ~mCardId);
//        }
//        if (id == mCardId) {
//            mPlaylist = daoSession.getMusicPlayInfoDao().queryBuilder().list();
//        }
//        if (mPlaylist.size() > 0) {
//            final int pos = mPreferences.getInt("curpos", 0); //记录了上次退出时播放曲目在播放队列中的位置
//            if (pos < 0 || pos >= mPlaylist.size()) {
//                mPlaylist.clear();
//                return;
//            }
//            playPosition = pos;
//            updateCursor(mPlaylist.get(playPosition).getId()); //更新mCursor和amblumCursor
//            if (mCursor == null) {
//                SystemClock.sleep(3000);
//                updateCursor(mPlaylist.get(playPosition).getId());
//            }
//            synchronized (this) {
//                closeCursor();
//                mOpenFailedCounter = 20;
//                openCurrentAndNext();
//            }
//            if (!wrappedMediaPlayer.isInitialized()) { //说明setDataSource出现问题
//                mPlaylist.clear();
//                return;
//            }
//            final long seekpos = mPreferences.getLong("seekpos", 0);
//            seek(seekpos >= 0 && seekpos < duration() ? seekpos : 0);
//            int repmode = mPreferences.getInt("repeatmode", REPEAT_NONE);
//            if (repmode != REPEAT_ALL && repmode != REPEAT_CURRENT) {
//                repmode = REPEAT_NONE;
//            }
//            repeatMode = repmode;
//            int shufmode = mPreferences.getInt("shufflemode", SHUFFLE_MODE);
//            if (shufmode != SHUFFLE_AUTO && shufmode != SHUFFLE_NORMAL) {
//                shufmode = SHUFFLE_NONE;
//            }
//            if (shufmode != SHUFFLE_NONE) {
//                mHistory = mPlaybackStateStore.getHistory(mPlaylist.size());
//            }
//            if (shufmode == SHUFFLE_AUTO) {
//                if (!makeAutoShuffleList()) {
//                    shufmode = SHUFFLE_NONE;
//                }
//            }
//            mShuffleMode = shufmode;
//        }
    }

    private long duration() {
        return 0;
    }

    private void closeCursor() {
    }

    private void updateCursor(long songId) {

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
        stop(true);
        notifyChange(QUEUE_CHANGED);
        notifyChange(META_CHANGED);
    }

    private void notifyChange(String action) {

    }

    private void saveQueue(boolean isAll) {

    }

    private void setUpMediaSession() {
        mediaSessionCompat = new MediaSessionCompat(this, "listener");
        mediaSessionCompat.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
                play();

            }

            @Override
            public void onPause() {
                super.onPause();
                pause();
                mPausedByTransientLossOfFocus = false;
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                next(true);
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                previous(true);
            }

            @Override
            public void onStop() {
                super.onStop();
                pause();
                mPausedByTransientLossOfFocus = false;
                seek(0);
                releaseServiceUiAndStop();
            }

            @Override
            public void onSeekTo(long pos) {
                super.onSeekTo(pos);
            }
        });
        mediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

    }

    private void releaseServiceUiAndStop() {
        if (isPlaying() || mPausedByTransientLossOfFocus || musicServiceHandler.hasMessages(TRACK_END)) {
            return;
        }
        cancelNotification();
        audioManager.abandonAudioFocus(mAudioFocusListener); // 不需要 audio focus
    }

    private void cancelNotification() {
        stopForeground(true);
        notificationManagerCompat.cancel(hashCode());
        notificationPostTime = 0;
        notifyType = NOTIFY_TYPE_NONE;
    }

    private boolean isPlaying() {
        return false;
    }

    private void seek(long position) {

    }

    private void next(boolean isForce) {

    }

    private void previous(boolean isForce) {

    }


    private void stop(boolean b) {

    }


    private void play() {
        int status = audioManager.requestAudioFocus(mAudioFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (status != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            return;
        }
        Intent intent = new Intent(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION);
        intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, getAudioSessionId());
        intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
        sendBroadcast(intent);
    }

    private long getAudioSessionId() {
        return 1;
    }

    private void pause() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        CommonLogger.e("onStartCommand");
        mStartId = startId;
        if (intent != null) {
            String action = intent.getAction();
            String value = intent.getStringExtra(COMMAND);
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
            }
        }
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            handlerThread.quitSafely();
        } else {
            handlerThread.quit();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        CommonLogger.e("onBind");
        return bind;
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
            musicServiceWeakReference.get().open(list, position, sourceId, sourceType);
        }

        @Override
        public void stop() throws RemoteException {

        }

        @Override
        public void pause() throws RemoteException {

        }

        @Override
        public void play() throws RemoteException {

        }

        @Override
        public void prev(boolean forcePrevious) throws RemoteException {

        }

        @Override
        public void next() throws RemoteException {

        }

        @Override
        public void enqueue(long[] list, int action, long sourceId, int sourceType) throws RemoteException {

        }

        @Override
        public void setQueuePosition(int index) throws RemoteException {

        }

        @Override
        public void setShuffleMode(int shufflemode) throws RemoteException {

        }

        @Override
        public void setRepeatMode(int repeatmode) throws RemoteException {

        }

        @Override
        public void moveQueueItem(int from, int to) throws RemoteException {

        }

        @Override
        public void refresh() throws RemoteException {

        }

        @Override
        public void playlistChanged() throws RemoteException {

        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return false;
        }

        @Override
        public long[] getQueue() throws RemoteException {
            return new long[0];
        }

        @Override
        public long getQueueItemAtPosition(int position) throws RemoteException {
            return 0;
        }

        @Override
        public int getQueueSize() throws RemoteException {
            return 0;
        }

        @Override
        public int getQueuePosition() throws RemoteException {
            return 0;
        }

        @Override
        public int getQueueHistoryPosition(int position) throws RemoteException {
            return 0;
        }

        @Override
        public int getQueueHistorySize() throws RemoteException {
            return 0;
        }

        @Override
        public int[] getQueueHistoryList() throws RemoteException {
            return new int[0];
        }

        @Override
        public long duration() throws RemoteException {
            return 0;
        }

        @Override
        public long position() throws RemoteException {
            return 0;
        }

        @Override
        public long seek(long pos) throws RemoteException {
            return 0;
        }

        @Override
        public void seekRelative(long deltaInMs) throws RemoteException {

        }

        @Override
        public long getAudioId() throws RemoteException {
            return 0;
        }

        @Override
        public MusicPlayInfo getCurrentPlayInfo() throws RemoteException {
            return null;
        }

        @Override
        public MusicPlayInfo getPlayInfo(int index) throws RemoteException {
            return null;
        }

        @Override
        public long getNextAudioId() throws RemoteException {
            return 0;
        }

        @Override
        public long getPreviousAudioId() throws RemoteException {
            return 0;
        }

        @Override
        public long getArtistId() throws RemoteException {
            return 0;
        }

        @Override
        public long getAlbumId() throws RemoteException {
            return 0;
        }

        @Override
        public String getArtistName() throws RemoteException {
            return null;
        }

        @Override
        public String getPlayInfoName() throws RemoteException {
            return null;
        }

        @Override
        public String getAlbumName() throws RemoteException {
            return null;
        }

        @Override
        public String getPath() throws RemoteException {
            return null;
        }

        @Override
        public int getShuffleMode() throws RemoteException {
            return 0;
        }

        @Override
        public int removePlayInfos(int first, int last) throws RemoteException {
            return 0;
        }

        @Override
        public int removePlayInfo(long id) throws RemoteException {
            return 0;
        }

        @Override
        public boolean removePlayInfoAtPosition(long id, int position) throws RemoteException {
            return false;
        }

        @Override
        public int getRepeatMode() throws RemoteException {
            return 0;
        }

        @Override
        public int getMediaMountedCount() throws RemoteException {
            return 0;
        }

        @Override
        public int getAudioSessionId() throws RemoteException {
            return 0;
        }

        @Override
        public void setLockscreenAlbumArt(boolean enabled) throws RemoteException {

        }
    }

    private void open(long[] list, int position, long sourceId, int sourceType) {

    }

    private void openFile(String path) {

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
                        musicServiceWeakReference.get().sendErrorMsg(info.getName());
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
                        seek(0);
                        play();
                    } else {
//                        播放下一首
                        next(false);
                    }
                    break;
                case RELEASE_WAKE_LOCK:
                    musicServiceWeakReference.get().getWeakLock().release();
                    break;
                case NEXT:
                    next(false);
                    break;
                default:
                    break;
            }

        }
    }

    private void removeMusicInfo(long songId) {

    }

    private void openCurrentAndNext() {

    }

    private void sendErrorMsg(String songName) {

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
            }
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnErrorListener(this);
            return true;
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            CommonLogger.e("播放声音出错" + what + "  " + extra);
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
            return false;
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            if (mp == mediaPlayer && nextMediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = nextMediaPlayer;
                nextMediaPlayer = null;
                musicServiceHandler.sendEmptyMessage(NEXT);
            } else {
//                已经播放完所有的音乐
                musicServiceWeakReference.get().getWeakLock().acquire(30000);
                musicServiceHandler.sendEmptyMessage(TRACK_END);
                musicServiceHandler.sendEmptyMessage(RELEASE_WAKE_LOCK);
            }
        }

        public boolean isInitialized() {
            return isInitialized;
        }
    }

    private PowerManager.WakeLock getWeakLock() {
        return mWakeLock;
    }

    private String getSongName() {
        return null;
    }

    private long getAudioId() {
        return 0;
    }
}
