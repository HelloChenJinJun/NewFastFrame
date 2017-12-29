package com.example.chat.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.chat.R;
import com.example.chat.bean.SharedMessage;
import com.example.chat.ui.fragment.HomeFragment;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.utils.ToastUtils;

import java.io.Serializable;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/24     11:59
 * QQ:         1981367757
 */

public class HomeActivity extends BaseActivity {


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtil.e("onNewIntent");
        if (intent.getSerializableExtra("url_share_message") != null) {
            Serializable sharedMessage = intent.getSerializableExtra("url_share_message");
            ((HomeFragment) currentFragment).notifyUrlSharedMessageAdd(sharedMessage);
            return;
        }
        ((HomeFragment) currentFragment).notifyNewIntentCome(intent);
    }


    @Override
    public void updateData(Object o) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu_layout, menu);
        return true;
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
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        addOrReplaceFragment(HomeFragment.newInstance(), R.id.fl_activity_home_container);
    }


    private long mExitTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            ToastUtils.showShortToast("再按一次退出程序");
            mExitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, HomeActivity.class);
        activity.startActivity(intent);
    }
}
