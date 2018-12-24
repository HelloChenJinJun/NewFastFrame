package com.example.chat.dagger.feedback;

import com.example.chat.dagger.ChatMainComponent;
import com.example.chat.mvp.feedback.ChatListActivity;
import com.example.commonlibrary.dagger.scope.PerActivity;

import dagger.Component;

/**
 * 项目名称:    SecondhandMarket
 * 创建人:      李晨
 * 创建时间:    2018/4/26     23:20
 * QQ:         1981367757
 */
@PerActivity
@Component(dependencies = ChatMainComponent.class, modules = ChatListModule.class)
public interface ChatListComponent {
    public void inject(ChatListActivity chatListActivity);
}
