package com.example.chat.mvp.splash;

import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.chat.R;
import com.example.chat.base.Constant;
import com.example.chat.bean.GroupTableMessage;
import com.example.chat.bean.User;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.login.LoginActivity;
import com.example.chat.mvp.main.HomeActivity;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.utils.CommonLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/11      16:52
 * QQ:             1981367757
 * Splash界面
 */
@Route(path = "/chat/splash")
public class SplashActivity extends BaseActivity {


        private void updateUserInfo() {
                UserManager.getInstance().queryAndSaveCurrentContactsList(new FindListener<User>() {
                                                                                  @Override
                                                                                  public void done(final List<User> contacts, BmobException e) {
                                                                                          if (e == null) {
                                                                                                  String uid = UserManager.getInstance().getCurrentUserObjectId();
                                                                                                  MsgManager.getInstance().queryGroupTableMessage(uid, new FindListener<GroupTableMessage>() {
                                                                                                          @Override
                                                                                                          public void done(List<GroupTableMessage> list, BmobException e) {
                                                                                                                  if (e == null) {
                                                                                                                          if (list != null && list.size() > 0) {
                                                                                                                                  CommonLogger.e("在服务器上查询到该用户所有的群,数目:" + list.size());
                                                                                                                                  List<GroupTableMessage> newData=new ArrayList<>();
                                                                                                                                  for (GroupTableMessage message :
                                                                                                                                          list) {
//                                                                                                                                  这里进行判断出现是因为有可能建群失败的时候，未能把groupId上传上去
                                                                                                                                          if (message.getGroupId()!=null) {
                                                                                                                                                  newData.add(message);
                                                                                                                                          }
                                                                                                                                  }
                                                                                                                                  UserDBManager.getInstance().addOrUpdateGroupTable(newData);
                                                                                                                          } else {
                                                                                                                                  LogUtil.e("服务器上没有查到该用户所拥有的群");
                                                                                                                          }
                                                                                                                  }else {
                                                                                                                          LogUtil.e("在服务器上查询所有群结构消息失败" +e.toString());
                                                                                                                          if (e.getErrorCode() == 101) {
                                                                                                                                CommonLogger.e("没有创立群表");
                                                                                                                          }
                                                                                                                  }
                                                                                                                  jumpToHome();
                                                                                                          }




                                                                                                          }

                                                                                                  );
                                                                                          }else {
                                                                                                  CommonLogger.e("查询好友失败"+e.toString());
                                                                                                  jumpToHome();
                                                                                          }
                                                                                  }

                                                                          }

                );
        }

        private void jumpToHome() {
                HomeActivity.start(SplashActivity.this, false);
                finish();
        }

        @Override
        protected void onResume() {
                super.onResume();
                boolean isLogin = BaseApplication.getAppComponent().getSharedPreferences().getBoolean(Constant.LOGIN_STATUS,false);
                if (isLogin) {
                        Flowable.timer(1, TimeUnit.SECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(aLong -> {
                                        LogUtil.e("该用户有缓存数据，直接跳转到主界面");
                                        updateUserInfo();
                                });
                } else {
                        Flowable.timer(1, TimeUnit.SECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(aLong -> {
                                        LogUtil.e("该用户无缓存数据，直接跳转到登录界面");
                                        LoginActivity.start(SplashActivity.this,null);
                                        finish();
                                });
                }
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
                return R.layout.activity_splash;
        }

        @Override
        protected void initView() {

        }

        @Override
        protected void initData() {

        }



        @Override
        public void updateData(Object o) {

        }
}
