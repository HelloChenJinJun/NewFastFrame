package com.snew.video.dagger.actor;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.snew.video.adapter.ActorVideoDetailAdapter;
import com.snew.video.mvp.actor.ActorDetailInfoActivity;
import com.snew.video.mvp.actor.ActorDetailInfoPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/19     16:28
 */
@Module
public class ActorDetailInfoModule {
    private ActorDetailInfoActivity mActorDetailInfoActivity;

    public ActorDetailInfoModule(ActorDetailInfoActivity actorDetailInfoActivity) {
        mActorDetailInfoActivity = actorDetailInfoActivity;
    }

    @Provides
    public ActorVideoDetailAdapter provideAdapter() {
        return new ActorVideoDetailAdapter();
    }

    @Provides
    public ActorDetailInfoPresenter providePresenter(DefaultModel defaultModel) {
        return new ActorDetailInfoPresenter(mActorDetailInfoActivity, defaultModel);
    }
}
