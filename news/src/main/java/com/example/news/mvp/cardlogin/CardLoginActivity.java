package com.example.news.mvp.cardlogin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.news.mvp.cardinfo.CardInfoActivity;
import com.example.news.NewsApplication;
import com.example.news.R;
import com.example.news.dagger.cardlogin.CardLoginModule;
import com.example.news.dagger.cardlogin.DaggerCardLoginComponent;
import com.example.news.util.NewsUtil;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/19      19:40
 * QQ:             1981367757
 */

public class CardLoginActivity extends BaseActivity<Object, CardLoginPresenter> implements View.OnClickListener {


    private EditText account, password, verify;
    private ImageView verifyImage;
    private Button login;

    @Override
    public void updateData(Object o) {
        if (o == null) {
//            保存密码
            BaseApplication.getAppComponent().getSharedPreferences()
                    .edit().putString(NewsUtil.PW, NewsUtil.getEncodePassWord(password.getText().toString().trim()))
                    .apply();
            CardInfoActivity.start(this);
            finish();
        } else if (o instanceof Bitmap) {
            verifyImage.setImageBitmap((Bitmap) o);
        } else {
            presenter.getVerifyImage();
        }
    }

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
        return R.layout.activity_card_login;
    }

    @Override
    protected void initView() {
        account = (EditText) findViewById(R.id.et_activity_card_login_account);
        login = (Button) findViewById(R.id.btn_activity_card_login);
        password = (EditText) findViewById(R.id.et_activity_card_login_pw);
        verify = (EditText) findViewById(R.id.et_activity_card_login_verify);
        verifyImage = (ImageView) findViewById(R.id.iv_activity_card_login_verify);
    }

    @Override
    protected void initData() {
        DaggerCardLoginComponent.builder().cardLoginModule(new CardLoginModule(this))
                .newsComponent(NewsApplication.getNewsComponent())
                .build().inject(this);
        login.setOnClickListener(this);
        verifyImage.setOnClickListener(this);
        if (BaseApplication.getAppComponent().getSharedPreferences().getString(NewsUtil.CARD_LOGIN_COOKIE, null) == null) {
            login.post(new Runnable() {
                @Override
                public void run() {
                    presenter.getCookie();
                }
            });
        } else {
            presenter.getVerifyImage();
        }
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setNeedNavigation(true);
        toolBarOption.setTitle("个人图书馆");
        setToolBar(toolBarOption);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (getIntent().getStringExtra(NewsUtil.ERROR_INFO) != null) {
                    ToastUtils.showShortToast(getIntent().getStringExtra(NewsUtil.ERROR_INFO));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_activity_card_login) {
            if (TextUtils.isEmpty(account.getText().toString().trim())
                    || TextUtils.isEmpty(password.getText().toString()
                    .trim()) || TextUtils.isEmpty(verify.getText().toString().trim())) {
                ToastUtils.showShortToast("内容不能为空");
            } else {
                presenter.login(account.getText().toString().trim()
                        , password.getText().toString().trim()
                        , verify.getText().toString().trim());
            }
        } else {
            presenter.getVerifyImage();
        }
    }


    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        if (listener == null) {
            ToastUtils.showShortToast(errorMsg);
        } else if (errorMsg != null && errorMsg.equals("登录失败")) {
            ToastUtils.showShortToast(errorMsg);
        } else {
            super.showError(errorMsg, listener);
        }
    }

    public static void start(Context context, String info) {
        Intent intent = new Intent(context, CardLoginActivity.class);
        if (info != null) {
            intent.putExtra(NewsUtil.ERROR_INFO, info);
        }
        context.startActivity(intent);
    }
}
