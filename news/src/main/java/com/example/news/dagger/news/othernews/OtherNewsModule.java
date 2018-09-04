package com.example.news.dagger.news.othernews;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.news.mvp.news.othernew.OtherNewsListAdapter;
import com.example.news.mvp.news.othernew.OtherNewsListFragment;
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
    public OtherNewsListPresenter provideOtherNewsListPresenter(DefaultModel otherNewsListModel) {
        return new OtherNewsListPresenter(otherNewsListFragment, otherNewsListModel);
    }


}
