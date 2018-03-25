package com.example.chat.dagger.notify;

import com.example.chat.dagger.ChatMainComponent;
import com.example.chat.mvp.notify.SystemNotifyActivity;
import com.example.commonlibrary.dagger.scope.PerActivity;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/16     14:33
 * QQ:         1981367757
 */
@PerActivity
@Component(dependencies = ChatMainComponent.class,modules = SystemNotifyModule.class)
public interface SystemNotifyComponent {
    public void inject(SystemNotifyActivity systemNotifyActivity);
}
