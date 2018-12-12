package com.snew.video.dagger.qq;

import com.example.commonlibrary.dagger.scope.PerFragment;
import com.snew.video.dagger.VideoComponent;
import com.snew.video.mvp.qq.QQVideoListFragment;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/12     16:05
 */
@PerFragment
@Component(dependencies = VideoComponent.class, modules = QQVideoListModule.class)
public interface QQVideoListComponent {
    public void inject(QQVideoListFragment qqVideoListFragment);
}
