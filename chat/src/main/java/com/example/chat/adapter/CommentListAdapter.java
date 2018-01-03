package com.example.chat.adapter;

import com.example.chat.R;
import com.example.chat.bean.User;
import com.example.chat.bean.post.CommentDetailBean;
import com.example.chat.bean.post.PublicCommentBean;
import com.example.chat.util.TimeUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.google.gson.Gson;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/2     23:52
 * QQ:         1981367757
 */

public class CommentListAdapter extends BaseRecyclerAdapter<PublicCommentBean,BaseWrappedViewHolder>{
    private Gson gson=BaseApplication.getAppComponent().getGson();




    @Override
    protected int getLayoutId() {
        return R.layout.item_activity_comment_list;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, PublicCommentBean data) {
        CommentDetailBean detailBean=gson.fromJson(data.getContent(),CommentDetailBean.class);
        holder.setImageUrl(R.id.riv_item_activity_comment_list_avatar,data
        .getUser().getAvatar())
                .setText(R.id.tv_item_activity_comment_list_name,data.getUser()
                .getNick())
                .setText(R.id.tv_item_activity_comment_list_time,getText(data.getUser()))
                .setText(R.id.tv_item_activity_comment_list_content,detailBean.getContent())
        .setOnItemChildClickListener(R.id.riv_item_activity_comment_list_avatar)
        .setOnItemChildClickListener(R.id.iv_item_activity_comment_list_comment)
        .setOnItemClickListener();
        if (detailBean.getReplyContent() != null) {
            holder.setVisible(R.id.rl_item_activity_comment_list_reply_container,true)
            .setText(R.id.tv_item_activity_comment_list_reply,detailBean.getReplyContent())
            .setOnItemChildClickListener(R.id.tv_item_activity_comment_list_look);
        }else {
            holder.setVisible(R.id.rl_item_activity_comment_list_reply_container,false);
        }
    }

    private CharSequence getText(User user) {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(TimeUtil.getRealTime(user.getCreatedAt()))
                .append("  来自[")
                .append(user.getAddress())
                .append("]");
        return stringBuilder.toString();
    }
}
