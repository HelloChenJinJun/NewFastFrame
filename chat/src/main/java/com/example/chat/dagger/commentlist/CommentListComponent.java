package com.example.chat.dagger.commentlist;

import com.example.chat.dagger.ChatMainComponent;
import com.example.chat.mvp.commentlist.CommentListActivity;
import com.example.commonlibrary.dagger.scope.PerActivity;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/3     15:12
 * QQ:         1981367757
 */
@PerActivity
@Component(dependencies = ChatMainComponent.class, modules = CommentListModule.class)
public interface CommentListComponent {
    public void inject(CommentListActivity commentListActivity);
}
