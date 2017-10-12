package com.example.chat.adapter.holder;

import android.view.View;

import com.example.chat.R;
import com.example.chat.adapter.OnShareMessageItemClickListener;
import com.example.chat.bean.SharedMessage;
import com.example.chat.bean.User;
import com.example.chat.manager.UserCacheManager;
import com.example.chat.manager.UserManager;
import com.example.chat.util.FaceTextUtil;
import com.example.chat.util.LogUtil;
import com.example.chat.util.TimeUtil;
import com.example.chat.view.CommentListView;
import com.example.chat.view.LikerTextView;
import com.example.chat.view.UrlTextView;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/3/26      14:02
 * QQ:             1981367757
 */

public class BaseShareMessageViewHolder extends BaseWrappedViewHolder {

        public BaseShareMessageViewHolder(View itemView) {
                super(itemView);
        }


        public void initCommonData(final SharedMessage shareMessage, final OnShareMessageItemClickListener listener) {
                User user;
//                删除按钮
                setVisible(R.id.tv_share_fragment_item_main_link_title,false);
                if (shareMessage.getBelongId().equals(UserManager.getInstance().getCurrentUserObjectId())) {
                        user = UserManager.getInstance().getCurrentUser();
                        setVisible(R.id.tv_share_fragment_item_main_delete, true);
                        setOnClickListener(R.id.tv_share_fragment_item_main_delete, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                        if (listener != null) {
                                                listener.onDeleteShareMessageClick(shareMessage.getObjectId());
                                        }
                                }
                        });
                } else {
                        user = UserCacheManager.getInstance().getUser(shareMessage.getBelongId());
                        setVisible(R.id.tv_share_fragment_item_main_delete, false);
                }
//                地址显示按钮
                if (shareMessage.getAddress() != null && !shareMessage.getAddress().equals("")) {
                        setVisible(R.id.tv_share_message_item_main_location, true);
                        setText(R.id.tv_share_message_item_main_location, shareMessage.getAddress());
                } else {
                        setVisible(R.id.tv_share_message_item_main_location, false);
                }
//                头像
                setImageUrl(R.id.riv_share_fragment_item_main_avatar, user.getAvatar())
//               创建时间
                        .setText(R.id.tv_share_fragment_item_main_time, TimeUtil.getRealTime(shareMessage.getCreatedAt()))
//                昵称
                        .setText(R.id.tv_share_fragment_item_main_name, user.getNick() != null && user.getNick().equals("") ? user.getNick() : user.getUsername())
                        .setOnClickListener(R.id.tv_share_fragment_item_main_name, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                        if (listener != null) {
                                                listener.onNameClick(shareMessage.getBelongId());
                                        }
                                }
                        })
                        .setOnClickListener(R.id.riv_share_fragment_item_main_avatar, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                        if (listener != null) {
                                                listener.onImageAvatarClick(shareMessage.getBelongId());
                                        }
                                }
                        })
                        .setOnClickListener(R.id.iv_share_fragment_item_main_comment, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                        if (listener != null) {
                                                listener.onCommentBtnClick(v, shareMessage.getObjectId(), getAdapterPosition(), shareMessage.getLikerList().contains(UserManager.getInstance().getCurrentUserObjectId()));
                                        }
                                }
                        });
//                评论列表
                CommentListView commentListView = (CommentListView) getView(R.id.clv_share_fragment_item_main_comment_list);
                commentListView.setCommentItemClickListener(new CommentListView.CommentItemClickListener() {

                        @Override
                        public void onCommentItemClick(View view, int position, String replyUid) {
                                if (listener != null) {
                                        listener.onCommentItemClick(view, shareMessage.getObjectId(), getAdapterPosition(), position, replyUid);
                                }
                        }

                        @Override
                        public void onCommentItemLongClick(int position) {
                                if (listener != null) {
                                        listener.onCommentItemLongClick(shareMessage.getObjectId(), getAdapterPosition(), position);
                                }
                        }

                        @Override
                        public void onCommentItemNameClick(String uid) {
                                if (listener != null) {
                                        listener.onCommentItemNameClick(uid);
                                }
                        }
                });
                commentListView.bindData(shareMessage.getCommentMsgList());
//                点赞列表
                LikerTextView likerTextView = ((LikerTextView) getView(R.id.ltv_share_fragment_item_main_liker));
                likerTextView.setLikerTextItemClickListener(new LikerTextView.LikerTextViewItemClickListener() {

                        @Override
                        public void onItemTextClick(String uid) {
                                if (listener != null) {
                                        listener.onLikerTextViewClick(uid);
                                }
                        }
                });
                likerTextView.bindData(shareMessage.getLikerList());
//                内容（包含链接）
                UrlTextView urlTextView = ((UrlTextView) getView(R.id.utv_share_fragment_item_main_content));
                urlTextView.setOnTextExpandStatusChangeListener(new UrlTextView.OnTextExpandStatusChangeListener() {
                        @Override
                        public void onExpandStatusChanged(boolean isExpand) {
                                LogUtil.e("展开");
                        }
                });
                urlTextView.setExpand(false);
                urlTextView.setText(FaceTextUtil.toSpannableString(itemView.getContext(), shareMessage.getContent()));
        }
}
