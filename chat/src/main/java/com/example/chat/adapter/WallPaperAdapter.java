package com.example.chat.adapter;


import com.example.chat.R;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;



/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/2/26      21:09
 * QQ:             1981367757
 */
public class WallPaperAdapter extends BaseRecyclerAdapter<String, BaseWrappedViewHolder> {


        private int selectedPosition = -1;




        @Override
        protected int getLayoutId() {
                return R.layout.wallpaper_item;
        }


        public void setSelectedPosition(int selectedPosition) {
                this.selectedPosition = selectedPosition;
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, String data) {
                holder.setImageBg(R.id.iv_wallpaper_item_display, data).setOnItemClickListener();
                if (selectedPosition == holder.getAdapterPosition()) {
                        holder.setImageResource(R.id.iv_wallpaper_item_display, R.drawable.change_background_picture_btn);
                }
        }


}
