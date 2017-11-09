package com.example.live.dagger.video;

import com.example.commonlibrary.dagger.scope.PerActivity;
import com.example.live.ui.VideoActivity;
import com.example.live.dagger.MainComponent;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/15      21:34
 * QQ:             1981367757
 */

@PerActivity
@Component(dependencies = MainComponent.class, modules = VideoModule.class)
public interface VideoComponent {
    public void inject(VideoActivity videoActivity);
}
