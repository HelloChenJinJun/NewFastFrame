package com.example.news.dagger.news.othernews.detail;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.news.mvp.news.othernew.detail.OtherNewsDetailActivity;
import com.example.news.mvp.news.othernew.detail.OtherNewsDetailPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/26      16:08
 * QQ:             1981367757
 */
@Module
public class OtherNewsDetailModule {
    private OtherNewsDetailActivity otherNewsDetailActivity;

    public OtherNewsDetailModule(OtherNewsDetailActivity otherNewsDetailActivity) {
        this.otherNewsDetailActivity = otherNewsDetailActivity;
    }


    @Provides
    public OtherNewsDetailPresenter provideOtherNewsDetailPresenter(DefaultModel otherNewsDetailModel){
        return new OtherNewsDetailPresenter(otherNewsDetailActivity,otherNewsDetailModel);
    }


}
