package com.example.chat.adapter;


import com.example.chat.R;
import com.example.chat.bean.User;
import com.example.chat.util.CommonUtils;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;

import java.util.List;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/11      12:48
 * QQ:             1981367757
 *
 * 附近人列表适配器
 */
public class NearbyPeopleAdapter extends BaseRecyclerAdapter<User, BaseWrappedViewHolder> {


        @Override
        protected int getLayoutId() {
                return R.layout.nearby_people_item_layout;
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, User data) {
                holder.setImageUrl(R.id.riv_nearby_people_avatar, data.getAvatar())
                        .setText(R.id.tv_nearby_people_nick,data.getName())
                        .setText(R.id.tv_nearby_people_distance, "距离" + CommonUtils.getDistance(data.getLocation().getLongitude(), data.getLocation().getLatitude()) + "米")
                .setOnItemClickListener();
                if (data.getSignature() != null) {
                        holder.setVisible(R.id.tv_nearby_people_signature, true)
                                .setText(R.id.tv_nearby_people_signature, data.getSignature());
                } else {
                        holder.setVisible(R.id.tv_nearby_people_signature, false);
                }
        }
}
