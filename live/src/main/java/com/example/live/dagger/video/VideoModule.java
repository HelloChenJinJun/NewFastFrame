package com.example.live.dagger.video;

import com.example.live.MainRepositoryManager;
import com.example.live.ui.VideoActivity;
import com.example.live.mvp.video.VideoModel;
import com.example.live.mvp.video.VideoPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/15      21:35
 * QQ:             1981367757
 */
@Module
public class VideoModule {
    private VideoActivity videoActivity;

    public VideoModule(VideoActivity videoActivity) {
        this.videoActivity = videoActivity;
    }


    @Provides
    public VideoPresenter provideVideoPresenter(VideoModel videoModel){
        return new VideoPresenter(videoActivity,videoModel);
    }


    @Provides
    public VideoModel provideVidoModel(MainRepositoryManager mainRepositoryManager){
        return new VideoModel(mainRepositoryManager);
    }
}
