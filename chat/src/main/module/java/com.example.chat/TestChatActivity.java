package com.example.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.chat.mvp.splash.SplashActivity;


/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/10/7      23:36
 * QQ:             1981367757
 */

public class TestChatActivity extends AppCompatActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_chat);

    }

    public void chat(View view){
        Intent intent=new Intent(this, SplashActivity.class);
        startActivity(intent);
    }


}
