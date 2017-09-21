package com.example.news.dagger.cardlogin;

import com.example.news.mvp.cardlogin.CardLoginActivity;
import com.example.news.mvp.cardlogin.CardLoginModel;
import com.example.news.mvp.cardlogin.CardLoginPresenter;
import com.example.news.MainRepositoryManager;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/19      23:08
 * QQ:             1981367757
 */
@Module
public class CardLoginModule {
    private CardLoginActivity cardLoginActivity;

    public CardLoginModule(CardLoginActivity cardLoginActivity) {
        this.cardLoginActivity = cardLoginActivity;
    }


    @Provides
    public CardLoginPresenter provideCardLoginPresenter(CardLoginModel cardLoginModel) {
        return new CardLoginPresenter(cardLoginActivity, cardLoginModel);
    }

    @Provides
    public CardLoginModel provideCardLoginModel(MainRepositoryManager mainRepositoryManager) {
        return new CardLoginModel(mainRepositoryManager);
    }
}
