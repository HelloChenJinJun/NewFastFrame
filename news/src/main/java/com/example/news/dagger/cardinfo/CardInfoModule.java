package com.example.news.dagger.cardinfo;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.news.mvp.cardinfo.CardInfoActivity;
import com.example.news.mvp.cardinfo.CardInfoPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/19      23:22
 * QQ:             1981367757
 */
@Module
public class CardInfoModule {
    private CardInfoActivity cardInfoActivity;

    public CardInfoModule(CardInfoActivity cardInfoActivity) {
        this.cardInfoActivity = cardInfoActivity;
    }


    @Provides
    public CardInfoPresenter provideCardInfoPresenter(DefaultModel defaultModel){
        return new CardInfoPresenter(cardInfoActivity,defaultModel);
    }



}
