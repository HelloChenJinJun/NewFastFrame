package com.example.cootek.newfastframe.dagger.recommend;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.cootek.newfastframe.adapter.RecommendAlbumAdapter;
import com.example.cootek.newfastframe.adapter.RecommendRadioAdapter;
import com.example.cootek.newfastframe.adapter.RecommendSongListAdapter;
import com.example.cootek.newfastframe.mvp.recommend.RecommendPresenter;
import com.example.cootek.newfastframe.ui.fragment.RecommendFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by COOTEK on 2017/9/1.
 */
@Module
public class RecommendModule {
    private RecommendFragment recommendFragment;

    public RecommendModule(RecommendFragment recommendFragment) {
        this.recommendFragment = recommendFragment;
    }

    @Provides
    public RecommendRadioAdapter provideRecommendAdapter() {
        return new RecommendRadioAdapter();
    }

    @Provides
    public RecommendPresenter provideRecommendProvider(DefaultModel recommendModel) {
        return new RecommendPresenter(recommendFragment, recommendModel);
    }




    @Provides
    RecommendSongListAdapter provideRecommendSongListAdapter() {
        return new RecommendSongListAdapter();
    }


    @Provides
    RecommendAlbumAdapter provideRecommendAlbumAdapter() {
        return new RecommendAlbumAdapter();
    }


}
