package com.example.chat.dagger.shareinfo;

import com.example.chat.dagger.ChatMainComponent;
import com.example.chat.mvp.shareinfo.ShareInfoFragment;
import com.example.commonlibrary.dagger.scope.PerFragment;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/30     17:39
 * QQ:         1981367757
 */
@PerFragment
@Component(dependencies = ChatMainComponent.class, modules = ShareInfoModule.class)
public interface ShareInfoComponent {
    public void inject(ShareInfoFragment shareInfoFragment);
}
