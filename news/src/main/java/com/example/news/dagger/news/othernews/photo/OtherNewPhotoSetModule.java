package com.example.news.dagger.news.othernews.photo;

import com.example.news.MainRepositoryManager;
import com.example.news.adapter.OtherNewPhotoSetAdapter;
import com.example.news.mvp.news.othernew.photo.OtherNewPhotoSetActivity;
import com.example.news.mvp.news.othernew.photo.OtherNewPhotoSetModel;
import com.example.news.mvp.news.othernew.photo.OtherNewPhotoSetPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/26      17:04
 * QQ:             1981367757
 */
@Module
public class OtherNewPhotoSetModule {
    private OtherNewPhotoSetActivity otherNewPhotoSetActivity;

    public OtherNewPhotoSetModule(OtherNewPhotoSetActivity otherNewPhotoSetActivity) {
        this.otherNewPhotoSetActivity = otherNewPhotoSetActivity;
    }


    @Provides
    public OtherNewPhotoSetAdapter provideOtherNewPhotoSetAdater(){
        return new OtherNewPhotoSetAdapter();
    }


    @Provides
    public OtherNewPhotoSetPresenter providePhotoSetPresenter(OtherNewPhotoSetModel otherNewPhotoSetModel){
        return new OtherNewPhotoSetPresenter(otherNewPhotoSetActivity,otherNewPhotoSetModel);
    }


    @Provides
    public OtherNewPhotoSetModel providePhotoSetModel(MainRepositoryManager mainRepositoryManager){
        return new OtherNewPhotoSetModel(mainRepositoryManager);
    }
}
