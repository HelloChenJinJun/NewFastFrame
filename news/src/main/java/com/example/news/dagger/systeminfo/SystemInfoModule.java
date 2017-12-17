package com.example.news.dagger.systeminfo;

import com.example.news.MainRepositoryManager;
import com.example.news.mvp.systeminfo.SystemInfoLoginActivity;
import com.example.news.mvp.systeminfo.SystemInfoLoginPresenter;
import com.example.news.mvp.systeminfo.SystemInfoModel;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/17     17:52
 * QQ:         1981367757
 */
@Module
public class SystemInfoModule {
    private SystemInfoLoginActivity systemInfoLoginActivity;


    public SystemInfoModule(SystemInfoLoginActivity systemInfoLoginActivity) {
        this.systemInfoLoginActivity = systemInfoLoginActivity;
    }


    @Provides
    public SystemInfoLoginPresenter provideSystemInfoPresenter(SystemInfoModel systemInfoModel){
        return new SystemInfoLoginPresenter(systemInfoLoginActivity,systemInfoModel);
    }

    @Provides
    public SystemInfoModel provideSystemModel(MainRepositoryManager mainRepositoryManager){
        return new SystemInfoModel(mainRepositoryManager);
    }
}
