package com.example.news.dagger.news.othernews.photolist;

import com.example.news.MainRepositoryManager;
import com.example.news.adapter.PhotoListAdapter;
import com.example.news.mvp.news.othernew.photolist.PhotoListFragment;
import com.example.news.mvp.news.othernew.photolist.PhotoListModel;
import com.example.news.mvp.news.othernew.photolist.PhotoListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/29      17:49
 * QQ:             1981367757
 */
@Module
public class PhotoListModule {

    private PhotoListFragment photoListFragment;

    public PhotoListModule(PhotoListFragment photoListFragment) {
        this.photoListFragment = photoListFragment;
    }


    @Provides
    public PhotoListAdapter providePhotoListAdapter(){
        return new PhotoListAdapter();
    }


    @Provides
    public PhotoListPresenter providePhotoListPresenter(PhotoListModel photoListModel){
        return new PhotoListPresenter(photoListFragment,photoListModel);
    }


    @Provides
    public PhotoListModel providePhotoListModel(MainRepositoryManager mainRepositoryManager){
        return new PhotoListModel(mainRepositoryManager);
    }
}
