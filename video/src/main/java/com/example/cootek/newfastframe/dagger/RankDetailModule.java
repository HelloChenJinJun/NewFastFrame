package com.example.cootek.newfastframe.dagger;

import com.example.commonlibrary.mvp.IView;
import com.example.cootek.newfastframe.MainRepositoryManager;
import com.example.cootek.newfastframe.adapter.RankDetailAdapter;
import com.example.cootek.newfastframe.mvp.RankDetailModel;
import com.example.cootek.newfastframe.mvp.RankDetailPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by COOTEK on 2017/8/16.
 */

@Module
public class RankDetailModule {
    private IView<Object> iView;


    public RankDetailModule(IView<Object> iView) {
        this.iView = iView;
    }


    @Provides
    public RankDetailModel provideRankDetailModel(MainRepositoryManager mainRepositoryManager) {
        return new RankDetailModel(mainRepositoryManager);
    }

    @Provides
    public RankDetailPresenter provideRankDetailPresenter(RankDetailModel rankModel) {
        return new RankDetailPresenter(iView, rankModel);
    }


    @Provides
    public RankDetailAdapter provideAdapter() {
        return new RankDetailAdapter();
    }
}
