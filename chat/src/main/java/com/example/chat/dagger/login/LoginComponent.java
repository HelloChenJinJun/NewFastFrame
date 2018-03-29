package com.example.chat.dagger.login;

import com.example.chat.dagger.ChatMainComponent;
import com.example.chat.mvp.login.LoginActivity;
import com.example.commonlibrary.dagger.scope.PerActivity;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/27     13:48
 * QQ:         1981367757
 */
@PerActivity
@Component(dependencies = ChatMainComponent.class, modules = LoginModule.class)
public interface LoginComponent {
    public void inject(LoginActivity loginActivity);
}
