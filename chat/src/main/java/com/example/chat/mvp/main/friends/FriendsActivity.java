package com.example.chat.mvp.main.friends;

import android.app.Activity;
import android.content.Intent;

import com.example.chat.R;
import com.example.chat.base.ChatBaseActivity;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/21     22:03
 */
public class FriendsActivity extends ChatBaseActivity {
    public static void start(Activity activity) {
        Intent intent=new Intent(activity,FriendsActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected boolean isNeedHeadLayout() {
        return false;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
    }


    @Override
    protected boolean needStatusPadding() {
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

    }

    @Override
    public void updateData(Object o) {

    }
}
