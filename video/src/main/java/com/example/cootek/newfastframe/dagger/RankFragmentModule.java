package com.example.cootek.newfastframe.dagger;

import com.example.commonlibrary.mvp.IView;
import com.example.cootek.newfastframe.MainRepositoryManager;
import com.example.cootek.newfastframe.adapter.RankAdapter;
import com.example.cootek.newfastframe.mvp.RankModel;
import com.example.cootek.newfastframe.mvp.RankPresenter;
import com.example.cootek.newfastframe.api.RankListBean;

import dagger.Module;
import dagger.Provides;

/**
 * Created by COOTEK on 2017/8/16.
 */
@Module
public class RankFragmentModule {
    private IView<RankListBean> iView;

    public RankFragmentModule(IView<RankListBean> iView) {
        this.iView = iView;
    }


    @Provides
    public RankAdapter provideRankAdapter() {
        return new RankAdapter();
    }

    @Provides
    public RankPresenter providerRankPresenter(RankModel rankModel) {
        return new RankPresenter(iView, rankModel);
    }

    @Provides
    RankModel providerRankModel(MainRepositoryManager baseRepositoryManager) {
        return new RankModel(baseRepositoryManager);
    }

}
