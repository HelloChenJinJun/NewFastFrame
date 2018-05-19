package com.example.chat.dagger.commentnotify;

import com.example.chat.dagger.ChatMainComponent;
import com.example.chat.mvp.commentnotify.CommentNotifyActivity;
import com.example.commonlibrary.dagger.scope.PerActivity;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/5/19     14:01
 */
@PerActivity
@Component(dependencies = ChatMainComponent.class,modules = CommentNotifyModule.class)
public interface CommentNotifyComponent {
    public void inject(CommentNotifyActivity commentNotifyActivity);
}
