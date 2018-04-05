package com.example.chat.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.base.Constant;
import com.example.chat.bean.User;
import com.example.chat.bean.post.CommentDetailBean;
import com.example.chat.bean.post.PublicCommentBean;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.UserDetail.UserDetailActivity;
import com.example.chat.util.FaceTextUtil;
import com.example.chat.util.TimeUtil;
import com.example.chat.view.CustomMoveMethod;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.bean.chat.UserEntity;
import com.example.commonlibrary.utils.ToastUtils;
import com.google.gson.Gson;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/2     23:52
 * QQ:         1981367757
 */

public class CommentListAdapter extends BaseRecyclerAdapter<PublicCommentBean, BaseWrappedViewHolder> {
    private Gson gson = BaseApplication.getAppComponent().getGson();


    @Override
    protected int getLayoutId() {
        return R.layout.item_activity_comment_list;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, PublicCommentBean data) {
        CommentDetailBean detailBean = gson.fromJson(data.getContent(), CommentDetailBean.class);
        holder.setImageUrl(R.id.riv_item_activity_comment_list_avatar, data
                .getUser().getAvatar())
                .setText(R.id.tv_item_activity_comment_list_name, data.getUser()
                        .getName())
                .setText(R.id.tv_item_activity_comment_list_time, getText(data))
                .setText(R.id.tv_item_activity_comment_list_content, FaceTextUtil.toSpannableString(holder.getContext(),detailBean.getContent()))
                .setOnItemChildClickListener(R.id.riv_item_activity_comment_list_avatar)
                .setOnItemChildClickListener(R.id.iv_item_activity_comment_list_comment)
                .setImageResource(R.id.iv_item_activity_comment_list_sex, data.getUser()
                        .isSex() ? R.drawable.ic_sex_female : R.drawable.ic_sex_male)
                .setOnItemClickListener();

        if (data.getSendStatus().equals(Constant.SEND_STATUS_FAILED)) {
            holder.setVisible(R.id.iv_item_activity_comment_list_retry, true)
                    .setVisible(R.id.pb_item_activity_comment_list_loading, false);
        } else if (data.getSendStatus().equals(Constant.SEND_STATUS_SENDING)) {
            holder.setVisible(R.id.iv_item_activity_comment_list_retry, false)
                    .setVisible(R.id.pb_item_activity_comment_list_loading, true);
        } else if (data.getSendStatus().equals(Constant.SEND_STATUS_SUCCESS)) {
            holder.setVisible(R.id.iv_item_activity_comment_list_retry, false)
                    .setVisible(R.id.pb_item_activity_comment_list_loading, false);
        }
        if (detailBean.getPublicId() != null) {
            String[] uidList = detailBean.getPublicId().split("&");
            String replyUid;
            if (uidList[0].equals(data.getUser().getObjectId())) {
                replyUid = uidList[1];
            } else {
                replyUid = uidList[0];
            }
            holder.itemView.setTag(replyUid);
            UserManager.getInstance().findUserById(replyUid, new FindListener<User>() {
                @Override
                public void done(List<User> list, BmobException e) {
                    if (e == null
                            && list != null && list.size() > 0) {
                        if (holder.itemView.getTag() != null
                                && holder.itemView.getTag().equals(list.get(0).getObjectId())) {
                            holder.setText(R.id.tv_item_activity_comment_list_reply, getReplyContent(list.get(0),
                                    FaceTextUtil.toSpannableString(holder.getContext()
                                    ,detailBean.getReplyContent())));
                        }
                    } else if (e!=null) {
                        ToastUtils.showShortToast("加载用户信息失败"+e.toString());
                    }
                }
            });
            ((TextView) holder.getView(R.id.tv_item_activity_comment_list_reply))
                    .setMovementMethod(new CustomMoveMethod());
            holder.setVisible(R.id.rl_item_activity_comment_list_reply_container, true)
                    .setOnItemChildClickListener(R.id.tv_item_activity_comment_list_look);
        } else {
            holder.setVisible(R.id.rl_item_activity_comment_list_reply_container, false);
        }
    }

    private SpannableStringBuilder getReplyContent(User user, CharSequence content) {

        String name = user.getName();
        SpannableString spannableString = new SpannableString(name);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#232121")), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                UserDetailActivity.start(((Activity) widget.getContext()), user.getObjectId());
            }
        }, 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append("回复@").append(spannableString)
                .append("的评论：").append(content);
        return spannableStringBuilder;


    }

    private CharSequence getText(PublicCommentBean data) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TimeUtil.getRealTime(data.getCreatedAt()))
                .append("  来自[")
                .append(data.getUser().getCollege())
                .append("]");
        return stringBuilder.toString();
    }
}
