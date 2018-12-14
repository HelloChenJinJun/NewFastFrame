package com.snew.video.dagger.qq.detail;

import com.example.commonlibrary.dagger.scope.PerActivity;
import com.snew.video.dagger.VideoComponent;
import com.snew.video.mvp.qq.detail.QQVideoDetailActivity;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/14     14:48
 */
@PerActivity
@Component(dependencies = VideoComponent.class, modules = QQVideoDetailModule.class)
public interface QQVideoDetailComponent {
    public void inject(QQVideoDetailActivity qqVideoDetailActivity);
}
