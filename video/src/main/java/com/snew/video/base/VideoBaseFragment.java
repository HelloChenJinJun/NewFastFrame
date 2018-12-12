package com.snew.video.base;

import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.snew.video.dagger.VideoComponent;

/**
 * 项目名称:    Update
 * 创建人:      陈锦军
 * 创建时间:    2018/11/29     10:41
 */
public abstract class VideoBaseFragment<T, P extends BasePresenter> extends BaseFragment<T, P> {

    public VideoComponent getComponent() {
        return VideoApplication.getMainComponent();
    }

}
