package com.example.chat.adapter;

import android.text.SpannableStringBuilder;

import com.example.chat.R;
import com.example.chat.base.Constant;
import com.example.chat.bean.post.CommentDetailBean;
import com.example.chat.bean.post.PostDataBean;
import com.example.chat.bean.post.PublicCommentBean;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.chat.util.FaceTextUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.bean.chat.UserEntity;
import com.google.gson.Gson;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/5/19     11:16
 */

public class CommentNotifyAdapter extends BaseRecyclerAdapter<PublicCommentBean,BaseWrappedViewHolder>{
    private Gson gson= BaseApplication.getAppComponent()
            .getGson();
    @Override
    protected int getLayoutId() {
        return R.layout.item_activity_comment_notify;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, PublicCommentBean data) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        CommentDetailBean commentDetailBean =gson.fromJson(data.getContent(), CommentDetailBean.class);
        spannableStringBuilder.append(data.getUser().getNick());

        if (commentDetailBean.getReplyContent() != null) {
            spannableStringBuilder.append(" 回复 ");
            String[] uidList=commentDetailBean.getPublicId().split("&");
            String uid;
            if (!uidList[0].equals(UserManager.getInstance().getCurrentUserObjectId())) {
                uid=uidList[0];
            }else {
                uid=uidList[1];
            }
            UserEntity replyUser= UserDBManager.getInstance()
                    .getUser(uid);
            spannableStringBuilder.append(replyUser.getName());
        }
        spannableStringBuilder.append("：");
        spannableStringBuilder.append(FaceTextUtil.toSpannableString(holder.itemView.getContext(), commentDetailBean.getContent()));
        holder.setImageUrl(R.id.riv_item_activity_comment_notify_avatar
                ,data.getUser().getAvatar())
                .setText(R.id.tv_item_activity_comment_notify_name,data
                        .getUser().getNick())
                .setText(R.id.tv_item_activity_comment_notify_content,spannableStringBuilder)
                .setText(R.id.tv_item_activity_comment_notify_time,data.getCreatedAt())
                .setOnItemClickListener();
        Integer mediaType=data.getPost().getMsgType();
        PostDataBean bean=gson.fromJson(data.getPost().getContent(),PostDataBean.class);
        if (mediaType.equals(Constant.EDIT_TYPE_IMAGE)||mediaType.equals(Constant.EDIT_TYPE_VIDEO)) {
            String url=null;
            if (mediaType.equals(Constant.EDIT_TYPE_IMAGE)) {
                url=bean.getImageList().get(0);
            }else {
                for (String str :
                        bean.getImageList()) {
                    if (!str.endsWith(".mp4")) {
                        url=str;
                    }
                }
            }
            holder.setVisible(R.id.iv_item_activity_comment_notify_image,true)
                    .setVisible(R.id.tv_item_activity_comment_notify_text,false).setImageUrl(R.id.iv_item_activity_comment_notify_image,url);
        }else {
            holder.setVisible(R.id.iv_item_activity_comment_notify_image,false)
                    .setVisible(R.id.tv_item_activity_comment_notify_text,true).setText(R.id.tv_item_activity_comment_notify_text
                    ,bean.getContent());
        }
    }
}
