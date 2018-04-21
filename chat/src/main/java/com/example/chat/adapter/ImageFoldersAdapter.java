package com.example.chat.adapter;


import com.example.chat.R;
import com.example.chat.bean.ImageFolder;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;

import java.util.List;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/23      23:18
 * QQ:             1981367757
 */

public class ImageFoldersAdapter extends BaseRecyclerAdapter<ImageFolder, BaseWrappedViewHolder> {

        private int currentSelectedPosition;


        public int getCurrentSelectedPosition() {
                return currentSelectedPosition;
        }

        public void setCurrentSelectedPosition(int currentSelectedPosition) {
                this.currentSelectedPosition = currentSelectedPosition;
        }



        @Override
        protected int getLayoutId() {
                return R.layout.image_folder_item;
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, ImageFolder data) {
                if (getCurrentSelectedPosition() == holder.getAdapterPosition()) {
                        holder.setVisible(R.id.iv_image_folder_select, true);
                } else {
                        holder.setVisible(R.id.iv_image_folder_select, false);
                }
                holder.setText(R.id.tv_image_folder_name, data.getName())
                        .setImageUrl(R.id.iv_image_folder_cover, data.getDisplay().getPath())
                        .setText(R.id.tv_image_folder_count, data.getAllImages().size() + "")
                .setOnItemClickListener();

        }
}
