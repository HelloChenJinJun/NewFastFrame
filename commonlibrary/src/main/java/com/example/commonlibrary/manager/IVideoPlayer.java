package com.example.commonlibrary.manager;

import com.example.commonlibrary.manager.video.VideoController;

import java.util.List;
import java.util.Map;

/**
 * 项目名称:    Update
 * 创建人:      陈锦军
 * 创建时间:    2018/11/22     10:41
 */
public interface IVideoPlayer {

    IVideoPlayer setUp(String url, Map<String, String> headers);


    void start();


    void pause();

    void seekTo(int position);

    public IVideoPlayer setVolume(int volume);

    int getMaxVolume();

    int getVolume();

    int getDuration();

    int getPosition();

    int getBufferedPercentage();

    IVideoPlayer setTitle(String title);

    IVideoPlayer setTotalLength(long length);

    //    获取现在的播放状态
    int getCurrentState();

    //    获取现在的视频窗口状态
    int getWindowState();

    IVideoPlayer setWindowState(int state);

    //    重设
    void reset();

    void release();

    IVideoPlayer setClarity(List<VideoController.Clarity> clarityList);

    IVideoPlayer setImageCover(String imageUrl);


    VideoController getController();

    String getUrl();
}
