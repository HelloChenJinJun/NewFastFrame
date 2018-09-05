package com.example.live.dagger.list;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.live.adapter.ListLiveAdapter;
import com.example.live.mvp.list.ListLiveFragment;
import com.example.live.mvp.list.ListLivePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/14      21:09
 * QQ:             1981367757
 */
@Module
public class ListLiveModule {


    private ListLiveFragment listLiveFragment;

    public ListLiveModule(ListLiveFragment listLiveFragment) {
        this.listLiveFragment = listLiveFragment;
    }

    @Provides
    public ListLiveAdapter provideListLiveAdapter() {
        return new ListLiveAdapter();
    }

    @Provides
    public ListLivePresenter provideListLivePresenter(DefaultModel listLiveModel) {
        return new ListLivePresenter(listLiveFragment, listLiveModel);
    }


}
