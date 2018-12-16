package com.snew.video.dagger.qq.detail;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.snew.video.adapter.VideoPersonAdapter;
import com.snew.video.adapter.VideoTVItemAdapter;
import com.snew.video.adapter.VideoTagAdapter;
import com.snew.video.mvp.qq.detail.QQVideoDetailActivity;
import com.snew.video.mvp.qq.detail.QQVideoDetailPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/14     14:49
 */
@Module
public class QQVideoDetailModule {
    private QQVideoDetailActivity mQQVideoDetailActivity;

    public QQVideoDetailModule(QQVideoDetailActivity QQVideoDetailActivity) {
        mQQVideoDetailActivity = QQVideoDetailActivity;
    }


    @Provides
    public QQVideoDetailPresenter providePresenter(DefaultModel defaultModel) {
        return new QQVideoDetailPresenter(mQQVideoDetailActivity, defaultModel);
    }

    @Provides
    public VideoTVItemAdapter provideTVAdapter() {
        return new VideoTVItemAdapter();
    }

    @Provides
    public VideoTagAdapter provideTagAdapter() {
        return new VideoTagAdapter();
    }

    @Provides
    public VideoPersonAdapter providePersonAdapter() {
        return new VideoPersonAdapter();
    }
}
