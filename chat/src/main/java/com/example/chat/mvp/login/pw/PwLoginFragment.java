package com.example.chat.mvp.login.pw;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chat.base.ChatApplication;
import com.example.chat.R;
import com.example.chat.base.ConstantUtil;
import com.example.chat.dagger.login.DaggerLoginComponent;
import com.example.chat.dagger.login.LoginModule;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.main.HomeActivity;
import com.example.chat.mvp.register.RegisterActivity;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.customview.CustomEditText;
import com.example.commonlibrary.router.Router;
import com.example.commonlibrary.router.RouterConfig;
import com.example.commonlibrary.router.RouterRequest;
import com.example.commonlibrary.utils.AppUtil;
import com.example.commonlibrary.utils.Constant;
import com.example.commonlibrary.utils.ToastUtils;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/29     16:04
 */
public class PwLoginFragment extends BaseFragment<BaseBean, PwLoginPresenter> implements View.OnClickListener {

    private CustomEditText userName;
    private CustomEditText passWord;
    private ImageView bg;

    public static PwLoginFragment newInstance() {
        return new PwLoginFragment();
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
        return R.layout.fragment_pw_login;
    }

    @Override
    protected void initView() {
        bg = findViewById(R.id.iv_login_bg);
        userName = findViewById(R.id.cet_login_name);
        passWord = findViewById(R.id.cet_login_password);
        Button login = findViewById(R.id.btn_login_confirm);
        TextView register = findViewById(R.id.tv_login_register);
        TextView forget = findViewById(R.id.tv_login_forget);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forget.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        DaggerLoginComponent.builder().loginModule(new LoginModule(this))
                .chatMainComponent(ChatApplication.getChatMainComponent())
                .build().inject(this);
    }

    @Override
    protected void updateView() {

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_login_confirm) {
            boolean isNetConnected = AppUtil.isNetworkAvailable();
            if (!isNetConnected) {
                ToastUtils.showShortToast(getString(R.string.network_tip));
            } else {
                login();
            }

        } else if (i == R.id.tv_login_register) {
            Intent intent = new Intent(getActivity(), RegisterActivity.class);
            startActivityForResult(intent, ConstantUtil.REQUEST_CODE_REGISTER);

        } else if (i == R.id.tv_login_forget) {
            LogUtil.e("忘记密码");
        }
    }


    private void login() {
        if (TextUtils.isEmpty(userName.getText())) {
            userName.startShakeAnimation();
            ToastUtils.showShortToast(getString(R.string.account_null));
            return;
        }
        if (TextUtils.isEmpty(passWord.getText())) {
            passWord.startShakeAnimation();
            ToastUtils.showShortToast(getString(R.string.password_null));
            return;
        }
        if (!AppUtil.isNetworkAvailable()) {
            ToastUtils.showShortToast(getString(R.string.network_tip));
            return;
        }
        presenter.login(userName.getText().toString().trim()
                , passWord.getText().toString().trim());
    }

    @Override
    public void updateData(BaseBean baseBean) {
        boolean isFirstLogin = BaseApplication.getAppComponent().getSharedPreferences()
                .getBoolean(UserManager.getInstance().getCurrentUserObjectId() + ConstantUtil.FIRST_STATUS, false);

        BaseApplication.getAppComponent()
                .getSharedPreferences()
                .edit().putBoolean(UserManager.getInstance().getCurrentUserObjectId() + ConstantUtil.FIRST_STATUS, !isFirstLogin).apply();
        dealResultInfo(isFirstLogin);
    }

    private void dealResultInfo(boolean isFirstLogin) {
        if (getAppComponent().getSharedPreferences().getBoolean(Constant.ALONE, false)) {
            Router.getInstance().deal(RouterRequest
                    .newBuild().context(getActivity()).isFinish(true).provideName(RouterConfig.MAIN_PROVIDE_NAME)
                    .actionName("login").build());
        } else {
            HomeActivity.start(getActivity(), isFirstLogin);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ConstantUtil.REQUEST_CODE_REGISTER:
                    String name = data.getStringExtra("username");
                    String password = data.getStringExtra("password");
                    if (name != null && password != null) {
                        passWord.setText(password);
                        userName.setText(name);
                    }
                    BaseApplication.getAppComponent()
                            .getSharedPreferences()
                            .edit().putBoolean(UserManager.getInstance().getCurrentUserObjectId() + ConstantUtil.FIRST_STATUS, true).apply();
                    login();
                    break;

            }
        }
    }
}
