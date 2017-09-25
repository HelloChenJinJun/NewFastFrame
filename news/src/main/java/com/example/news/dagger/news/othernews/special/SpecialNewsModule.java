package com.example.news.dagger.news.othernews.special;

import com.example.news.MainRepositoryManager;
import com.example.news.adapter.SpecialNewsAdapter;
import com.example.news.bean.SpecialNewsBean;
import com.example.news.mvp.news.othernew.special.ISpecialNewsView;
import com.example.news.mvp.news.othernew.special.SpecialNewsModel;
import com.example.news.mvp.news.othernew.special.SpecialNewsPresenter;

import java.util.List;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/25      20:22
 * QQ:             1981367757
 */
@Module
public class SpecialNewsModule {
    private ISpecialNewsView<List<SpecialNewsBean>> iSpecialNewsView;

    public SpecialNewsModule(ISpecialNewsView<List<SpecialNewsBean>> iSpecialNewsView) {
        this.iSpecialNewsView = iSpecialNewsView;
    }

    @Provides
    public SpecialNewsAdapter provideSpecialNewsAdapter(){
        return new SpecialNewsAdapter();
    }



    @Provides
    public SpecialNewsPresenter provideSpecialNewsPresenter(SpecialNewsModel specialNewsModel){
        return new SpecialNewsPresenter(iSpecialNewsView,specialNewsModel);
    }


    @Provides
    public SpecialNewsModel  provideSpecialNewsModel(MainRepositoryManager mainRepositoryManager){
        return new SpecialNewsModel(mainRepositoryManager);
    }

}
