package com.example.news;

import android.content.Intent;
import android.view.View;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.BaseFragment;
import com.example.news.mvp.cardinfo.CardInfoActivity;
import com.example.news.mvp.cardlogin.CardLoginActivity;
import com.example.news.mvp.librarylogin.LibraryLoginActivity;
import com.example.news.util.NewsUtil;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/18      14:58
 * QQ:             1981367757
 */

public class CenterFragment extends BaseFragment implements View.OnClickListener {
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
        return R.layout.fragment_center;
    }

    @Override
    protected void initView() {
        findViewById(R.id.tv_fragment_center_library).setOnClickListener(this);
        findViewById(R.id.tv_fragment_center_library_card).setOnClickListener(this);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void updateView() {

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_fragment_center_library) {
            if (BaseApplication.getAppComponent().getSharedPreferences().getString(NewsUtil.LIBRARY_COOKIE, null) == null) {
            Intent intent = new Intent(getContext(), LibraryLoginActivity.class);
            startActivity(intent);
            }else {
                Intent intent = new Intent(getContext(), LibraryInfoActivity.class);
                startActivity(intent);
            }
        } else {
            if (BaseApplication.getAppComponent().getSharedPreferences()
                    .getString(NewsUtil.CARD_POST_LOGIN_COOKIE, null) == null) {
                Intent intent = new Intent(getContext(), CardLoginActivity.class);
                startActivity(intent);
            }else {
                Intent intent=new Intent(getContext(),CardInfoActivity.class);
                startActivity(intent);
            }
        }
    }

    public static CenterFragment newInstance() {
        return new CenterFragment();
    }
}
