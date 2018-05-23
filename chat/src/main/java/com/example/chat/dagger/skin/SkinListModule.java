package com.example.chat.dagger.skin;

import com.example.chat.MainRepositoryManager;
import com.example.chat.adapter.SkinListAdapter;
import com.example.chat.mvp.skin.SkinListActivity;
import com.example.chat.mvp.skin.SkinListModel;
import com.example.chat.mvp.skin.SkinListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/5/23     10:52
 */
@Module
public class SkinListModule {
    private SkinListActivity skinListActivity;

    public SkinListModule(SkinListActivity skinListActivity) {
        this.skinListActivity = skinListActivity;
    }

    @Provides
    public SkinListAdapter provideAdapter(){
        return new SkinListAdapter();
    }

    @Provides
    public SkinListPresenter providePresenter(SkinListModel skinListModel){
        return new SkinListPresenter(skinListActivity,skinListModel);
    }

    @Provides
    public SkinListModel provideModel(MainRepositoryManager mainRepositoryManager){
        return new SkinListModel(mainRepositoryManager);
    }


}
