package com.example.chat.adapter;

import android.text.SpannableStringBuilder;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.base.Constant;
import com.example.chat.bean.PostNotifyBean;
import com.example.chat.bean.post.CommentDetailBean;
import com.example.chat.bean.post.PostDataBean;
import com.example.chat.bean.post.PublicCommentBean;
import com.example.chat.bean.post.PublicPostBean;
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

public class CommentNotifyAdapter extends BaseRecyclerAdapter<PostNotifyBean,BaseWrappedViewHolder>{
    private Gson gson= BaseApplication.getAppComponent()
            .getGson();
    @Override
    protected int getLayoutId() {
        return R.layout.item_activity_comment_notify;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, PostNotifyBean data) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (data.getType().equals(Constant.TYPE_COMMENT)) {
            CommentDetailBean commentDetailBean =gson.fromJson(data.getPublicCommentBean().getContent(), CommentDetailBean.class);
            if (commentDetailBean!=null&&commentDetailBean.getReplyContent() != null) {
                spannableStringBuilder.append(" 回复 ");
                String[] uidList=commentDetailBean.getPublicId().split("&");
                String uid;
                if (!uidList[0].equals(data.getPublicCommentBean().getUser().getObjectId())) {
                    uid=uidList[0];
                }else {
                    uid=uidList[1];
                }
                UserEntity replyUser= UserDBManager.getInstance()
                        .getUser(uid);
                spannableStringBuilder.append(replyUser.getName());
            }
            spannableStringBuilder.append("：");
            if (commentDetailBean!=null) {
                spannableStringBuilder.append(FaceTextUtil.toSpannableString(holder.itemView.getContext(), commentDetailBean.getContent()));
            }
            holder.setText(R.id.tv_item_activity_comment_notify_content,spannableStringBuilder);
        }else if (data.getType().equals(Constant.TYPE_LIKE)){
            ((TextView)holder.getView(R.id.tv_item_activity_comment_notify_content)).setCompoundDrawablesWithIntrinsicBounds(holder.itemView.getContext().getResources().getDrawable(R.drawable.ic_favorite_border_deep_orange_a700_24dp), null, null, null);
        }else {
            spannableStringBuilder.append("转发了该条说说");
            holder.setText(R.id.tv_item_activity_comment_notify_content,spannableStringBuilder);
        }
        holder.setImageUrl(R.id.riv_item_activity_comment_notify_avatar
                ,data.getRelatedUser().getAvatar())
                .setText(R.id.tv_item_activity_comment_notify_name,data
                        .getRelatedUser().getNick())
                .setText(R.id.tv_item_activity_comment_notify_time,data.getCreatedAt())
                .setOnItemClickListener();
        PublicPostBean temp=data.getPublicPostBean();
        if (temp==null) {
            temp=data.getPublicCommentBean().getPost();
            if (temp==null)return;
        }
        Integer mediaType=temp.getMsgType();
        if (mediaType==null)return;
        PostDataBean bean=gson.fromJson(temp.getContent(),PostDataBean.class);
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
