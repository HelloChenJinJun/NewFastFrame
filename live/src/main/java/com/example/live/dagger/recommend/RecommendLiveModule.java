package com.example.live.dagger.recommend;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.live.adapter.RecommendLiveAdapter;
import com.example.live.mvp.recommend.RecommendLiveFragment;
import com.example.live.mvp.recommend.RecommendLivePresenter;

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
    public RecommendLivePresenter provideRecommendLivePresenter(DefaultModel recommendLiveModel){
        return new RecommendLivePresenter(recommendLiveFragment,recommendLiveModel);
    }


}
