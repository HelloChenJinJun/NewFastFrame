package com.example.cootek.newfastframe.dagger.singerlist;

import com.example.cootek.newfastframe.MainRepositoryManager;
import com.example.cootek.newfastframe.adapter.SingerListAdapter;
import com.example.cootek.newfastframe.mvp.singerlist.SingerListModel;
import com.example.cootek.newfastframe.mvp.singerlist.SingerListPresenter;
import com.example.cootek.newfastframe.ui.fragment.SingerListFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by COOTEK on 2017/9/2.
 */
@Module
public class SingerListModule {
    private SingerListFragment singerListFragment;


    public SingerListModule(SingerListFragment singerListFragment) {
        this.singerListFragment = singerListFragment;
    }


    @Provides
    public SingerListAdapter provideSingerListAdapter() {
        return new SingerListAdapter();
    }

    @Provides
    public SingerListPresenter provideSingerListPresenter(SingerListModel singerListModel) {
        return new SingerListPresenter(singerListFragment, singerListModel);
    }

    @Provides
    public SingerListModel provideSingerListModel(MainRepositoryManager mainRepositoryManager) {
        return new SingerListModel(mainRepositoryManager);
    }
}
