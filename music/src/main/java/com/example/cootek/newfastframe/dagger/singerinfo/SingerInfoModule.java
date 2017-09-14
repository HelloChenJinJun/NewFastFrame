package com.example.cootek.newfastframe.dagger.singerinfo;

import com.example.cootek.newfastframe.MainRepositoryManager;
import com.example.cootek.newfastframe.adapter.SingerInfoAdapter;
import com.example.cootek.newfastframe.mvp.singerinfo.SingerInfoModel;
import com.example.cootek.newfastframe.mvp.singerinfo.SingerInfoPresenter;
import com.example.cootek.newfastframe.ui.SingerInfoActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by COOTEK on 2017/9/3.
 */
@Module
public class SingerInfoModule {
    private SingerInfoActivity singerInfoActivity;

    public SingerInfoModule(SingerInfoActivity singerInfoActivity) {
        this.singerInfoActivity = singerInfoActivity;
    }


    @Provides
    public SingerInfoAdapter provideSingerInfoAdapter() {
        return new SingerInfoAdapter();
    }


    @Provides
    public SingerInfoPresenter provideSingerInfoPresenter(SingerInfoModel singerInfoModel) {
        return new SingerInfoPresenter(singerInfoActivity, singerInfoModel);
    }


    @Provides
    public SingerInfoModel provideSingerInfoModel(MainRepositoryManager mainRepositoryManager) {
        return new SingerInfoModel(mainRepositoryManager);
    }
}
