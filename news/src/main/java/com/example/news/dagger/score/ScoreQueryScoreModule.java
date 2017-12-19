package com.example.news.dagger.score;

import com.example.news.MainRepositoryManager;
import com.example.news.ScoreQueryActivity;
import com.example.news.adapter.ScoreQueryAdapter;
import com.example.news.mvp.score.ScoreQueryModel;
import com.example.news.mvp.score.ScoreQueryPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/19     15:19
 * QQ:         1981367757
 */
@Module
public class ScoreQueryScoreModule {
    private ScoreQueryActivity scoreQueryActivity;

    public ScoreQueryScoreModule(ScoreQueryActivity scoreQueryActivity) {
        this.scoreQueryActivity = scoreQueryActivity;
    }

    @Provides
    public ScoreQueryAdapter provideAdapter(){
        return new ScoreQueryAdapter();
    }


    @Provides
    public ScoreQueryPresenter providePresenter(ScoreQueryModel scoreQueryModel){
        return new ScoreQueryPresenter(scoreQueryActivity,scoreQueryModel);
    }



    @Provides
    public ScoreQueryModel provideModel(MainRepositoryManager mainRepositoryManager){
        return new ScoreQueryModel(mainRepositoryManager);
    }

}
