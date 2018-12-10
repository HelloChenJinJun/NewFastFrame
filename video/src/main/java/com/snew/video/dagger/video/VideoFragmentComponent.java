package com.snew.video.dagger.video;

import com.example.commonlibrary.dagger.scope.PerFragment;
import com.snew.video.dagger.VideoComponent;
import com.snew.video.mvp.video.VideoListFragment;

import dagger.Component;

/**
 * 项目名称:    Update
 * 创建人:      陈锦军
 * 创建时间:    2018/11/27     15:56
 */
@Component(dependencies = VideoComponent.class, modules = VideoFragmentModule.class)
@PerFragment
public interface VideoFragmentComponent {
    public void inject(VideoListFragment videoFragment);
}
