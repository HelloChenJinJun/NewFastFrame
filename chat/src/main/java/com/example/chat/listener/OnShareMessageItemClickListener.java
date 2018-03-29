package com.example.chat.listener;

import android.view.View;

import com.example.chat.bean.SharedMessage;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/3/26      15:30
 * QQ:             1981367757
 */

public interface OnShareMessageItemClickListener {
        void onImageAvatarClick(String uid);

        void onNameClick(String uid);

        void onCommentBtnClick(View view, String id, int shareMessagePosition, boolean isLike);

        void onCommentItemClick(View view, String id, int shareMessagePosition, int commentPosition, String replyUid);

        void onCommentItemLongClick(String id, int shareMessagePosition, int commentPosition);

        void onCommentItemNameClick(String uid);

        void onLikerTextViewClick(String uid);

        void onLinkViewClick(SharedMessage shareMessage);

        void onDeleteShareMessageClick(String id);

        void onPhotoItemClick(View view, String id, int photoPosition, String url);

}
