package com.example.chat.mvp.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.chat.R;
import com.example.chat.base.ConstantUtil;
import com.example.chat.mvp.login.phone.PhoneLoginFragment;
import com.example.chat.mvp.login.pw.PwLoginFragment;
import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.baseadapter.adapter.ViewPagerAdapter;
import com.example.commonlibrary.customview.WrappedViewPager;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;


/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/11      17:49
 * QQ:             1981367757
 * 登录界面
 */
public class LoginActivity extends BaseActivity {


    private List<Fragment> mFragmentList;
    private WrappedViewPager wrappedViewPager;
    private ImageView bg;


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
        wrappedViewPager = findViewById(R.id.wvp_activity_login_container);
        bg = findViewById(R.id.iv_login_bg);
    }


    @Override
    public void initData() {

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mFragmentList = new ArrayList<>();
        mFragmentList.add(PwLoginFragment.newInstance());
        mFragmentList.add(PhoneLoginFragment.newInstance());
        viewPagerAdapter.setTitleAndFragments(null, mFragmentList);
        wrappedViewPager.setAdapter(viewPagerAdapter);
        wrappedViewPager.setCurrentItem(0);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.translate_anim);
            bg.startAnimation(animation);
        }, 200);
    }


    @Override
    public void updateData(Object o) {
    }


    public static void start(Activity activity, String from) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra(ConstantUtil.FROM, from);
        activity.startActivity(intent);
    }

}
