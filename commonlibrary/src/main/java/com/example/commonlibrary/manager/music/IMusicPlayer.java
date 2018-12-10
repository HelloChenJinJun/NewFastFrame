package com.example.commonlibrary.manager.music;

import com.example.commonlibrary.bean.music.MusicPlayBean;

import java.util.List;

/**
 * 项目名称:    Update
 * 创建人:      陈锦军
 * 创建时间:    2018/12/4     11:10
 */
public interface IMusicPlayer {


    void play(MusicPlayBean musicPlayBean,long seekPosition);

    void play(List<MusicPlayBean> urlList, int position,long seekPosition);

    void play(long seekPosition);


    void pause();

    void seekTo(int position);


    int getDuration();

    int getPosition();

    int getBufferedPercentage();


    void setPlayMode(int playMode);


    void next();

    void pre();


    //    获取现在的播放状态
    int getCurrentState();


    //    重设
    void reset();

    void release();


    String getUrl();

    int getPlayMode();
}
