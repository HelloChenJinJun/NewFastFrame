package com.example.live.dagger.recommend;

import com.example.live.MainRepositoryManager;
import com.example.live.RecommendLiveAdapter;
import com.example.live.RecommendLiveFragment;
import com.example.live.mvp.RecommendLiveModel;
import com.example.live.mvp.RecommendLivePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/14      20:30
 * QQ:             1981367757
 */
@Module
public class RecommendLiveModule {

    private RecommendLiveFragment recommendLiveFragment;

    public RecommendLiveModule(RecommendLiveFragment recommendLiveFragment) {
        this.recommendLiveFragment = recommendLiveFragment;
    }

    @Provides
    public RecommendLiveAdapter provideRecommendLiveAdapter(){
        return new RecommendLiveAdapter();
    }


    @Provides
    public RecommendLiveModel provideRecommendLiveModel(MainRepositoryManager mainRepositoryManager){
        return new RecommendLiveModel(mainRepositoryManager);
    }

    @Provides
    public RecommendLivePresenter provideRecommendLivePresenter(RecommendLiveModel recommendLiveModel){
        return new RecommendLivePresenter(recommendLiveFragment,recommendLiveModel);
    }


}
