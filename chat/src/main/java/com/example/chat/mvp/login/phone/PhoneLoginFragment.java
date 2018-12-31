package com.example.chat.mvp.login.phone;

import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.base.AppBaseFragment;
import com.example.chat.base.ConstantUtil;
import com.example.chat.dagger.login.DaggerPhoneLoginComponent;
import com.example.chat.dagger.login.PhoneLoginModule;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.main.HomeActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.customview.CustomEditText;
import com.example.commonlibrary.router.Router;
import com.example.commonlibrary.router.RouterConfig;
import com.example.commonlibrary.router.RouterRequest;
import com.example.commonlibrary.utils.AppUtil;
import com.example.commonlibrary.utils.Constant;
import com.example.commonlibrary.utils.TimeUtil;
import com.example.commonlibrary.utils.ToastUtils;


/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/29     16:18
 */
public class PhoneLoginFragment extends AppBaseFragment<BaseBean, PhoneLoginPresenter> implements View.OnClickListener {

    private CustomEditText phone, verifyCode;
    private TextView getVerifyCode;
    private RelativeLayout verifyContainer;
    private TextView verifyPhone;
    private TextView title;
    private Button login;


    @Override
    protected boolean isNeedHeadLayout() {
        return false;
    }


    @Override
    protected boolean needStatusPadding() {
        return false;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_phone_login;
    }

    @Override
    protected void initView() {
        phone = findViewById(R.id.et_fragment_phone_login_phone);
        title = findViewById(R.id.tv_fragment_phone_login_title);
        verifyCode = findViewById(R.id.et_fragment_phone_login_verify_code);
        getVerifyCode = findViewById(R.id.tv_fragment_phone_login_get_verify_code);
        verifyContainer = findViewById(R.id.rl_fragment_phone_login_verify_container);
        verifyPhone = findViewById(R.id.tv_fragment_phone_login_phone_login);
        getVerifyCode.setOnClickListener(this);
        login = findViewById(R.id.tv_fragment_phone_login_confirm);
        login.setOnClickListener(this);
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (AppUtil.isPhone(s.toString())) {
                        getVerifyCode.setEnabled(true);
                    } else {
                        getVerifyCode.setEnabled(false);
                    }
                } else {
                    getVerifyCode.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initData() {
        DaggerPhoneLoginComponent.builder().chatMainComponent(getMainComponent())
                .phoneLoginModule(new PhoneLoginModule(this))
                .build().inject(this);
        title.setText("手机号登录");
    }

    @Override
    protected void updateView() {

    }


    public static PhoneLoginFragment newInstance() {
        return new PhoneLoginFragment();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_fragment_phone_login_confirm) {
            String code = verifyCode.getText().toString().trim();
            if (TextUtils.isEmpty(code)) {
                ToastUtils.showShortToast("输入的验证码不能为空");
                verifyCode.startShakeAnimation();
                return;
            }
            String input = phone.getText().toString().trim();
            if (TextUtils.isEmpty(input)) {
                ToastUtils.showShortToast("输入手机号不能为空");
                phone.startShakeAnimation();
                return;
            }
            if (!AppUtil.isPhone(input)) {
                ToastUtils.showShortToast("输入的手机号格式不对!");
                return;
            }

            presenter.login(input, code);
        } else if (id == R.id.tv_fragment_phone_login_get_verify_code) {
            String input = phone.getText().toString().trim();
            if (TextUtils.isEmpty(input)) {
                ToastUtils.showShortToast("输入手机号不能为空");
                phone.startShakeAnimation();
                return;
            }
            if (!AppUtil.isPhone(input)) {
                ToastUtils.showShortToast("输入的手机号格式不对!");
                return;
            }
            getVerifyCode.setEnabled(false);
            presenter.getVerifyCode(input);
        }
    }

    @Override
    public void updateData(BaseBean o) {
        if (o != null) {
            if (o.getCode() == 200) {
                if (o.getType() == ConstantUtil.BASE_TYPE_SEND_VERIFY_CODE) {
                    ToastUtils.showShortToast("验证码发送成功，请注意查收");
                    verifyContainer.setVisibility(View.VISIBLE);
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                    SpannableString spannableString = new SpannableString(phone.getText().toString().trim());
                    spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, phone.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableStringBuilder.append("已发送验证码至 ").append(spannableString);
                    verifyPhone.setText(spannableStringBuilder);
                    getVerifyCode.setEnabled(false);
                    addDisposable(TimeUtil.countDown(60, aLong -> getVerifyCode.setText((60 - aLong) + "s"), aLong -> {
                        getVerifyCode.setEnabled(true);
                        getVerifyCode.setText("重新发送验证码");
                    }));
                } else if (o.getType() == ConstantUtil.BASE_TYPE_PHONE_LOGIN) {
                    boolean isFirstLogin = BaseApplication.getAppComponent().getSharedPreferences()
                            .getBoolean(UserManager.getInstance().getCurrentUserObjectId() + ConstantUtil.FIRST_STATUS, false);

                    BaseApplication.getAppComponent()
                            .getSharedPreferences()
                            .edit().putBoolean(UserManager.getInstance().getCurrentUserObjectId() + ConstantUtil.FIRST_STATUS, !isFirstLogin).apply();
                    dealResultInfo(isFirstLogin);
                }
            } else {
                ToastUtils.showShortToast(o.getDesc());
                if (o.getType() == ConstantUtil.BASE_TYPE_SEND_VERIFY_CODE) {
                    getVerifyCode.setEnabled(true);
                }
            }
        }
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
}
