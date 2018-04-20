package com.example.chat.mvp.main;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.chat.R;
import com.example.chat.base.Constant;
import com.example.chat.base.SlideBaseActivity;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.editInfo.EditUserInfoActivity;
import com.example.chat.mvp.settings.SettingsActivity;
import com.example.chat.mvp.wallpaper.WallPaperActivity;
import com.example.chat.mvp.search.SearchActivity;
import com.example.chat.mvp.searchFriend.SearchFriendActivity;
import com.example.chat.mvp.selectFriend.SelectedFriendsActivity;
import com.example.commonlibrary.utils.ToastUtils;


/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/24     11:59
 * QQ:         1981367757
 */

public class HomeActivity extends SlideBaseActivity {





    @Override
    public void updateData(Object o) {

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ((HomeFragment) currentFragment).notifyNewIntentCome(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu_layout, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = (String) item.getTitle();
        switch (title) {
            case "搜索":
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            case "添加好友":
                ToastUtils.showShortToast("点击了添加好友");
                SearchFriendActivity.start(this);
                break;
            case "建群":
                ToastUtils.showShortToast("创建群由于后台实时数据服务收费问题暂未开放");
                break;
            case "背景":
                ToastUtils.showShortToast("点击了背景");
                WallPaperActivity.start(this,Constant.WALLPAPER);
                break;
            case "设置":
                ToastUtils.showShortToast("点击了设置");
                SettingsActivity.start(this);
                break;
            default:
                break;
        }
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
        if (getIntent().getBooleanExtra(Constant.FIRST_LOGIN, false)) {
            EditUserInfoActivity.start(this, UserManager.getInstance()
            .getCurrentUserObjectId());
        }
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

    public static void start(Activity activity, boolean isFirstLogin) {
        Intent intent = new Intent(activity, HomeActivity.class);
        intent.putExtra(Constant.FIRST_LOGIN,isFirstLogin);
        activity.startActivity(intent);
    }
}
