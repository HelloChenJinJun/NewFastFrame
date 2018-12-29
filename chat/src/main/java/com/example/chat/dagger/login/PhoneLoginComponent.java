package com.example.chat.dagger.login;

import com.example.chat.dagger.ChatMainComponent;
import com.example.chat.mvp.login.phone.PhoneLoginFragment;
import com.example.commonlibrary.dagger.scope.PerFragment;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/29     16:29
 */
@PerFragment
@Component(dependencies = ChatMainComponent.class, modules = PhoneLoginModule.class)
public interface PhoneLoginComponent {
    public void inject(PhoneLoginFragment phoneLoginFragment);
}
