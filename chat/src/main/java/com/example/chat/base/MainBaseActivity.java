package com.example.chat.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.chat.ChatApplication;
import com.example.chat.dagger.ChatMainComponent;
import com.example.chat.listener.OnEditDataCompletedListener;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.login.LoginActivity;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Subscription;


/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/13      11:46
 * QQ:             1981367757
 */
public abstract class MainBaseActivity<T, P extends BasePresenter> extends BaseActivity<T, P> {
    private Set<Subscription> subscriptionList = new HashSet<>();


    protected void addSubscription(Subscription subscription) {
        subscriptionList.add(subscription);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscriptionList!=null) {
            for (Subscription item :
                    subscriptionList) {
                if (!item.isUnsubscribed()) {
                    item.unsubscribe();
                }
            }
            subscriptionList.clear();
            subscriptionList=null;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                LoginActivity.start(this,ConstantUtil.FROM_LOGIN);
            }else {
                LoginActivity.start(this,null);
            }
            finish();
        }
    }




    public ChatMainComponent getChatMainComponent(){
        return ChatApplication.getChatMainComponent();
    }


    public void showEditDialog(String title, List<String> names, final OnEditDataCompletedListener listener) {
        if (names != null && names.size() > 0) {
            mBaseDialog.setTitle(title).setEditViewsName(names).setLeftButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelBaseDialog();
                }
            }).setRightButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int size = mBaseDialog.getMiddleLayout().getChildCount();
                        List<String> data = new ArrayList<>();
                        for (int i = 0; i < size; i++) {
                            String result = ((EditText) ((LinearLayout) mBaseDialog.getMiddleLayout().getChildAt(i)).getChildAt(1)).getText().toString().trim();
                            if (result.equals("")) {
                                ToastUtils.showShortToast("1输入内容不能为空");
                                LogUtil.e("输入框不能为空");
                                return;
                            }
                            data.add(result);
                        }
                        listener.onDataInputCompleted(data);
                        dismissBaseDialog();
                    }
                }
            }).show();
        }
    }

}
