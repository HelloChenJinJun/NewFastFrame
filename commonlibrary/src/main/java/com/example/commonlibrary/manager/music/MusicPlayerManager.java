package com.example.commonlibrary.manager.music;

import android.media.MediaPlayer;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.rxbus.event.PlayStateEvent;
import com.example.commonlibrary.utils.Constant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称:    Update
 * 创建人:      陈锦军
 * 创建时间:    2018/12/4     11:24
 */
public class MusicPlayerManager implements IMusicPlayer, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnErrorListener {
    private MediaPlayer mMediaPlayer;
    private PlayData mPlayData;

    //  资源准备中
    public static final int PLAY_STATE_PREPARING = 1;


    //    已经准备好资源
    public static final int PLAY_STATE_PREPARED = 2;
    //    播放中
    public static final int PLAY_STATE_PLAYING = 3;
    //    暂停中
    public static final int PLAY_STATE_PAUSE = 4;
    //    播放完成
    public static final int PLAY_STATE_FINISH = 5;
    public static final int PLAY_STATE_ERROR = 6;
    private int mState = PLAY_STATE_PREPARING;
    private int bufferedPercent;


    private static MusicPlayerManager instance;

    public static MusicPlayerManager getInstance() {
        if (instance == null) {
            instance = new MusicPlayerManager();
        }
        return instance;
    }


    private MusicPlayerManager() {
        initData();
    }

    private void initData() {
        mMediaPlayer = new MediaPlayer();
        mPlayData = new PlayData();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
    }


    private MusicPlayBean mMusicPlayBean;

    public MusicPlayBean getMusicPlayBean() {
        return mMusicPlayBean;
    }

    @Override
    public void play(MusicPlayBean musicPlayBean, long seekPosition) {
        if (musicPlayBean != null) {
            this.mMusicPlayBean = musicPlayBean;
            play(seekPosition);
        }
    }

    @Override
    public void play(List<MusicPlayBean> musicPlayBeans, int position, long seekPosition) {
        List<String> urlList = new ArrayList<>();
        for (MusicPlayBean item :
                musicPlayBeans) {
            urlList.add(item.getSongUrl());
        }
        BaseApplication.getAppComponent()
                .getSharedPreferences().edit()
                .putString(Constant.RECENT_SONG_URL_LIST, BaseApplication
                        .getAppComponent().getGson().toJson(urlList)).apply();
        mPlayData.setData(musicPlayBeans, position);
        play(mPlayData.getCurrentItem(), seekPosition);
    }


    @Override
    public void play(long seekPosition) {
        if (mState == PLAY_STATE_PAUSE) {
            mState = PLAY_STATE_PLAYING;
            RxBusManager.getInstance().post(new PlayStateEvent(mState));
            mMediaPlayer.start();
            return;
        }
        if (mMusicPlayBean == null) {
            return;
        }
        try {
            mState = PLAY_STATE_PREPARING;
            RxBusManager.getInstance().post(new PlayStateEvent(mState));
            mMusicPlayBean.setUpdateTime(System.currentTimeMillis());
            BaseApplication
                    .getAppComponent().getDaoSession().getMusicPlayBeanDao()
                    .update(mMusicPlayBean);
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(mPlayData.getCurrentItem().getSongUrl());
            mMediaPlayer.prepareAsync();
            if (seekPosition != 0) {
                mMediaPlayer.seekTo((int) seekPosition);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        mMediaPlayer.pause();
        mState = PLAY_STATE_PAUSE;
        RxBusManager.getInstance().post(new PlayStateEvent(mState));
    }

    @Override
    public void seekTo(int position) {
        mMediaPlayer.seekTo(position);
    }

    @Override
    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public int getPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public int getBufferedPercentage() {
        return bufferedPercent;
    }

    @Override
    public void setPlayMode(int playMode) {
        mPlayData.setPlayMode(playMode);
    }

    @Override
    public void next() {
        MusicPlayBean nextPath = mPlayData.next();
        if (nextPath == null) {
            mMediaPlayer.reset();
        } else {
            play(nextPath, 0);
        }
    }

    @Override
    public void pre() {
        MusicPlayBean prePath = mPlayData.pre();
        if (prePath == null) {
            mMediaPlayer.reset();
        } else {
            play(prePath, 0);
        }
    }


    @Override
    public int getCurrentState() {
        return mState;
    }


    @Override
    public void reset() {
        mMediaPlayer.reset();
    }

    @Override
    public void release() {
        if (mMusicPlayBean != null) {
            BaseApplication
                    .getAppComponent().getSharedPreferences()
                    .edit().putLong(Constant.SEEK, mMediaPlayer.getCurrentPosition())
                    .putInt(Constant.MUSIC_POSITION, mPlayData.getPosition()).apply();
            mMediaPlayer.release();
        }
        mPlayData = null;
        mMediaPlayer = null;
    }


    @Override
    public String getUrl() {
        if (mMusicPlayBean != null) {
            return mMusicPlayBean.getSongUrl();
        }
        return null;
    }

    @Override
    public int getPlayMode() {
        return mPlayData.getPlayMode();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mState = PLAY_STATE_PREPARED;
        RxBusManager.getInstance().post(new PlayStateEvent(mState));
        mMediaPlayer.start();
        mState = PLAY_STATE_PLAYING;
        RxBusManager.getInstance().post(new PlayStateEvent(mState));
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        MusicPlayBean nextPath = mPlayData.next();
        if (nextPath == null) {
            mMediaPlayer.reset();
        } else {
            play(nextPath, 0);
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        bufferedPercent = percent;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mState = PLAY_STATE_ERROR;
        RxBusManager.getInstance().post(new PlayStateEvent(mState));
        return false;
    }
}
