package com.example.chat.adapter;

import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.bean.WinXinBean;
import com.example.chat.db.ChatDB;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/6      23:24
 * QQ:             1981367757
 */
public class WeiXinAdapter extends BaseRecyclerAdapter<WinXinBean, BaseWrappedViewHolder> {


        @Override
        protected int getLayoutId() {
                return R.layout.win_xin_fragment_item_layout;
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, WinXinBean data) {
                holder.setImageUrl(R.id.iv_win_xin_fragment_item_picture, data.getPicUrl())
                        .setText(R.id.tv_wei_xin_fragment_layout_title, data.getTitle())
                        .setText(R.id.tv_wei_xin_fragment_item_description, data.getDescription())
                        .setText(R.id.tv_wei_xin_fragment_item_time, data.getCtime())
                        .setOnItemChildClickListener(R.id.btn_wei_xin_fragment_right)
                .setOnItemClickListener();
                if (ChatDB.create().getWeixinInfoReadStatus(data.getUrl()) == 1) {
                        ((TextView) holder.getView(R.id.tv_wei_xin_fragment_layout_title)).setTextColor(holder.getContext().getResources().getColor(R.color.base_color_text_grey));
                } else {
                        ((TextView) holder.getView(R.id.tv_wei_xin_fragment_layout_title)).setTextColor(holder.getContext().getResources().getColor(R.color.base_color_text_black));
                }
        }
}
