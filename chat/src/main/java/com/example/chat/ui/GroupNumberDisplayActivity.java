package com.example.chat.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chat.R;
import com.example.chat.bean.GroupNumberInfo;
import com.example.chat.manager.MessageCacheManager;
import com.example.commonlibrary.cusotomview.RoundAngleImageView;
import com.example.commonlibrary.cusotomview.ToolBarOption;

import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/2      11:03
 * QQ:             1981367757
 */
public class GroupNumberDisplayActivity extends SlideBaseActivity implements AdapterView.OnItemClickListener {
        private GridView display;
        private GroupNumberAdapter mAdapter;
        private String groupId;



        @Override
        protected boolean isNeedHeadLayout() {
                return true;
        }

        @Override
        protected boolean isNeedEmptyLayout() {
                return false;
        }

        @Override
        protected int getContentLayout() {
                return R.layout.activity_group_number_display;
        }


        @Override
        public void initView() {
                display = (GridView) findViewById(R.id.gv_group_number_display);
                display.setOnItemClickListener(this);
        }



        @Override
        public void initData() {
                groupId = getIntent().getStringExtra("groupId");
                mAdapter = new GroupNumberAdapter(MessageCacheManager.getInstance().getAllGroupNumberInfo(groupId));
                display.setAdapter(mAdapter);
                initActionBar();
        }

        private void initActionBar() {
                ToolBarOption toolBarOption = new ToolBarOption();
                toolBarOption.setNeedNavigation(true);
                toolBarOption.setTitle("群成员列表");
                toolBarOption.setAvatar(null);
                setToolBar(toolBarOption);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupNumberInfo groupNumberInfo = (GroupNumberInfo) mAdapter.getItem(position);
                Intent intent = new Intent(this, GroupNumberInfoDetailActivity.class);
                intent.putExtra("isCreator", getIntent().getBooleanExtra("isCreator", false));
                intent.putExtra("groupId", groupId);
                intent.putExtra("groupNumberInfo", groupNumberInfo);
                startActivity(intent);
        }

        @Override
        public void updateData(Object o) {

        }

        private class GroupNumberAdapter extends BaseAdapter {
                private List<GroupNumberInfo> data;

                GroupNumberAdapter(List<GroupNumberInfo> list) {
                        if (list != null) {
                                data = list;
                        } else {
                                data = new ArrayList<>();
                        }
                }

                @Override
                public int getCount() {
                        return data.size();
                }

                @Override
                public Object getItem(int position) {
                        return data.get(position);
                }

                @Override
                public long getItemId(int position) {
                        return position;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                        ViewHolder viewHolder;
                        if (convertView == null) {
                                viewHolder = new ViewHolder();
                                convertView = LayoutInflater.from(GroupNumberDisplayActivity.this).inflate(R.layout.group_info_avatar_item_layout, parent, false);
                                viewHolder.name = (TextView) convertView.findViewById(R.id.tv_group_info_avatar_item_layout_name);
                                viewHolder.avatar = (RoundAngleImageView) convertView.findViewById(R.id.riv_group_info_avatar_item_layout_avatar);
                                convertView.setTag(viewHolder);
                        } else {
                                viewHolder = (ViewHolder) convertView.getTag();
                        }
                        GroupNumberInfo groupNumberInfo = data.get(position);
                        Glide.with(GroupNumberDisplayActivity.this).load(groupNumberInfo.getUser().getAvatar()).error(R.mipmap.default_image).placeholder(R.drawable.location_default).into(viewHolder.avatar);
                        viewHolder.name.setText(groupNumberInfo.getGroupNick());
                        return convertView;
                }

                private class ViewHolder {
                        TextView name;
                        RoundAngleImageView avatar;
                }
        }
}
