package com.example.chat.adapter;

import com.example.chat.R;
import com.example.chat.bean.FaceText;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/29     10:35
 * QQ:         1981367757
 */

public class EmotionViewAdapter extends BaseRecyclerAdapter<FaceText,BaseWrappedViewHolder>{
    @Override
    protected int getLayoutId() {
        return R.layout.emtion_item;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, FaceText data) {
        holder.setImageDrawable(R.id.iv_emotion_item_display
        ,holder.itemView.getContext().getResources().getDrawable(holder.itemView.getContext().getResources().getIdentifier(data.getText().substring(1), "mipmap",holder.itemView.getContext().getPackageName())))
                .setOnItemClickListener();
    }
}
