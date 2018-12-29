package com.example.chat.dagger.login;

import com.example.chat.mvp.login.phone.PhoneLoginFragment;
import com.example.chat.mvp.login.phone.PhoneLoginPresenter;
import com.example.commonlibrary.mvp.model.DefaultModel;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/29     16:30
 */
@Module
public class PhoneLoginModule {
    private PhoneLoginFragment mPhoneLoginFragment;

    public PhoneLoginModule(PhoneLoginFragment phoneLoginFragment) {
        mPhoneLoginFragment = phoneLoginFragment;
    }

    @Provides
    public PhoneLoginPresenter providePresenter(DefaultModel defaultModel) {
        return new PhoneLoginPresenter(mPhoneLoginFragment, defaultModel);
    }
}
