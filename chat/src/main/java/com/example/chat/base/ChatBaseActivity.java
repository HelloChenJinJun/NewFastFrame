package com.example.chat.base;

import com.example.chat.ChatApplication;
import com.example.chat.dagger.ChatMainComponent;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.login.LoginActivity;
import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.mvp.presenter.BasePresenter;

import java.util.HashSet;
import java.util.Set;

import rx.Subscription;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/26      11:58
 * QQ:             1981367757
 */

public abstract class ChatBaseActivity<T, P extends BasePresenter> extends BaseActivity<T, P> {

    protected Set<Subscription> subscriptionSet = new HashSet<>();

    public void addSubscription(Subscription subscription) {
        if (subscription != null) {
            subscriptionSet.add(subscription);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

    private void checkLogin() {
        if (UserManager.getInstance().getCurrentUser() == null) {
            if (!BaseApplication
                    .getAppComponent().getSharedPreferences()
                    .getBoolean(ConstantUtil.IS_ALONE, true)) {
                LoginActivity.start(this, ConstantUtil.FROM_LOGIN);
            } else {
                LoginActivity.start(this, null);
            }
            finish();
        }
    }


    protected ChatMainComponent getChatMainComponent() {
        return ChatApplication.getChatMainComponent();
    }


}
