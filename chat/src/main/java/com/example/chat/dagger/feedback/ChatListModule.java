package com.example.chat.dagger.feedback;

import com.example.chat.adapter.ChatListAdapter;
import com.example.chat.mvp.feedback.ChatListActivity;
import com.example.chat.mvp.feedback.ChatListPresenter;
import com.example.commonlibrary.mvp.model.DefaultModel;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    SecondhandMarket
 * 创建人:      李晨
 * 创建时间:    2018/4/26     23:20
 * QQ:         1981367757
 */
@Module
public class ChatListModule {
    private ChatListActivity chatListActivity;

    public ChatListModule(ChatListActivity chatListActivity) {
        this.chatListActivity = chatListActivity;
    }

    @Provides
    public ChatListAdapter providerAdapter(){
        return new ChatListAdapter();
    }

    @Provides
    public ChatListPresenter providerPresenter(DefaultModel defaultModel){
        return new ChatListPresenter(chatListActivity,defaultModel);
    }


}
