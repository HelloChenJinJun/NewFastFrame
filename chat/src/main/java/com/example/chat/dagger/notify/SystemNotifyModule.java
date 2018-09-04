package com.example.chat.dagger.notify;

import com.example.chat.adapter.SystemNotifyAdapter;
import com.example.chat.mvp.notify.SystemNotifyActivity;
import com.example.chat.mvp.notify.SystemNotifyPresenter;
import com.example.commonlibrary.mvp.model.DefaultModel;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/16     14:33
 * QQ:         1981367757
 */
@Module
public class SystemNotifyModule {
    private SystemNotifyActivity systemNotifyActivity;

    public SystemNotifyModule(SystemNotifyActivity systemNotifyActivity) {
        this.systemNotifyActivity = systemNotifyActivity;
    }


    @Provides
    public SystemNotifyAdapter providerAdapter(){
        return new SystemNotifyAdapter();
    }



    @Provides
    public SystemNotifyPresenter providerPresenter(DefaultModel defaultModel){
        return new SystemNotifyPresenter(systemNotifyActivity,defaultModel);
    }







}
