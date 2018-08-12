package com.example.chat.adapter;

import android.util.SparseArray;
import android.util.SparseIntArray;

import com.example.chat.R;
import com.example.chat.bean.post.ReplyDetailContent;
import com.example.chat.manager.UserDBManager;
import com.example.chat.util.FaceTextUtil;
import com.example.chat.util.TimeUtil;
import com.example.commonlibrary.baseadapter.adapter.BaseMultipleRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.bean.chat.UserEntity;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/3     15:47
 * QQ:         1981367757
 */

public class CommentDetailAdapter extends BaseMultipleRecyclerAdapter<ReplyDetailContent,BaseWrappedViewHolder>{
    private static final Long DELTA_TIME=5*60*1000L;


    @Override
    protected SparseIntArray getLayoutIdMap() {
        SparseIntArray sparseArray=new SparseIntArray();
        sparseArray.put(ReplyDetailContent.TYPE_RIGHT, R.layout.item_activity_comment_list_detail_right);
        sparseArray.put(ReplyDetailContent.TYPE_LEFT,R.layout.item_activity_comment_list_detail_left);
        return sparseArray;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, ReplyDetailContent data) {
        UserEntity userEntity= UserDBManager.getInstance().getUser(data.getUid());
        if (holder.getItemViewType() == ReplyDetailContent.TYPE_LEFT) {
            holder.setImageUrl(R.id.riv_comment_detail_left_avatar,userEntity.getAvatar())
                    .setText(R.id.tv_comment_detail_left_name,userEntity.getName())
                    .setText(R.id.tv_comment_detail_left_content, FaceTextUtil
                    .toSpannableString(holder.getContext(),data.getContent()))
                    .setOnItemChildClickListener(R.id.riv_comment_detail_left_avatar);
        }else {
            holder.setImageUrl(R.id.riv_comment_detail_right_avatar,userEntity.getAvatar())
                    .setText(R.id.tv_comment_detail_right_name,userEntity.getName())
                    .setText(R.id.tv_comment_detail_right_content,data.getContent())
                    .setOnItemChildClickListener(R.id.riv_comment_detail_right_avatar);
            if (isNeedShowTime(holder.getAdapterPosition()-getItemUpCount(),data)) {
                holder.setVisible(R.id.tv_comment_detail_right_time,true)
                        .setText(R.id.tv_comment_detail_right_time, TimeUtil.getTime(data.getTime()));
            }else {
                holder.setVisible(R.id.tv_comment_detail_right_time,false);
            }
        }
    }

    private boolean isNeedShowTime(int position,ReplyDetailContent bean) {
        ReplyDetailContent lastData=data.get(position);
        if (lastData == null) {
            return true;
        }else {
            return bean.getTime() - lastData.getTime() >= DELTA_TIME;
        }
    }
}
