package com.example.chat.dagger.account.password;

import com.example.chat.dagger.ChatMainComponent;
import com.example.chat.mvp.account.password.PasswordChangeActivity;
import com.example.commonlibrary.dagger.scope.PerActivity;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/14     12:59
 * QQ:         1981367757
 */
@PerActivity
@Component(dependencies = ChatMainComponent.class,modules = PasswordChangeModule.class)
public interface PasswordChangeComponent {
    public void inject(PasswordChangeActivity passwordChangeActivity);


}
