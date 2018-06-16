package com.example.music;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chat.mvp.login.LoginActivity;
import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.cusotomview.WrappedViewPager;
import com.example.commonlibrary.utils.ConstantUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/26     20:21
 * QQ:         1981367757
 */

public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private WrappedViewPager display;
    private GuideAdapter guideAdapter;
    private TextView enter;
    private View one, two, three, four;

    @Override
    public void updateData(Object o) {

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
        return R.layout.activity_guide;
    }

    @Override
    protected void initView() {
        display = (WrappedViewPager) findViewById(R.id.wvp_activity_guide_display);
        one = findViewById(R.id.view_activity_guide_one);
        two = findViewById(R.id.view_activity_guide_two);
        three = findViewById(R.id.view_activity_guide_three);
        four = findViewById(R.id.view_activity_guide_four);
        int[] imageResId = new int[]{R.drawable.login_container_bg_01, R.drawable.login_container_bg_02
                , R.drawable.login_container_bg_03, R.drawable.login_container_bg_04};
        List<View> viewList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            View view = getLayoutInflater().inflate(R.layout.view_activity_guide, null);
            ((ImageView) view.findViewById(R.id.iv_view_activity_guide_display))
                    .setImageResource(imageResId[i]);
            if (i == 3) {
                enter = view.findViewById(R.id.tv_view_activity_guide_enter);
                enter.setVisibility(View.VISIBLE);
                enter.setOnClickListener(this);
            }
            viewList.add(view);
        }
        guideAdapter = new GuideAdapter(viewList);
    }

    @Override
    protected void initData() {
        display.setAdapter(guideAdapter);
        display.addOnPageChangeListener(this);
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, GuideActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            one.setSelected(true);
            two.setSelected(false);
            three.setSelected(false);
            four.setSelected(false);
        } else if (position == 1) {
            one.setSelected(false);
            two.setSelected(true);
            three.setSelected(false);
            four.setSelected(false);
        } else if (position == 2) {
            one.setSelected(false);
            two.setSelected(false);
            three.setSelected(true);
            four.setSelected(false);
        } else if (position == 3) {
            one.setSelected(false);
            two.setSelected(false);
            three.setSelected(false);
            four.setSelected(true);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_view_activity_guide_enter) {
            BaseApplication
                    .getAppComponent()
                    .getSharedPreferences()
                    .edit().putBoolean(ConstantUtil.FIRST_STATUS,false).apply();
           LoginActivity.start(this,ConstantUtil.FROM_LOGIN);
        } else {
            MainActivity.start(this);
        }
        finish();
    }
}
