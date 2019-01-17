package com.snew.video.dagger.preview;

import com.example.commonlibrary.dagger.scope.PerFragment;
import com.snew.video.dagger.VideoComponent;
import com.snew.video.mvp.preview.PreViewVideoFragment;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2019/1/9     16:17
 */
@PerFragment
@Component(dependencies = VideoComponent.class, modules = PreViewVideoModule.class)
public interface PreViewVideoComponent {
    public void inject(PreViewVideoFragment preViewVideoActivity);
}
