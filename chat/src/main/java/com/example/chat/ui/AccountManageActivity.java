package com.example.chat.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.chat.R;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.rxbus.event.PwChangeEvent;
import com.example.commonlibrary.utils.ToastUtils;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/13     23:07
 * QQ:         1981367757
 */

public class AccountManageActivity extends SlideBaseActivity implements View.OnClickListener {
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
        return R.layout.activity_account_manage;
    }

    @Override
    protected void initView() {
        RelativeLayout accountProtect = (RelativeLayout) findViewById(R.id.rl_activity_account_manage_protect);
        RelativeLayout changePw = (RelativeLayout) findViewById(R.id.rl_activity_account_manage_pw_change);
        accountProtect.setOnClickListener(this);
        changePw.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setNeedNavigation(true);
        toolBarOption.setTitle("账号管理");
        setToolBar(toolBarOption);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.rl_activity_account_manage_pw_change) {
            PasswordChangeActivity.start(this);
        } else if (id == R.id.rl_activity_account_manage_protect) {

        }
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, AccountManageActivity.class);
        activity.startActivity(intent);
    }
}
