package com.example.news.dagger.consume;

import com.example.news.MainRepositoryManager;
import com.example.news.adapter.ConsumeQueryAdapter;
import com.example.news.mvp.consume.ConsumeQueryActivity;
import com.example.news.mvp.consume.ConsumeQueryModel;
import com.example.news.mvp.consume.ConsumeQueryPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/21     18:03
 * QQ:         1981367757
 */
@Module
public class ConsumeQueryModule {
    private ConsumeQueryActivity consumeQueryActivity;

    public ConsumeQueryModule(ConsumeQueryActivity consumeQueryActivity) {
        this.consumeQueryActivity = consumeQueryActivity;
    }


    @Provides
    public ConsumeQueryAdapter provideAdapter(){
        return new ConsumeQueryAdapter();
    }


    @Provides
    public ConsumeQueryPresenter providePresenter(ConsumeQueryModel model){
        return new ConsumeQueryPresenter(consumeQueryActivity,model);
    }



    @Provides
    public ConsumeQueryModel provideModel(MainRepositoryManager mainRepositoryManager){
        return new ConsumeQueryModel(mainRepositoryManager);
    }
}
