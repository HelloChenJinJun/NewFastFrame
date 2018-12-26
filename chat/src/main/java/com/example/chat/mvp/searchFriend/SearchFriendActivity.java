package com.example.chat.mvp.searchFriend;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.adapter.SearchFriendAdapter;
import com.example.chat.base.ChatBaseActivity;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.UserInfoTask.UserInfoActivity;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.decoration.ListViewDecoration;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemChildClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.bean.chat.User;
import com.example.commonlibrary.customview.ToolBarOption;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/25      12:18
 * QQ:             1981367757
 */
public class SearchFriendActivity extends ChatBaseActivity implements View.OnClickListener {
    private EditText input;
    //        private SearchFriendAdapter adapter;
    private SuperRecyclerView display;
    private TextView search;
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
        input = findViewById(R.id.et_search_friend_input);
        search = findViewById(R.id.tv_activity_search_friend_query);
        display = findViewById(R.id.srcv_search_friend_display);
        display.setLayoutManager(new WrappedLinearLayoutManager(this));
        display.addItemDecoration(new ListViewDecoration());
        search.setOnClickListener(this);

    }


    @Override
    public void initData() {
        mAdapter = new SearchFriendAdapter();
        mAdapter.setOnItemClickListener(new OnSimpleItemChildClickListener() {
            @Override
            public void onItemChildClick(int position, View view, int id) {
                View itemView = display.getLayoutManager().findViewByPosition(position);
                View avatar = itemView.findViewById(R.id.riv_search_friend_item_avatar);
                View name = itemView.findViewById(R.id.tv_search_friend_item_name);
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(SearchFriendActivity.this
                        , Pair.create(avatar, "avatar"), Pair.create(name, "name"));
                UserInfoActivity.start(SearchFriendActivity.this, mAdapter.getData(position).getObjectId(), activityOptionsCompat);
                ActivityCompat.finishAfterTransition(SearchFriendActivity.this);
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
            ToastUtils.showShortToast("搜索内容不能为空!");
            return;
        }
        addSubscription(UserManager.getInstance().queryUsers(input.getText().toString().trim(), new FindListener<User>() {
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
                        } else {
                            LogUtil.e("查询出错!" + e.toString());
                        }
                    }
                }
        ));
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_activity_search_friend_query) {
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
