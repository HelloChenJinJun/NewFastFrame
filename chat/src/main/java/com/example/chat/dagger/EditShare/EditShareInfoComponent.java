package com.example.chat.dagger.EditShare;

import com.example.chat.dagger.ChatMainComponent;
import com.example.chat.mvp.EditShare.EditShareInfoActivity;
import com.example.commonlibrary.dagger.scope.PerActivity;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/25     21:24
 * QQ:         1981367757
 */
@PerActivity
@Component(dependencies = ChatMainComponent.class, modules = EditShareInfoModule.class)
public interface EditShareInfoComponent {
    public void inject(EditShareInfoActivity editShareInfoActivity);
}
