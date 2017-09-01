package com.example.cootek.newfastframe.dagger;

import com.example.commonlibrary.mvp.IView;
import com.example.cootek.newfastframe.MainRepositoryManager;
import com.example.cootek.newfastframe.adapter.SongListAdapter;
import com.example.cootek.newfastframe.mvp.SongListPresenter;
import com.example.cootek.newfastframe.mvp.SongListModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by COOTEK on 2017/8/16.
 */

@Module
public class SongListModule {
    private IView<Object> iView;


    public SongListModule(IView<Object> iView) {
        this.iView = iView;
    }


    @Provides
    public SongListModel provideRankDetailModel(MainRepositoryManager mainRepositoryManager) {
        return new SongListModel(mainRepositoryManager);
    }

    @Provides
    public SongListPresenter provideRankDetailPresenter(SongListModel rankModel) {
        return new SongListPresenter(iView, rankModel);
    }


    @Provides
    public SongListAdapter provideAdapter() {
        return new SongListAdapter();
    }
}
