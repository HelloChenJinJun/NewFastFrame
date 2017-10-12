package com.example.chat.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.base.Constant;
import com.example.chat.bean.GroupTableMessage;
import com.example.chat.bean.User;
import com.example.chat.db.ChatDB;
import com.example.chat.manager.LocationManager;
import com.example.chat.manager.MessageCacheManager;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserCacheManager;
import com.example.chat.manager.UserManager;
import com.example.chat.util.BmobUtils;
import com.example.chat.util.ChatUtil;
import com.example.chat.util.CommonUtils;
import com.example.chat.util.LogUtil;
import com.example.chat.view.AutoEditText;
import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/11      17:49
 * QQ:             1981367757
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
        private AutoEditText userName;
        private AutoEditText passWord;
        private Button login;
        private TextView register;
        private TextView forget;
        private ImageView bg;
        private boolean isFirstLogin = false;
        private List<GroupTableMessage> newData;


        @Override
        protected void onResume() {
                super.onResume();

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
                return R.layout.activity_login;
        }

        @Override
        public void initView() {
                bg = (ImageView) findViewById(R.id.iv_login_bg);
                userName = (AutoEditText) findViewById(R.id.aet_login_name);
                passWord = (AutoEditText) findViewById(R.id.aet_login_password);
                login = (Button) findViewById(R.id.btn_login_confirm);
                register = (TextView) findViewById(R.id.tv_login_register);
                forget = (TextView) findViewById(R.id.tv_login_forget);
                login.setOnClickListener(this);
                register.setOnClickListener(this);
                forget.setOnClickListener(this);
        }




        @Override
        public void initData() {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override

                        public void run() {
                                Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.translate_anim);
                                bg.startAnimation(animation);
                        }
                }, 200);
        }

        @Override
        public void onClick(View view) {
                switch (view.getId()) {
                        case R.id.btn_login_confirm:
                                boolean isNetConnected = CommonUtils.isNetWorkAvailable(this);
                                if (!isNetConnected) {
                                        ToastUtils.showShortToast(getString(R.string.network_tip));
                                } else {
                                        login();
                                }
                                break;
                        case R.id.tv_login_register:
                                Intent intent = new Intent(this, RegisterActivity.class);
                                startActivityForResult(intent, Constant.REQUEST_CODE_REGISTER);
                                break;
                        case R.id.tv_login_forget:
                                LogUtil.e("忘记密码");
                                break;
                }
        }

        private void login() {
                if (TextUtils.isEmpty(userName.getText())) {
                        userName.startShakeAnimation();
                        ToastUtils.showShortToast(getString(R.string.account_null));
                        return;
                }
                if (TextUtils.isEmpty(passWord.getText())) {
                        passWord.startShakeAnimation();
                        ToastUtils.showShortToast(getString(R.string.password_null));
                        return;
                }
                if (!CommonUtils.isNetWorkAvailable(this)) {
                        ToastUtils.showShortToast(getString(R.string.network_tip));
                        return;
                }
                showLoadDialog("正在登录.........");
                final User user = new User();
                user.setUsername(userName.getText().toString().trim());
                user.setPassword(passWord.getText().toString().trim());
                if (LocationManager.getInstance().getLatitude() != 0 || LocationManager.getInstance().getLongitude() != 0) {
                        user.setLocation(new BmobGeoPoint(LocationManager.getInstance().getLongitude(), LocationManager.getInstance()
                                .getLatitude()));
                }
                if (newData != null) {
                        newData.clear();
                }
                user.login(BaseApplication.getInstance(), new SaveListener() {
                                @Override
                                public void onSuccess() {
                                        dismissLoadDialog();
                                        ToastUtils.showShortToast("登录成功");
                                        LogUtil.e("登录成功");
//                                        登录成功之后，
//                                        检查其他设备绑定的用户，强迫其下线
                                        LogUtil.e("检查该用户绑定的其他设备.....");
                                        UserManager.getInstance().checkInstallation(new UpdateListener() {
                                                @Override
                                                public void onSuccess() {
                                                        ToastUtils.showShortToast("登录成功1");
                                                        LogUtil.e("登录成功");
                                                        showLoadDialog("正在获取好友资料.........");
                                                        updateUserInfo();
                                                }

                                                @Override
                                                public void onFailure(int i, String s) {
                                                        ToastUtils.showShortToast("登录失败,请重新登录" + s + i);
                                                        LogUtil.e("登录失败" + s + i);
                                                }
                                        });
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                        dismissLoadDialog();
                                        ToastUtils.showShortToast("登录失败" + s + i);
                                }
                        }
                );
        }

        /**
         * 更新用户资料
         */
        private void updateUserInfo() {
                LogUtil.e("更新好友信息中...........");
                UserManager.getInstance().queryAndSaveCurrentContactsList(new FindListener<User>() {
                                                                                  @Override
                                                                                  public void onSuccess(final List<User> contacts) {
                                                                                          //                               返回的是去除了黑名单的好友列表
//                                       否则保存到内存中，方便取用
                                                                                          LogUtil.e("查询好友成功");
                                                                                          LogUtil.e("已保存好友资料到内存中11");
                                                                                          String uid = UserManager.getInstance().getCurrentUserObjectId();
                                                                                          MsgManager.getInstance().queryGroupTableMessage(uid, new FindListener<GroupTableMessage>() {
                                                                                                          @Override
                                                                                                          public void onSuccess(final List<GroupTableMessage> list) {
                                                                                                                  if (list != null && list.size() > 0) {
                                                                                                                           newData=new ArrayList<>();
                                                                                                                          for (GroupTableMessage message :
                                                                                                                                  list) {
//                                                                                                                                  这里进行判断出现是因为有可能建群失败的时候，未能把groupId上传上去
                                                                                                                                  if (message.getGroupId()!=null) {
                                                                                                                                          newData.add(message);
                                                                                                                                  }
                                                                                                                          }
                                                                                                                          LogUtil.e("在服务器上查询到该用户所有的群,数目:" + newData.size());
                                                                                                                          if (ChatDB.create().saveGroupTableMessage(newData)) {
                                                                                                                                  LogUtil.e("保存用户所拥有的群结构消息到数据库中成功");
//                                                                                                                                  MessageCacheManager.getInstance().addGroupTableMessage(list);
                                                                                                                          } else {
                                                                                                                                  LogUtil.e("保存用户所拥有的群结构消息到数据库中失败");
                                                                                                                          }
                                                                                                                  } else {
                                                                                                                          LogUtil.e("服务器上没有查到该用户所拥有的群");
                                                                                                                  }
                                                                                                                  runOnUiThread(new Runnable() {
                                                                                                                          @Override
                                                                                                                          public void run() {
                                                                                                                                  MsgManager.getInstance().updateUserInstallationId(new UpdateListener() {
                                                                                                                                          @Override
                                                                                                                                          public void onSuccess() {
                                                                                                                                                  dismissLoadDialog();
                                                                                                                                                  UserCacheManager.getInstance().setLogin(true);
                                                                                                                                                  MessageCacheManager.getInstance().setLogin(true);
                                                                                                                                                  BaseApplication.getAppComponent().getSharedPreferences().edit().putBoolean(ChatUtil.LOGIN_STATUS,true).apply();
                                                                                                                                                  MessageCacheManager.getInstance().addGroupTableMessage(newData);
                                                                                                                                                  if (contacts != null && contacts.size() > 0) {
                                                                                                                                                          UserCacheManager.getInstance().setContactsList(BmobUtils.list2map(contacts));
                                                                                                                                                  }
                                                                                                                                                  Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                                                                                                                  intent.putExtra("isFirstLogin", isFirstLogin);
                                                                                                                                                  startActivity(intent);
                                                                                                                                                  finish();
                                                                                                                                          }

                                                                                                                                          @Override
                                                                                                                                          public void onFailure(int i, String s) {
                                                                                                                                                  dismissLoadDialog();
                                                                                                                                                  ToastUtils.showShortToast("登录失败，请重新登录" + s + i);
                                                                                                                                                  LogUtil.e("登录失败" + s + i);
                                                                                                                                          }
                                                                                                                                  });
                                                                                                                          }
                                                                                                                  });
                                                                                                          }

                                                                                                          @Override
                                                                                                          public void onError(int i, String s) {
                                                                                                                  dismissLoadDialog();
                                                                                                                  LogUtil.e("在服务器上查询所有群结构消息失败" + s + i);
                                                                                                                  if (i!=101) {
                                                                                                                          ToastUtils.showShortToast("登录失败，请重新登录");
                                                                                                                  }else {
//                                                                                                                          这是群结构消息未创建的错误
                                                                                                                          runOnUiThread(new Runnable() {
                                                                                                                                  @Override
                                                                                                                                  public void run() {
                                                                                                                                          MsgManager.getInstance().updateUserInstallationId(new UpdateListener() {
                                                                                                                                                  @Override
                                                                                                                                                  public void onSuccess() {
                                                                                                                                                          dismissLoadDialog();
                                                                                                                                                          UserCacheManager.getInstance().setLogin(true);
                                                                                                                                                          MessageCacheManager.getInstance().setLogin(true);
                                                                                                                                                          BaseApplication.getAppComponent().getSharedPreferences().edit().putBoolean(ChatUtil.LOGIN_STATUS,true).apply();

//                                                                                                                                                          MessageCacheManager.getInstance().addGroupTableMessage(list);
                                                                                                                                                          if (contacts != null && contacts.size() > 0) {
                                                                                                                                                                  UserCacheManager.getInstance().setContactsList(BmobUtils.list2map(contacts));
                                                                                                                                                          }
                                                                                                                                                          Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                                                                                                                          intent.putExtra("isFirstLogin", isFirstLogin);
                                                                                                                                                          startActivity(intent);
                                                                                                                                                          finish();
                                                                                                                                                  }

                                                                                                                                                  @Override
                                                                                                                                                  public void onFailure(int i, String s) {
                                                                                                                                                          dismissLoadDialog();
                                                                                                                                                          ToastUtils.showShortToast("登录失败，请重新登录" + s + i);
                                                                                                                                                          LogUtil.e("登录失败" + s + i);
                                                                                                                                                  }
                                                                                                                                          });
                                                                                                                                  }
                                                                                                                          });
                                                                                                                  }
                                                                                                          }
                                                                                                  }
                                                                                          );

                                                                                  }

                                                                                  @Override
                                                                                  public void onError(int i, String s) {
                                                                                          dismissLoadDialog();
                                                                                          ToastUtils.showShortToast("登录失败，请重新登录");
                                                                                          LogUtil.e("在服务器上面查询好友失败" + s + i);
                                                                                  }
                                                                          }
                );
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                if (resultCode == Activity.RESULT_OK) {
                        switch (requestCode) {
                                case Constant.REQUEST_CODE_REGISTER:
                                        String name = data.getStringExtra("username");
                                        String password = data.getStringExtra("password");
                                        if (name != null && password != null) {
                                                passWord.setText(password);
                                                userName.setText(name);
                                        }
                                        isFirstLogin = true;
                                        break;
                        }
                }
        }

        @Override
        protected void onDestroy() {
                super.onDestroy();
        }

        @Override
        public void updateData(Object o) {
                
        }
}
