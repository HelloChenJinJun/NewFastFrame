package com.example.chat.mvp.main.friends;

import android.app.Activity;
import android.content.Intent;

import com.rationalTiger.commonlibrary.cusotomview.ToolBarOption;
import com.rationalTiger.poster.R;
import com.rationalTiger.poster.base.MainBaseActivity;
import com.rationalTiger.poster.mvp.searchFriend.SearchFriendActivity;

/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/24     12:44
 * QQ:         1981367757
 */

public class FriendsActivity extends MainBaseActivity {
    @Override
    public void updateData(Object o) {

    }

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
        return R.layout.activity_friends;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        addOrReplaceFragment(FriendsFragment.newInstance(), R.id.fl_activity_friends_container);
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("通讯录");
        toolBarOption.setNeedNavigation(true);
        toolBarOption.setRightResId(R.drawable.ic_person_add_white_24dp);
        toolBarOption.setRightListener(view -> SearchFriendActivity.start(FriendsActivity.this));
        setToolBar(toolBarOption);
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, FriendsActivity.class);
        activity.startActivity(intent);
    }
}
