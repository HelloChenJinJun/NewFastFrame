package com.example.news.dagger.cardinfo;

import com.example.news.mvp.cardinfo.CardInfoActivity;
import com.example.news.mvp.cardinfo.CardInfoModel;
import com.example.news.mvp.cardinfo.CardInfoPresenter;
import com.example.news.MainRepositoryManager;

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
    public CardInfoPresenter provideCardInfoPresenter(CardInfoModel cardInfoModel){
        return new CardInfoPresenter(cardInfoActivity,cardInfoModel);
    }


    @Provides
    public CardInfoModel provideCardInfoModel(MainRepositoryManager mainRepositoryManager){
        return new CardInfoModel(mainRepositoryManager);
    }
}
