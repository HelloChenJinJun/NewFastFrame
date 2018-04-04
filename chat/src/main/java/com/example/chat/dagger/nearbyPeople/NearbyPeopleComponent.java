package com.example.chat.dagger.nearbyPeople;

import com.example.chat.dagger.ChatMainComponent;
import com.example.chat.mvp.NearByPeople.NearbyPeopleActivity;
import com.example.commonlibrary.dagger.scope.PerActivity;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/4/4     10:00
 * QQ:         1981367757
 */
@PerActivity
@Component(dependencies = ChatMainComponent.class,modules = NearbyPeopleModule.class)
public interface NearbyPeopleComponent {
    public void inject(NearbyPeopleActivity nearbyPeopleActivity);

}
