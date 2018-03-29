package com.example.chat.mvp.blackList;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.chat.R;
import com.example.chat.adapter.BlackAdapter;
import com.example.chat.base.SlideBaseActivity;
import com.example.chat.bean.User;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.UserInfoTask.UserInfoActivity;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.cusotomview.ListViewDecoration;
import com.example.commonlibrary.cusotomview.ToolBarOption;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/10      12:52
 * QQ:             1981367757
 */

public class BlackListActivity extends SlideBaseActivity {
        private RecyclerView display;
        private BlackAdapter mBlackAdapter;



        @Override
        protected boolean isNeedHeadLayout() {
                return true;
        }

        @Override
        protected boolean isNeedEmptyLayout() {
                return true;
        }

        @Override
        protected int getContentLayout() {
                return R.layout.activity_black_list;
        }


        @Override
        public void initView() {
                display = (RecyclerView) findViewById(R.id.rcv_black_list_display);
        }


        @Override
        public void initData() {
                display.setLayoutManager(new LinearLayoutManager(this));
                display.setHasFixedSize(true);
                display.setItemAnimator(new DefaultItemAnimator());
                display.addItemDecoration(new ListViewDecoration(this));
                mBlackAdapter = new BlackAdapter();
                mBlackAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
                        @Override
                        public void onItemClick(int position, View view) {
                                User user = mBlackAdapter.getData(position);
                                Intent intent = new Intent(BlackListActivity.this, UserInfoActivity.class);
                                intent.putExtra("uid", user.getObjectId());
                                startActivity(intent);
                        }
                });
                display.setAdapter(mBlackAdapter);
                mBlackAdapter.addData(UserCacheManager.getInstance().getAllBlackUser());
                initActionBar();
        }

        private void initActionBar() {
                ToolBarOption toolBarOption = new ToolBarOption();
                toolBarOption.setNeedNavigation(true);
                toolBarOption.setAvatar(UserManager.getInstance().getCurrentUser().getAvatar());
                toolBarOption.setTitle("黑名单列表");
                setToolBar(toolBarOption);
        }

        public static void start(Activity activity) {
                Intent intent = new Intent(activity, BlackListActivity.class);
                activity.startActivity(intent);
        }

        @Override
        public void updateData(Object o) {

        }
}
