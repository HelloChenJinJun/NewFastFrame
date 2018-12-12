package com.snew.video.dagger.qq;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.snew.video.adapter.VideoAdapter;
import com.snew.video.mvp.qq.QQVideoListFragment;
import com.snew.video.mvp.qq.QQVideoListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/12     16:05
 */
@Module
public class QQVideoListModule {
    private QQVideoListFragment mQQVideoListFragment;

    public QQVideoListModule(QQVideoListFragment QQVideoListFragment) {
        mQQVideoListFragment = QQVideoListFragment;
    }


    @Provides
    public QQVideoListPresenter providePresenter(DefaultModel defaultModel) {
        return new QQVideoListPresenter(mQQVideoListFragment, defaultModel);
    }

    @Provides
    VideoAdapter provideAdapter() {
        return new VideoAdapter();
    }

}
