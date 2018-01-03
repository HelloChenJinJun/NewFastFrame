package com.example.chat.adapter;

import android.icu.text.StringPrepParseException;
import android.util.SparseArray;

import com.example.chat.R;
import com.example.chat.bean.post.CommentListDetailBean;
import com.example.chat.util.TimeUtil;
import com.example.commonlibrary.baseadapter.adapter.BaseMultipleRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/3     15:47
 * QQ:         1981367757
 */

public class CommentDetailAdapter extends BaseMultipleRecyclerAdapter<CommentListDetailBean,BaseWrappedViewHolder>{
    private static final Long DELTA_TIME=5*60*1000L;


    @Override
    protected SparseArray<Integer> getLayoutIdMap() {
        SparseArray<Integer> sparseArray=new SparseArray<>();
        sparseArray.put(CommentListDetailBean.TYPE_RIGHT, R.layout.item_activity_comment_list_detail_right);
        sparseArray.put(CommentListDetailBean.TYPE_LEFT,R.layout.item_activity_comment_list_detail_left);
        return sparseArray;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, CommentListDetailBean data) {
        if (holder.getItemViewType() == CommentListDetailBean.TYPE_LEFT) {
            holder.setImageUrl(R.id.riv_comment_detail_left_avatar,data.getAvatar())
                    .setText(R.id.tv_comment_detail_left_name,data.getName())
                    .setText(R.id.tv_comment_detail_left_content,data.getContent())
                    .setOnItemChildClickListener(R.id.riv_comment_detail_left_avatar);
        }else {
            holder.setImageUrl(R.id.riv_comment_detail_right_avatar,data.getAvatar())
                    .setText(R.id.tv_comment_detail_right_name,data.getName())
                    .setText(R.id.tv_comment_detail_right_content,data.getContent())
                    .setOnItemChildClickListener(R.id.riv_comment_detail_right_avatar);
            if (isNeedShowTime(holder.getAdapterPosition()-getItemCount(),data)) {
                holder.setVisible(R.id.tv_comment_detail_right_time,true)
                        .setText(R.id.tv_comment_detail_right_time, TimeUtil.getTime(data.getTime()));
            }else {
                holder.setVisible(R.id.tv_comment_detail_right_time,false);
            }
        }

    }

    private boolean isNeedShowTime(int position,CommentListDetailBean bean) {
        CommentListDetailBean lastData=data.get(position);
        if (lastData == null) {
            return true;
        }else {
            return bean.getTime() - lastData.getTime() >= DELTA_TIME;
        }
    }
}
