package com.example.chat.dagger.notify;

import com.example.chat.MainRepositoryManager;
import com.example.chat.adapter.SystemNotifyAdapter;
import com.example.chat.mvp.notify.SystemNotifyActivity;
import com.example.chat.mvp.notify.SystemNotifyModel;
import com.example.chat.mvp.notify.SystemNotifyPresenter;

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
    public SystemNotifyPresenter providerPresenter(SystemNotifyModel systemNotifyModel){
        return new SystemNotifyPresenter(systemNotifyActivity,systemNotifyModel);
    }


    @Provides
    public SystemNotifyModel providerModel(MainRepositoryManager mainRepositoryManager){
        return new SystemNotifyModel(mainRepositoryManager);
    }




}
