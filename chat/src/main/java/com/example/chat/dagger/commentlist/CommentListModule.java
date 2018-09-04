package com.example.chat.dagger.commentlist;

import com.example.chat.adapter.CommentListAdapter;
import com.example.chat.mvp.commentlist.CommentListActivity;
import com.example.chat.mvp.commentlist.CommentListPresenter;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.repository.DefaultRepositoryManager;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/3     15:12
 * QQ:         1981367757
 */
@Module
public class CommentListModule {
    private CommentListActivity commentListActivity;

    public CommentListModule(CommentListActivity commentListActivity) {
        this.commentListActivity = commentListActivity;
    }

    @Provides
    public CommentListAdapter providerAdapter() {
        return new CommentListAdapter();
    }


    @Provides
    public CommentListPresenter providerPresenter(DefaultModel defaultModel) {
        return new CommentListPresenter(commentListActivity, defaultModel);
    }
}
