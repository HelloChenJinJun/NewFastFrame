package com.example.chat.dagger.chat;

import com.example.chat.MainRepositoryManager;
import com.example.chat.adapter.ChatMessageAdapter;
import com.example.chat.mvp.chat.ChatActivity;
import com.example.chat.mvp.chat.ChatModel;
import com.example.chat.mvp.chat.ChatPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/29     23:20
 * QQ:         1981367757
 */
@Module
public class ChatActivityModule {
    private ChatActivity chatActivity;

    public ChatActivityModule(ChatActivity chatActivity) {
        this.chatActivity = chatActivity;
    }


    @Provides
    public ChatMessageAdapter providerAdapter(){
        return  new ChatMessageAdapter();
    }

    @Provides
    public ChatPresenter providerPresenter(ChatModel chatModel){
        return new ChatPresenter(chatActivity,chatModel);
    }

    @Provides
    public ChatModel providerModel(MainRepositoryManager mainRepositoryManager){
        return new ChatModel(mainRepositoryManager);
    }
}
