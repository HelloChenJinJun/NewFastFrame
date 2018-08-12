package com.example.music;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.chat.manager.UserManager;
import com.example.chat.mvp.login.LoginActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.StatusBarUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/26     20:10
 * QQ:         1981367757
 */

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {
    private TextView time;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_main);
        StatusBarUtil.setTranslucentForImageViewInFragment(this,0,null);
        time = (TextView) findViewById(R.id.tv_activity_splash_main_time);
        TextView title = (TextView) findViewById(R.id.tv_activity_splash_main_title);
        Animation animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_top_in);
        animation.setAnimationListener(this);
        title.startAnimation(animation);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Flowable.intervalRange(0, 3, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(aLong -> time.setText((3 - aLong)+""))
                .doOnComplete(() -> {
//                    倒计时完毕置为可点击状态
                    if (BaseApplication.getAppComponent().getSharedPreferences()
                            .getBoolean(ConstantUtil.FIRST_STATUS,true)) {
                        BaseApplication.getAppComponent().getSharedPreferences()
                                .edit().putBoolean(ConstantUtil.FIRST_STATUS,false)
                                .apply();
                        LoginActivity.start(this,ConstantUtil.FROM_LOGIN);
                    }else {
                        if (UserManager.getInstance().getCurrentUser() != null) {
                            MainActivity.start(this);
                        }else {
                            LoginActivity.start(this,ConstantUtil.FROM_LOGIN);
                        }
                    }
//                    if (UserManager.getInstance().getCurrentUser() != null) {
//                        MainActivity.start(this);
//                    }else {
//                        LoginActivity.start(this,ConstantUtil.FROM_LOGIN);
//                    }
                    finish();
                })
                .subscribe();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
