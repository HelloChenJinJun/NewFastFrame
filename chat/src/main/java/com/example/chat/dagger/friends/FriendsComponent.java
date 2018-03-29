package com.example.chat.dagger.friends;


import com.example.chat.dagger.ChatMainComponent;
import com.example.chat.mvp.main.friends.FriendsFragment;
import com.example.commonlibrary.dagger.scope.PerFragment;

import dagger.Component;

/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/17     15:53
 * QQ:         1981367757
 */
@PerFragment
@Component(dependencies = ChatMainComponent.class, modules = FriendsModule.class)
public interface FriendsComponent {
    public void inject(FriendsFragment friendsFragment);
}
