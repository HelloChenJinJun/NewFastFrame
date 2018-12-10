package com.snew.video.base;

import com.example.commonlibrary.SlideBaseActivity;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.snew.video.dagger.VideoComponent;

/**
 * 项目名称:    Update
 * 创建人:      陈锦军
 * 创建时间:    2018/11/30     15:36
 */
public abstract class VideoBaseActivity<T, P extends BasePresenter> extends SlideBaseActivity<T, P> {
    public VideoComponent getComponent() {
        return VideoApplication.getMainComponent();
    }


}
