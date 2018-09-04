package com.example.news.dagger.cardlogin;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.news.mvp.cardlogin.CardLoginActivity;
import com.example.news.mvp.cardlogin.CardLoginPresenter;

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
    public CardLoginPresenter provideCardLoginPresenter(DefaultModel cardLoginModel) {
        return new CardLoginPresenter(cardLoginActivity, cardLoginModel);
    }

}
