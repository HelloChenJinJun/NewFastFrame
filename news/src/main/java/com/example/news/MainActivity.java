package com.example.news;


import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RadioGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.router.Router;
import com.example.commonlibrary.router.RouterRequest;
import com.example.commonlibrary.router.RouterResult;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.news.mvp.searchlibrary.LibraryFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = "/news/main")
public class MainActivity extends BaseActivity {

    private List<Fragment> fragmentList;
    private MenuItem searchItem, expendItem;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Router.getInstance().deal(new RouterRequest.Builder().provideName("chat").context(this)
                .actionName("intent").object(intent).build());
    }


    @Override
    public void updateData(Object o) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Map<String, Object> map = new HashMap<>();
        map.put(ConstantUtil.TITLE, item.getTitle());
        Router.getInstance().deal(new RouterRequest.Builder().context(this)
                .provideName("chat").actionName("menuOnClick")
                .paramMap(map).build());
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu_layout, menu);
        searchItem = menu.getItem(0);
        expendItem = menu.getItem(1);
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
        return R.layout.activity_main_news;
    }

    @Override
    protected void initView() {
        RadioGroup bottomContainer = (RadioGroup) findViewById(R.id.rg_activity_main_bottom_container);
        bottomContainer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.rb_activity_main_bottom_index) {
                    addOrReplaceFragment(fragmentList.get(0));
                } else if (checkedId == R.id.rb_activity_main_bottom_center) {
                    addOrReplaceFragment(fragmentList.get(1));
                } else if (checkedId == R.id.rb_activity_main_bottom_person) {
                    addOrReplaceFragment(fragmentList.get(2));
                } else if (checkedId == R.id.rb_activity_main_bottom_chat) {
                    boolean loginStatus = BaseApplication.getAppComponent()
                            .getSharedPreferences().getBoolean(ConstantUtil.LOGIN_STATUS, false);
                    if (loginStatus) {
                        addOrReplaceFragment(fragmentList.get(3));
                    } else {
                        ToastUtils.showShortToast("未登录状态，请先登录再进去");
                    }
                } else if (checkedId == R.id.rb_activity_main_bottom_public) {
                    addOrReplaceFragment(fragmentList.get(4));
                }
            }
        });
    }

    @Override
    protected void initData() {
        fragmentList = new ArrayList<>();
        fragmentList.add(IndexFragment.newInstance());
//        fragmentList.add(LibraryFragment.newInstance());
        fragmentList.add(CenterFragment.newInstance());
        fragmentList.add(PersonFragment.newInstance());
        RouterResult  result=Router.getInstance().deal(new RouterRequest.Builder()
                .provideName("chat").actionName("main").build());
        if (result!=null&&result.getObject()!=null) {
            fragmentList.add((Fragment) result.getObject());
        }
        RouterResult publicResult=Router.getInstance().deal(new RouterRequest.Builder()
                .provideName("chat").actionName("public").build());
        if (publicResult != null && publicResult.getObject() != null) {
            fragmentList.add((Fragment) publicResult.getObject());
        }
        addOrReplaceFragment(fragmentList.get(0), R.id.fl_activity_main_container);
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


    public void notifyLoginStatus(boolean isLogin) {
        getSupportFragmentManager().beginTransaction().remove(fragmentList.get(3))
                .commitAllowingStateLoss();
    }
}
