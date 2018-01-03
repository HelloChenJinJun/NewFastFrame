package com.example.chat.dagger.commentdetail;

import com.example.chat.MainRepositoryManager;
import com.example.chat.adapter.CommentDetailAdapter;
import com.example.chat.mvp.commentdetail.CommentDetailModel;
import com.example.chat.mvp.commentdetail.CommentDetailPresenter;
import com.example.chat.mvp.commentdetail.CommentListDetailActivity;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/3     16:18
 * QQ:         1981367757
 */
@Module
public class CommentDetailModule {
   private CommentListDetailActivity commentListDetailActivity;

    public CommentDetailModule(CommentListDetailActivity commentListDetailActivity) {
        this.commentListDetailActivity = commentListDetailActivity;
    }

    @Provides
    public CommentDetailAdapter providerAdapter(){
        return new CommentDetailAdapter();
    }



    @Provides
    public CommentDetailPresenter providerPresenter(CommentDetailModel commentDetailModel){
        return new CommentDetailPresenter(commentListDetailActivity,commentDetailModel);
    }



    @Provides
    public CommentDetailModel providerModel(MainRepositoryManager mainRepositoryManager){
        return new CommentDetailModel(mainRepositoryManager);
    }




}
