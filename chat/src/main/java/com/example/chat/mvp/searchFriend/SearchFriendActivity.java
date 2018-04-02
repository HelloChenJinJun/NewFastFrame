package com.example.chat.mvp.searchFriend;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chat.R;
import com.example.chat.adapter.SearchFriendAdapter;
import com.example.chat.base.SlideBaseActivity;
import com.example.chat.bean.User;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.UserInfoTask.UserInfoActivity;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemChildClickListener;
import com.example.commonlibrary.cusotomview.ListViewDecoration;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/25      12:18
 * QQ:             1981367757
 */
public class SearchFriendActivity extends SlideBaseActivity implements View.OnClickListener {
        private EditText input;
        //        private SearchFriendAdapter adapter;
        private RecyclerView display;
        private Button search;
        private SearchFriendAdapter mAdapter;



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
                return R.layout.activity_search_friend;
        }

        @Override
        public void initView() {
                input = (EditText) findViewById(R.id.et_search_friend_input);
                search = (Button) findViewById(R.id.btn_search_friend);
                display = (RecyclerView) findViewById(R.id.swrc_search_friend_display);
                display.setLayoutManager(new LinearLayoutManager(this));
                display.setItemAnimator(new DefaultItemAnimator());
                display.addItemDecoration(new ListViewDecoration(this));
                search.setOnClickListener(this);

        }


        @Override
        public void initData() {
                mAdapter = new SearchFriendAdapter();
                mAdapter.setOnItemClickListener(new OnSimpleItemChildClickListener() {
                        @Override
                        public void onItemChildClick(int position, View view, int id) {
                                UserInfoActivity.start(SearchFriendActivity.this,mAdapter.getData(position).getObjectId());
                                finish();
                        }
                });
                display.setAdapter(mAdapter);
                initActionBar();
        }

        private void initActionBar() {
                ToolBarOption toolBarOption = new ToolBarOption();
                toolBarOption.setAvatar(null);
                toolBarOption.setNeedNavigation(true);
                toolBarOption.setTitle("查找好友");
                setToolBar(toolBarOption);
        }

        private void searchUsers() {
                showLoadDialog("正在搜索，请稍候.......");
                if (TextUtils.isEmpty(input.getText().toString().trim())) {
                        dismissLoadDialog();
                        ToastUtils.showShortToast("请输入用户名进行查询!");
                        return;
                }
                UserManager.getInstance().queryUsers(input.getText().toString().trim(), new FindListener<User>() {
                                @Override
                                public void done(List<User> list, BmobException e) {
                                        dismissLoadDialog();
                                        if (e == null) {
                                                if (list != null && list.size() > 0) {
                                                        LogUtil.e("查询用户成功：个数为" + list.size());
                                                        LogUtil.e(list.size() + "大小");
                                                        mAdapter.clearAllData();
                                                        mAdapter.addData(list);
//                                                adapter.setData(list);
                                                }
                                        }else {
                                                LogUtil.e("查询出错!" + e.toString());
                                        }
                                }
                        }
                );
        }

        @Override
        public void onClick(View v) {
                int i = v.getId();
                if (i == R.id.btn_search_friend) {
                        searchUsers();

                }
        }


        public static void start(Activity activity) {
                Intent intent = new Intent(activity, SearchFriendActivity.class);
                activity.startActivity(intent);
        }

        @Override
        public void updateData(Object o) {

        }
}
