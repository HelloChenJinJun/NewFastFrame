package com.example.chat.dagger.account.password;

import com.example.chat.MainRepositoryManager;
import com.example.chat.mvp.account.password.PasswordChangePresenter;
import com.example.chat.mvp.account.password.PasswordChangeActivity;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.repository.DefaultRepositoryManager;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/14     12:59
 * QQ:         1981367757
 */
@Module
public class PasswordChangeModule {
    private PasswordChangeActivity passwordChangeActivity;

    public PasswordChangeModule(PasswordChangeActivity passwordChangeActivity) {
        this.passwordChangeActivity = passwordChangeActivity;
    }

    @Provides
    public PasswordChangePresenter providerPresenter(DefaultModel defaultModel) {
        return new PasswordChangePresenter(passwordChangeActivity, defaultModel);
    }

    @Provides
    public DefaultModel providerModel(DefaultRepositoryManager defaultRepositoryManager){
        return new DefaultModel(defaultRepositoryManager);
    }



}



