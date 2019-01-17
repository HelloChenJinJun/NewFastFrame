package com.snew.video.dagger.preview;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.snew.video.adapter.QQVideoListAdapter;
import com.snew.video.mvp.preview.PreViewVideoFragment;
import com.snew.video.mvp.preview.PreViewVideoPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2019/1/9     16:17
 */
@Module
public class PreViewVideoModule {
    private PreViewVideoFragment mPreViewVideoActivity;

    public PreViewVideoModule(PreViewVideoFragment preViewVideoActivity) {
        mPreViewVideoActivity = preViewVideoActivity;
    }


    @Provides
    public PreViewVideoPresenter providePresenter(DefaultModel defaultModel) {
        return new PreViewVideoPresenter(mPreViewVideoActivity, defaultModel);
    }


    @Provides
    public QQVideoListAdapter provideAdapter(){
        return new QQVideoListAdapter();
    }
}
