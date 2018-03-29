package com.example.chat.dagger.nearbyList;


import com.example.chat.dagger.ChatMainComponent;
import com.example.chat.mvp.nearbyList.NearbyListActivity;
import com.example.commonlibrary.dagger.scope.PerActivity;

import dagger.Component;

/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/28     9:59
 * QQ:         1981367757
 */
@PerActivity
@Component(dependencies = ChatMainComponent.class, modules = NearbyListModule.class)
public interface NearbyListComponent {
    public void inject(NearbyListActivity nearbyListActivity);
}
