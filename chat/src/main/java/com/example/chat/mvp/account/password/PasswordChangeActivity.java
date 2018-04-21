package com.example.chat.mvp.account.password;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.example.chat.ChatApplication;
import com.example.chat.R;
import com.example.chat.base.SlideBaseActivity;
import com.example.chat.dagger.account.password.DaggerPasswordChangeComponent;
import com.example.chat.dagger.account.password.PasswordChangeModule;
import com.example.chat.util.CommonUtils;
import com.example.chat.view.AutoEditText;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.rxbus.event.PwChangeEvent;
import com.example.commonlibrary.utils.AppUtil;
import com.example.commonlibrary.utils.ToastUtils;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/13     23:44
 * QQ:         1981367757
 */

public class PasswordChangeActivity extends SlideBaseActivity<Object,PasswordChangePresenter> implements View.OnClickListener {

    private AutoEditText old,news,confirm;


    public static void start(Activity activity) {
        Intent intent=new Intent(activity,PasswordChangeActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void updateData(Object o) {
        if (o != null && o instanceof PwChangeEvent) {
            ToastUtils.showShortToast("最终密码修改成功");
            finish();
        }
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
        Button commit = (Button) findViewById(R.id.btn_activity_password_change_commit);
        confirm= (AutoEditText) findViewById(R.id.aet_activity_password_change_confirm);
        commit.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        DaggerPasswordChangeComponent.builder().chatMainComponent(ChatApplication.getChatMainComponent())
                .passwordChangeModule(new PasswordChangeModule(this)).build().inject(this);
        ToolBarOption toolBarOption=new ToolBarOption();
        toolBarOption.setTitle("修改密码");
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
    }







    @Override
    public void onClick(View view) {
        if (TextUtils.isEmpty(old.getText().toString().trim())) {
            ToastUtils.showShortToast("原密码不能为空");
            old.startShakeAnimation();
            return;
        }
        if (TextUtils.isEmpty(news.getText())) {
            ToastUtils.showShortToast(getString(R.string.password_null));
            return;
        }
        if (TextUtils.isEmpty(confirm.getText())) {
            ToastUtils.showShortToast(getString(R.string.password_null));
            confirm.startShakeAnimation();
            return;
        }
        if (!news.getText().toString().trim().equals(confirm.getText().toString().trim())) {
            ToastUtils.showShortToast(getString(R.string.register_password_error));
            return;
        }
        if (!AppUtil.isNetworkAvailable()) {
            ToastUtils.showShortToast(getString(R.string.network_tip));
            return;
        }
        presenter.resetPassword(old.getText().toString().trim(),news.getText().toString().trim());
    }
}
