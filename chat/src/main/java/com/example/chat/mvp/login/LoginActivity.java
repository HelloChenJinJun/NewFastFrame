package com.example.chat.mvp.login;

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
import com.example.chat.bean.User;
import com.example.chat.dagger.login.DaggerLoginComponent;
import com.example.chat.dagger.login.LoginModule;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.editInfo.EditUserInfoActivity;
import com.example.chat.mvp.main.HomeActivity;
import com.example.chat.mvp.register.RegisterActivity;
import com.example.chat.util.CommonUtils;
import com.example.chat.util.LogUtil;
import com.example.chat.view.AutoEditText;
import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.router.Router;
import com.example.commonlibrary.router.RouterRequest;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.rxbus.event.LoginEvent;
import com.example.commonlibrary.utils.AppUtil;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/11      17:49
 * QQ:             1981367757
 * 登录界面
 */
public class LoginActivity extends BaseActivity<Object, LoginPresenter> implements View.OnClickListener {
    private AutoEditText userName;
    private AutoEditText passWord;
    private ImageView bg;
    private String from;
//    private Button main;



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
    protected boolean needStatusPadding() {
        return false;
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
        addDisposable(RxBusManager.getInstance().registerEvent(User.class,user-> dealResultInfo(user,false), throwable -> {
        }));
        from=getIntent().getStringExtra(ConstantUtil.FROM);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.translate_anim);
            bg.startAnimation(animation);
        }, 200);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_login_confirm) {
            boolean isNetConnected =AppUtil.isNetworkAvailable();
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
        if (!AppUtil.isNetworkAvailable()) {
            ToastUtils.showShortToast(getString(R.string.network_tip));
            return;
        }
        showLoadDialog("正在登录.........");
//        if (from!=null) {
////            与新闻模块进行交互
//            presenter.registerEvent(LoginEvent.class, loginEvent -> {
////                新闻模块登录后发送事件
//                if (loginEvent.isSuccess()) {
//                    presenter.login(userName.getText().toString().trim()
//                            , passWord.getText().toString().trim(), loginEvent.getUserInfoEvent());
//                } else {
//                    hideLoading();
//                    ToastUtils.showShortToast(loginEvent.getErrorMessage());
//                }
//            });
////            传送登录参数到新闻模块
//            Map<String, Object> map = new HashMap<>();
//            map.put(ConstantUtil.ACCOUNT, userName.getText().toString().trim());
//            map.put(ConstantUtil.PASSWORD, passWord.getText().toString().trim());
//            Router.getInstance().deal(new RouterRequest.Builder()
//                    .provideName("news").actionName("login").paramMap(map).build());
//        }else {
//            presenter.login(userName.getText().toString().trim()
//                    , passWord.getText().toString().trim(),null);
//        }
        presenter.login(userName.getText().toString().trim()
                , passWord.getText().toString().trim(),null);
    }


    private void dealResultInfo(User user,boolean isFirstLogin) {
        if (from!=null) {
            Map<String, Object> map = new HashMap<>();
            map.put(ConstantUtil.AVATAR, user.getAvatar());
            map.put(ConstantUtil.SIGNATURE, user.getSignature());
            map.put(ConstantUtil.NICK, user.getNick());
            map.put(ConstantUtil.ACCOUNT, user.getUsername());
            map.put(ConstantUtil.NAME, user.getName());
            map.put(ConstantUtil.SEX, user.isSex());
            map.put(ConstantUtil.BG_HALF, user.getTitleWallPaper());
            map.put(ConstantUtil.BG_ALL, user.getWallPaper());
            map.put(ConstantUtil.CLASS_NUMBER,user.getClassNumber());
            map.put(ConstantUtil.SCHOOL,user.getSchool());
            map.put(ConstantUtil.MAJOR,user.getMajor());
            map.put(ConstantUtil.YEAR,user.getYear());
            map.put(ConstantUtil.STUDENT_TYPE, user.getEducation());
            map.put(ConstantUtil.COLLEGE, user.getCollege());
            map.put(ConstantUtil.YEAR,user.getYear());
            map.put(ConstantUtil.PASSWORD,user.getPw());
            map.put(ConstantUtil.FROM, from);
            Router.getInstance().deal(new RouterRequest.Builder()
                    .paramMap(map).context(this).provideName("app")
                    .actionName("person").isFinish(true).build());
        }else {
            HomeActivity.start(this,isFirstLogin);
            finish();
        }
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
                    BaseApplication.getAppComponent()
                            .getSharedPreferences()
                            .edit().putBoolean(UserManager.getInstance().getCurrentUserObjectId()+ConstantUtil.FIRST_STATUS, true).apply();
                    login();
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
                .getBoolean(UserManager.getInstance().getCurrentUserObjectId()+ConstantUtil.FIRST_STATUS, false);

            BaseApplication.getAppComponent()
                    .getSharedPreferences()
                    .edit().putBoolean(UserManager.getInstance().getCurrentUserObjectId()+ConstantUtil.FIRST_STATUS, !isFirstLogin).apply();
        User user = UserManager.getInstance().getCurrentUser();
        dealResultInfo(user,isFirstLogin);
    }



    public static void start(Activity activity,String from){
        Intent intent=new Intent(activity,LoginActivity.class);
        intent.putExtra(ConstantUtil.FROM,from);
        activity.startActivity(intent);
    }

}
