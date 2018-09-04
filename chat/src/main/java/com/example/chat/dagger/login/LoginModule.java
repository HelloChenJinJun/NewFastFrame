package com.example.chat.dagger.login;

import com.example.chat.mvp.login.LoginPresenter;
import com.example.chat.mvp.login.LoginActivity;
import com.example.commonlibrary.mvp.model.DefaultModel;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/27     13:48
 * QQ:         1981367757
 */
@Module
public class LoginModule {
    private LoginActivity loginActivity;


    public LoginModule(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }


    @Provides
    public LoginPresenter providerPresenter(DefaultModel defaultModel) {
        return new LoginPresenter(loginActivity, defaultModel);
    }





}
