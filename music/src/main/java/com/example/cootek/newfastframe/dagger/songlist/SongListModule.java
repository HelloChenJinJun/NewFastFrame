package com.example.cootek.newfastframe.dagger.songlist;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.view.IView;
import com.example.cootek.newfastframe.adapter.SongListAdapter;
import com.example.cootek.newfastframe.mvp.songlist.SongListPresenter;

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
    public SongListPresenter provideRankDetailPresenter(DefaultModel rankModel) {
        return new SongListPresenter(iView, rankModel);
    }


    @Provides
    public SongListAdapter provideAdapter() {
        return new SongListAdapter();
    }
}
