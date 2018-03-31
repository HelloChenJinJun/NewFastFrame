package com.example.chat.dagger.friends;


import com.example.chat.MainRepositoryManager;
import com.example.chat.adapter.FriendsAdapter;
import com.example.chat.mvp.main.friends.FriendsFragment;
import com.example.chat.mvp.main.friends.FriendsModel;
import com.example.chat.mvp.main.friends.FriendsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/17     15:54
 * QQ:         1981367757
 */
@Module
public class FriendsModule {


    private FriendsFragment friendsFragment;

    public FriendsModule(FriendsFragment friendsFragment) {
        this.friendsFragment = friendsFragment;
    }


    @Provides
    public FriendsAdapter providerAdapter(){
        FriendsAdapter friendsAdapter=new FriendsAdapter();
        friendsAdapter.setHasSelected(false);
        return friendsAdapter;
    }



    @Provides
    public FriendsPresenter providerPresenter(FriendsModel friendsModel) {
        return new FriendsPresenter(friendsFragment, friendsModel);
    }


    @Provides
    public FriendsModel providerModel(MainRepositoryManager mainRepositoryManager) {
        return new FriendsModel(mainRepositoryManager);
    }


}
