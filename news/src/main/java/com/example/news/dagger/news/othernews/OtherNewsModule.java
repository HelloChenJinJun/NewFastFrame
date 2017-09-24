package com.example.news.dagger.news.othernews;

import com.example.news.MainRepositoryManager;
import com.example.news.mvp.news.othernew.OtherNewsListAdapter;
import com.example.news.mvp.news.othernew.OtherNewsListFragment;
import com.example.news.mvp.news.othernew.OtherNewsListModel;
import com.example.news.mvp.news.othernew.OtherNewsListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/24      18:33
 * QQ:             1981367757
 */
@Module
public class OtherNewsModule {
    private OtherNewsListFragment otherNewsListFragment;


    public OtherNewsModule(OtherNewsListFragment otherNewsListFragment) {
        this.otherNewsListFragment = otherNewsListFragment;
    }


    @Provides
    public OtherNewsListAdapter provideOtherNewsListAdapter() {
        return new OtherNewsListAdapter();
    }


    @Provides
    public OtherNewsListPresenter provideOtherNewsListPresenter(OtherNewsListModel otherNewsListModel) {
        return new OtherNewsListPresenter(otherNewsListFragment, otherNewsListModel);
    }

    @Provides
    public OtherNewsListModel provideOtherNewsListModel(MainRepositoryManager mainRepositoryManager) {
        return new OtherNewsListModel(mainRepositoryManager);
    }
}
