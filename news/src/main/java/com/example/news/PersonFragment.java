package com.example.news;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.cusotomview.RoundAngleImageView;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.news.event.UserInfoEvent;
import com.example.news.mvp.systeminfo.SystemInfoLoginActivity;
import com.example.news.util.NewsUtil;

import io.reactivex.functions.Consumer;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/21     14:28
 * QQ:         1981367757
 */

public class PersonFragment extends BaseFragment implements View.OnClickListener {
    private TextView signature;
    private RoundAngleImageView avatar;
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
        return R.layout.fragment_person;
    }

    @Override
    protected void initView() {
        signature= (TextView) findViewById(R.id.tv_fragment_person_signature);
        avatar= (RoundAngleImageView) findViewById(R.id.riv_fragment_person_avatar);
        avatar.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        RxBusManager.getInstance().registerEvent(UserInfoEvent.class, new Consumer<UserInfoEvent>() {
            @Override
            public void accept(UserInfoEvent userInfoEvent) throws Exception {
                Intent intent=new Intent();
                intent.putExtra("from","news");
                intent.setClassName("com.example.chat","LoginActivity");
                intent.putExtra("account",userInfoEvent.getAccount());
                intent.putExtra("password",userInfoEvent.getPassword());
                getActivity().startActivityForResult(intent,ConstantUtil.REQUEST_CODE_LOGIN);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (throwable != null) {
                    ToastUtils.showShortToast("请求异常"+throwable.getMessage());
                }
            }
        });
    }

    @Override
    protected void updateView() {

    }

    public static PersonFragment newInstance() {
        return new PersonFragment();
    }

    @Override
    public void onClick(View view) {
        if (BaseApplication.getAppComponent().getSharedPreferences().getBoolean(NewsUtil
                .IS_LOGIN, false)) {
            ToastUtils.showShortToast("已经登录");
        }else {
            SystemInfoLoginActivity.start(getActivity());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode){
                case ConstantUtil.REQUEST_CODE_LOGIN:
                    Glide.with(getContext()).load(data.getStringExtra("avatar")).into(avatar);
                    signature.setText(data.getStringExtra("signature"));
                    ToastUtils.showShortToast("登录成功");
                    break;
            }
        }
    }
}
