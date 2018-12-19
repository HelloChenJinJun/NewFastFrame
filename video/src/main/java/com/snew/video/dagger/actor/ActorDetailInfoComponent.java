package com.snew.video.dagger.actor;

import com.example.commonlibrary.dagger.scope.PerActivity;
import com.snew.video.dagger.VideoComponent;
import com.snew.video.mvp.actor.ActorDetailInfoActivity;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/19     16:27
 */
@PerActivity
@Component(dependencies = VideoComponent.class, modules = ActorDetailInfoModule.class)
public interface ActorDetailInfoComponent {
    public void inject(ActorDetailInfoActivity actorDetailInfoActivity);
}
