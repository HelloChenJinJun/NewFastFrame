package com.example.cootek.newfastframe.dagger;

import com.example.commonlibrary.mvp.IView;
import com.example.cootek.newfastframe.MainRepositoryManager;
import com.example.cootek.newfastframe.RankDetailAdapter;
import com.example.cootek.newfastframe.RankDetailModel;
import com.example.cootek.newfastframe.RankDetailPresenter;
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
public class RankDetailModule {
    private IView<RankListBean> iView;


    public RankDetailModule(IView<RankListBean> iView) {
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
