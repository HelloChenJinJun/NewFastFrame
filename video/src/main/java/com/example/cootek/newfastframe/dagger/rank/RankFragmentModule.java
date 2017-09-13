package com.example.cootek.newfastframe.dagger.rank;

import com.example.commonlibrary.mvp.view.IView;
import com.example.cootek.newfastframe.MainRepositoryManager;
import com.example.cootek.newfastframe.adapter.RankAdapter;
import com.example.cootek.newfastframe.mvp.rank.RankModel;
import com.example.cootek.newfastframe.mvp.rank.RankPresenter;
import com.example.cootek.newfastframe.bean.RankListBean;

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
