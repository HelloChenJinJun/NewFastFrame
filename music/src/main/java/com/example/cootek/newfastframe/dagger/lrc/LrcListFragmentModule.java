package com.example.cootek.newfastframe.dagger.lrc;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.cootek.newfastframe.mvp.lrc.LrcListFragment;
import com.example.cootek.newfastframe.mvp.lrc.LrcListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/7     18:06
 */
@Module
public class LrcListFragmentModule {
    private LrcListFragment mLrcListFragment;

    public LrcListFragmentModule(LrcListFragment lrcListFragment) {
        mLrcListFragment = lrcListFragment;
    }


    @Provides
    public LrcListPresenter providePresenter(DefaultModel defaultModel) {
        return new LrcListPresenter(mLrcListFragment, defaultModel);
    }
}
