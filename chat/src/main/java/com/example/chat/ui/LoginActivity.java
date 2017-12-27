package com.example.chat.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chat.ChatApplication;
import com.example.chat.R;
import com.example.chat.base.Constant;
import com.example.chat.base.RandomData;
import com.example.chat.bean.GroupTableMessage;
import com.example.chat.bean.User;
import com.example.chat.dagger.login.DaggerLoginComponent;
import com.example.chat.dagger.login.LoginModule;
import com.example.chat.db.ChatDB;
import com.example.chat.manager.LocationManager;
import com.example.chat.manager.MessageCacheManager;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserCacheManager;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.login.LoginPresenter;
import com.example.chat.util.BmobUtils;
import com.example.chat.util.ChatUtil;
import com.example.chat.util.CommonUtils;
import com.example.chat.util.LogUtil;
import com.example.chat.view.AutoEditText;
import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.router.Router;
import com.example.commonlibrary.router.RouterRequest;
import com.example.commonlibrary.router.RouterResult;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.rxbus.event.LoginEvent;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import io.reactivex.functions.Consumer;

/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/11      17:49
 * QQ:             1981367757
 */
public class LoginActivity extends BaseActivity<Object, LoginPresenter> implements View.OnClickListener {
    private AutoEditText userName;
    private AutoEditText passWord;
    private ImageView bg;

    @Override
    protected void onResume() {
        super.onResume();

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
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        bg = (ImageView) findViewById(R.id.iv_login_bg);
        userName = (AutoEditText) findViewById(R.id.aet_login_name);
        passWord = (AutoEditText) findViewById(R.id.aet_login_password);
        Button login = (Button) findViewById(R.id.btn_login_confirm);
        TextView register = (TextView) findViewById(R.id.tv_login_register);
        TextView forget = (TextView) findViewById(R.id.tv_login_forget);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forget.setOnClickListener(this);
    }


    @Override
    public void initData() {
        DaggerLoginComponent.builder().loginModule(new LoginModule(this))
                .chatMainComponent(ChatApplication.getChatMainComponent())
                .build().inject(this);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override

            public void run() {
                Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.translate_anim);
                bg.startAnimation(animation);
            }
        }, 200);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_login_confirm) {
            boolean isNetConnected = CommonUtils.isNetWorkAvailable(this);
            if (!isNetConnected) {
                ToastUtils.showShortToast(getString(R.string.network_tip));
            } else {
                login();
            }

        } else if (i == R.id.tv_login_register) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivityForResult(intent, Constant.REQUEST_CODE_REGISTER);

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
        if (!CommonUtils.isNetWorkAvailable(this)) {
            ToastUtils.showShortToast(getString(R.string.network_tip));
            return;
        }
        showLoadDialog("正在登录.........");
        presenter.registerEvent(LoginEvent.class, new Consumer<LoginEvent>() {
            @Override
            public void accept(LoginEvent loginEvent) throws Exception {
                if (loginEvent.isSuccess()) {
                    presenter.login(userName.getText().toString().trim()
                            , passWord.getText().toString().trim(), loginEvent.getUserInfoEvent());
                } else {
                    ToastUtils.showShortToast(loginEvent.getErrorMessage());
                }
            }
        });
        Map<String, Object> map = new HashMap<>();
        map.put(ConstantUtil.ACCOUNT, userName.getText().toString().trim());
        map.put(ConstantUtil.PASSWORD, passWord.getText().toString().trim());
        Router.getInstance().deal(new RouterRequest.Builder()
                .provideName("news").actionName("login").paramMap(map).build());
    }


    private void dealResultInfo(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put(ConstantUtil.AVATAR, user.getAvatar());
        map.put(ConstantUtil.SIGNATURE, user.getSignature());
        map.put(ConstantUtil.NICK, user.getNick());
        map.put(ConstantUtil.ACCOUNT, user.getUsername());
        map.put(ConstantUtil.NAME, user.getNick());
        map.put(ConstantUtil.SEX, user.isSex());
        map.put(ConstantUtil.BG_HALF, user.getTitleWallPaper());
        map.put(ConstantUtil.BG_ALL, user.getWallPaper());
        map.put(ConstantUtil.STUDENT_TYPE, getIntent().getStringExtra(ConstantUtil.STUDENT_TYPE));
        map.put(ConstantUtil.COLLEGE, getIntent().getStringExtra(ConstantUtil.COLLEGE));
        map.put(ConstantUtil.PASSWORD, passWord.getText().toString().trim());
        map.put(ConstantUtil.FROM, getIntent().getStringExtra(ConstantUtil.FROM));
        Router.getInstance().deal(new RouterRequest.Builder()
                .paramMap(map).context(this).provideName("news")
                .actionName("person").isFinish(true).build());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constant.REQUEST_CODE_REGISTER:
                    String name = data.getStringExtra("username");
                    String password = data.getStringExtra("password");
                    if (name != null && password != null) {
                        passWord.setText(password);
                        userName.setText(name);
                    }
                    break;
                case Constant.REQUEST_CODE_EDIT_USER_INFO:
                    User user = (User) data.getSerializableExtra("user");
                    dealResultInfo(user);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void updateData(Object o) {
        boolean isFirstLogin = BaseApplication.getAppComponent().getSharedPreferences()
                .getBoolean(ConstantUtil.FIRST_STATUS, false);
        if (isFirstLogin) {
            BaseApplication.getAppComponent()
                    .getSharedPreferences()
                    .edit().putBoolean(ConstantUtil.FIRST_STATUS, false).apply();
            Intent intent = new Intent(LoginActivity.this, EditUserInfoActivity.class);
            startActivityForResult(intent, Constant.REQUEST_CODE_EDIT_USER_INFO);
        } else {
            User user = UserManager.getInstance().getCurrentUser();
            dealResultInfo(user);
        }

    }
}
