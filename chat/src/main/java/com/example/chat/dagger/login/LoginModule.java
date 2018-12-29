package com.example.chat.dagger.login;

import com.example.chat.mvp.login.pw.PwLoginFragment;
import com.example.chat.mvp.login.pw.PwLoginPresenter;
import com.example.commonlibrary.mvp.model.DefaultModel;

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
    private PwLoginFragment mPwLoginFragment;


    public LoginModule(PwLoginFragment pwLoginFragment) {
        this.mPwLoginFragment=pwLoginFragment;
    }


    @Provides
    public PwLoginPresenter providerPresenter(DefaultModel defaultModel) {
        return new PwLoginPresenter(mPwLoginFragment, defaultModel);
    }





}
