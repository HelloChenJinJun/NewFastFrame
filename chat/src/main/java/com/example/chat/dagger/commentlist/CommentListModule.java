package com.example.chat.dagger.commentlist;

import com.example.chat.MainRepositoryManager;
import com.example.chat.adapter.CommentListAdapter;
import com.example.chat.mvp.commentlist.CommentListActivity;
import com.example.chat.mvp.commentlist.CommentListModel;
import com.example.chat.mvp.commentlist.CommentListPresenter;

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
    public CommentListPresenter providerPresenter(CommentListModel commentListModel) {
        return new CommentListPresenter(commentListActivity, commentListModel);
    }


    @Provides
    public CommentListModel providerModel(MainRepositoryManager mainRepositoryManager) {
        return new CommentListModel(mainRepositoryManager);
    }


}
