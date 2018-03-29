package com.example.chat.dagger.nearbyList;


import com.example.chat.MainRepositoryManager;
import com.example.chat.adapter.NearbyListAdapter;
import com.example.chat.mvp.nearbyList.NearbyListActivity;
import com.example.chat.mvp.nearbyList.NearbyListModel;
import com.example.chat.mvp.nearbyList.NearbyListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/28     9:59
 * QQ:         1981367757
 */
@Module
public class NearbyListModule {


    private NearbyListActivity nearbyListActivity;

    public NearbyListModule(NearbyListActivity nearbyListActivity) {
        this.nearbyListActivity = nearbyListActivity;
    }

    @Provides
    public NearbyListAdapter providerAdapter() {
        return new NearbyListAdapter();
    }


    @Provides
    public NearbyListPresenter providerPresenter(NearbyListModel nearbyListModel) {
        return new NearbyListPresenter(nearbyListActivity, nearbyListModel);
    }

    @Provides
    public NearbyListModel providerModel(MainRepositoryManager mainRepositoryManager) {
        return new NearbyListModel(mainRepositoryManager);
    }

}
