package com.example.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chat.manager.UserManager;
import com.example.chat.mvp.login.LoginActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.utils.ConstantUtil;


/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/26     20:10
 * QQ:         1981367757
 */

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {
    private ImageView display;
    private TextView time;
    private TextView title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_main);
        display = (ImageView) findViewById(R.id.iv_activity_splash_main_display);
        time = (TextView) findViewById(R.id.tv_activity_splash_main_time);
        title = (TextView) findViewById(R.id.tv_activity_splash_main_title);
        Animation animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_top_in);
        animation.setAnimationListener(this);
        title.startAnimation(animation);
    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (BaseApplication.getAppComponent().getSharedPreferences()
                .getBoolean(ConstantUtil.FIRST_STATUS,true)) {
            GuideActivity.start(this);
        }else {
            if (UserManager.getInstance().getCurrentUser() != null) {
                MainActivity.start(this);
            }else {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtra(ConstantUtil.FROM, ConstantUtil.FROM_LOGIN);
                startActivity(intent);
            }
        }
        finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
