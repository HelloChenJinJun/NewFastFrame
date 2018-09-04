package com.example.news.dagger.systemcenter;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.news.mvp.systemcenter.SystemCenterActivity;
import com.example.news.mvp.systemcenter.SystemCenterPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/23     18:04
 * QQ:         1981367757
 */
@Module
public class SystemCenterModule {
    private SystemCenterActivity systemCenterActivity;

    public SystemCenterModule(SystemCenterActivity systemCenterActivity) {
        this.systemCenterActivity = systemCenterActivity;
    }


    @Provides
    public SystemCenterPresenter providerPresenter(DefaultModel systemCenterModel) {
        return new SystemCenterPresenter(systemCenterActivity, systemCenterModel);
    }





}
