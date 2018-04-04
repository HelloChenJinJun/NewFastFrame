package com.example.chat.dagger.nearbyPeople;

import com.example.chat.MainRepositoryManager;
import com.example.chat.adapter.NearbyPeopleAdapter;
import com.example.chat.mvp.NearByPeople.NearbyPeopleActivity;
import com.example.chat.mvp.NearByPeople.NearbyPeopleModel;
import com.example.chat.mvp.NearByPeople.NearbyPeoplePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/4/4     10:01
 * QQ:         1981367757
 */
@Module
public class NearbyPeopleModule {
    private NearbyPeopleActivity nearbyPeopleActivity;

    public NearbyPeopleModule(NearbyPeopleActivity nearbyPeopleActivity) {
        this.nearbyPeopleActivity = nearbyPeopleActivity;
    }

    @Provides
    public NearbyPeoplePresenter providerPresenter(NearbyPeopleModel model){
        return new NearbyPeoplePresenter(nearbyPeopleActivity,model);
    }

    @Provides
    public NearbyPeopleModel providerModel(MainRepositoryManager mainRepositoryManager){
        return new NearbyPeopleModel(mainRepositoryManager);
    }



    @Provides
    public NearbyPeopleAdapter providerAdapter(){
        return new NearbyPeopleAdapter();
    }

}
