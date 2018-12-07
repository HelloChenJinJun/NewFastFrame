package com.example.cootek.newfastframe.base;

import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.cootek.newfastframe.MusicApplication;
import com.example.cootek.newfastframe.dagger.main.MainComponent;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/7     9:59
 */
public abstract class MusicBaseFragment<T, P extends BasePresenter> extends BaseFragment<T, P> {


    protected MainComponent getMainComponent() {
        return MusicApplication.getMainComponent();
    }
}
