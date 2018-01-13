package com.example.chat.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.chat.R;
import com.example.chat.mvp.account.password.PasswordChangePresenter;
import com.example.chat.view.AutoEditText;
import com.example.commonlibrary.cusotomview.ToolBarOption;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/13     23:44
 * QQ:         1981367757
 */

public class PasswordChangeActivity extends SlideBaseActivity<Object,PasswordChangePresenter> implements View.OnClickListener {

    private AutoEditText old,news,confirm;
    private Button commit;


    public static void start(Activity activity) {
        Intent intent=new Intent(activity,PasswordChangeActivity.class);
        activity.startActivity(intent);
    }

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
        return R.layout.activity_password_change;
    }

    @Override
    protected void initView() {
        old= (AutoEditText) findViewById(R.id.aet_activity_password_change_old);
        news= (AutoEditText) findViewById(R.id.aet_activity_password_change_new);
        commit= (Button) findViewById(R.id.btn_activity_password_change_commit);
        confirm= (AutoEditText) findViewById(R.id.aet_activity_password_change_confirm);
        commit.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        ToolBarOption toolBarOption=new ToolBarOption();
        toolBarOption.setTitle("修改密码");
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
    }

    @Override
    public void onClick(View view) {
        presenter.resetPassword(old.getText().toString().trim(),news.getText().toString().trim());
    }
}
