package com.example.cootek.newfastframe.dagger;

import com.example.commonlibrary.mvp.IView;
import com.example.commonlibrary.repository.BaseRepositoryManager;
import com.example.cootek.newfastframe.MainRepositoryManager;
import com.example.cootek.newfastframe.RankAdapter;
import com.example.cootek.newfastframe.RankModel;
import com.example.cootek.newfastframe.RankPresenter;
import com.example.cootek.newfastframe.api.RankListBean;

import java.util.List;

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
