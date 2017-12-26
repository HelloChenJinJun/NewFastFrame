package com.example.news.mvp.systeminfo;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.router.Router;
import com.example.commonlibrary.router.RouterRequest;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.news.NewsApplication;
import com.example.news.R;
import com.example.news.bean.SystemUserBean;
import com.example.news.dagger.systeminfo.DaggerSystemInfoComponent;
import com.example.news.dagger.systeminfo.SystemInfoModule;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/17     14:45
 * QQ:         1981367757
 */

public class SystemInfoLoginActivity extends BaseActivity<Object, SystemInfoLoginPresenter> implements View.OnClickListener {
    private EditText account, pw;


    @Override
    public void updateData(Object o) {
        if (o != null && o instanceof SystemUserBean) {
            SystemUserBean bean = (SystemUserBean) o;
            Map<String, Object> map = new HashMap<>();
            map.put(ConstantUtil.FROM, "news");
            map.put(ConstantUtil.ACCOUNT, account.getText().toString().trim());
            map.put(ConstantUtil.PASSWORD, pw.getText().toString().trim());
            map.put(ConstantUtil.AVATAR, bean.getSCHOOL_PIC());
            map.put(ConstantUtil.NICK, bean.getUSER_NAME());
            map.put(ConstantUtil.NAME, bean.getUSER_NAME());
            map.put(ConstantUtil.SEX, bean.getUSER_SEX().equals("男") ? Boolean.TRUE : Boolean.FALSE);
            map.put(ConstantUtil.STUDENT_TYPE, bean.getID_TYPE());
            map.put(ConstantUtil.COLLEGE, bean.getUNIT_NAME());
            Router.getInstance().deal(new RouterRequest.Builder().provideName("chat")
                    .actionName("login").paramMap(map).isFinish(true).context(this).build());
        }
    }

    @Override
    protected boolean isNeedHeadLayout() {
        return false;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return true;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_system_info_login;
    }

    @Override
    protected void initView() {
        account = (EditText) findViewById(R.id.et_activity_system_info_login_account);
        pw = (EditText) findViewById(R.id.et_activity_system_info_login_pw);
        View confirm = findViewById(R.id.btn_activity_system_info_login_confirm);
        confirm.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        DaggerSystemInfoComponent.builder().newsComponent(NewsApplication
                .getNewsComponent()).systemInfoModule(new SystemInfoModule(this))
                .build().inject(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_activity_system_info_login_confirm) {
            if (TextUtils.isEmpty(account.getText().toString().trim())
                    || TextUtils.isEmpty(pw.getText().toString().trim())) {
                ToastUtils.showShortToast("密码或账号不能为空");
            } else {
                presenter.login(account.getText().toString().trim(), pw.getText()
                        .toString().trim());
            }
        }
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SystemInfoLoginActivity.class);
        activity.startActivity(intent);
    }
}
