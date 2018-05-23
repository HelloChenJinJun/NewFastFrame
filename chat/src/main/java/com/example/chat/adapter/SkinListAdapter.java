package com.example.chat.adapter;

import com.example.chat.R;
import com.example.chat.bean.SkinBean;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.bean.chat.SkinEntity;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/5/22     23:52
 */

public class SkinListAdapter extends BaseRecyclerAdapter<SkinEntity,BaseWrappedViewHolder>{
    @Override
    protected int getLayoutId() {
        return R.layout.item_activity_skin_list;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, SkinEntity data) {
        holder.setText(R.id.tv_item_activity_skin_list_title,data.getTitle())
                .setImageUrl(R.id.iv_item_activity_skin_list_image,data.getImageList().get(0))
                .setOnItemClickListener();
        if (data.isHasSelected()) {
            holder.setVisible(R.id.cb_item_activity_skin_list_check,true);
        }else {
            holder.setVisible(R.id.cb_item_activity_skin_list_check,false);
        }
    }
}
