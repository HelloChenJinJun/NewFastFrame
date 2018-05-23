package com.example.chat.dagger.skin;

import com.example.chat.dagger.ChatMainComponent;
import com.example.chat.mvp.skin.SkinListActivity;
import com.example.commonlibrary.dagger.scope.PerActivity;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/5/23     10:51
 */
@PerActivity
@Component(dependencies = ChatMainComponent.class,modules = SkinListModule.class)
public interface SkinListComponent {
    public void inject(SkinListActivity skinListActivity);
}
