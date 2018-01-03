package com.example.chat.dagger.commentdetail;

import com.example.chat.dagger.ChatMainComponent;
import com.example.chat.mvp.commentdetail.CommentListDetailActivity;
import com.example.commonlibrary.dagger.scope.PerActivity;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/3     16:17
 * QQ:         1981367757
 */
@PerActivity
@Component(dependencies = ChatMainComponent.class,modules = CommentDetailModule.class)
public interface CommentDetailComponent {
    public void inject(CommentListDetailActivity commentListDetailActivity);
}
