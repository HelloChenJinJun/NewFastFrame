package com.example.chat.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.chat.R;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/13     23:07
 * QQ:         1981367757
 */

public class AccountManageActivity extends SlideBaseActivity implements View.OnClickListener {
    private RelativeLayout accountProtect,changePw;
    @Override
    public void updateData(Object o) {

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
    protected int getContentLayout() {
        return R.layout.activity_account_manage;
    }

    @Override
    protected void initView() {
        accountProtect= (RelativeLayout) findViewById(R.id.rl_activity_settings_account_manage);
        changePw= (RelativeLayout) findViewById(R.id.rl_activity_settings_change_pw);
        accountProtect.setOnClickListener(this);
        changePw.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        int id= view.getId();
        if (id == R.id.rl_activity_settings_change_pw) {
                PasswordChangeActivity.start(this);
        } else if (id == R.id.rl_activity_settings_account_manage) {

        }
    }

    public static void start(Activity activity) {
    Intent intent=new Intent(activity,AccountManageActivity.class);
    activity.startActivity(intent);
    }
}
