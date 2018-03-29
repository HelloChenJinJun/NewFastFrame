package com.example.chat.dagger.chat;

import com.example.chat.dagger.ChatMainComponent;
import com.example.chat.mvp.chat.ChatActivity;
import com.example.commonlibrary.dagger.scope.PerActivity;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/29     23:20
 * QQ:         1981367757
 */
@PerActivity
@Component(dependencies = ChatMainComponent.class, modules = ChatActivityModule.class)
public interface ChatActivityComponent {
    public void inject(ChatActivity chatActivity);
}
