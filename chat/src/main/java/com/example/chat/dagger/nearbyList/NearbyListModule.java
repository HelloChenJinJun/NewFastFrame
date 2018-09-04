package com.example.chat.dagger.nearbyList;


import com.example.chat.adapter.NearbyListAdapter;
import com.example.chat.mvp.nearbyList.NearbyListActivity;
import com.example.chat.mvp.nearbyList.NearbyListPresenter;
import com.example.commonlibrary.mvp.model.DefaultModel;

import javax.inject.Named;

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
    public NearbyListPresenter providerPresenter(DefaultModel defaultModel) {
        return new NearbyListPresenter(nearbyListActivity, defaultModel);
    }



}
