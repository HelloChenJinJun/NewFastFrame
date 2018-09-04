package com.example.chat.mvp.login;

import android.content.SharedPreferences;

import com.example.chat.base.AppBasePresenter;
import com.example.chat.base.Constant;
import com.example.chat.base.RandomData;
import com.example.chat.bean.CustomInstallation;
import com.example.chat.bean.GroupTableMessage;
import com.example.chat.bean.User;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.rxbus.event.UserInfoEvent;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.amap.api.col.t.a.i;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/27     13:50
 * QQ:         1981367757
 */

public class LoginPresenter extends AppBasePresenter<IView<Object>, DefaultModel> {
    private UserInfoEvent userInfoEvent;
    private String account, password;

    public LoginPresenter(IView<Object> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }

    public void login(String account, String password, final UserInfoEvent userInfoEvent) {
        this.userInfoEvent = userInfoEvent;
        this.account = account;
        this.password = password;
        iView.showLoading("正在登录......");
        final User user = new User();
        user.setUsername(account);
        user.setPassword(password);
        SharedPreferences sharedPreferences=BaseApplication.getAppComponent().getSharedPreferences();
        if (sharedPreferences.getString(Constant.LONGITUDE,null)!=null){
            user.setLocation(new BmobGeoPoint(Double.parseDouble(sharedPreferences.getString(Constant.LONGITUDE,null)),
                    Double.parseDouble(sharedPreferences.getString(Constant.LATITUDE,null))));
        }
        addSubscription(user.login( new SaveListener<BmobUser>() {
                                        @Override
                                        public void done(BmobUser bmobUser, BmobException e) {
                                            if (e == null) {
                                                ToastUtils.showShortToast("登录成功");
                                                LogUtil.e("登录成功");
                                                BaseApplication.getAppComponent()
                                                        .getSharedPreferences()
                                                        .edit().putBoolean(Constant.LOGIN_STATUS,true)
                                                        .apply();
//                                        登录成功之后，
//                                        检查其他设备绑定的用户，强迫其下线
                                                LogUtil.e("检查该用户绑定的其他设备.....");
                                                addSubscription(UserManager.getInstance().checkInstallation(new UpdateListener() {
                                                    @Override
                                                    public void done(BmobException e) {
                                                        if (e == null) {
                                                            iView.showLoading("正在获取好友资料.........");
                                                            updateUserInfo();
                                                        }else {
                                                            ToastUtils.showShortToast("登录失败,请重新登录" +e.toString());
                                                            CommonLogger.e("登录失败" +e.toString());
                                                            iView.hideLoading();
                                                        }
                                                    }
                                                }));
                                            }else {
                                                if (e.getErrorCode()== 101) {
//                            说明现在的账号还没有注册
                                                    if (userInfoEvent != null) {
                                                        ToastUtils.showShortToast("正在注册");
                                                        iView.showLoading("正在注册.......");
                                                        registerAccount();
                                                    }else {
//                                    正常登陆
                                                        ToastUtils.showShortToast("该账号未注册");
                                                        iView.hideLoading();
                                                    }
                                                } else {
                                                    ToastUtils.showShortToast("登录失败" + e.toString());
                                                    iView.hideLoading();
                                                }
                                            }
                                        }
                                    }
        ));
    }


    /**
     * 更新用户资料
     */
    private void updateUserInfo() {
        addSubscription(UserManager.getInstance().queryAndSaveCurrentContactsList(new FindListener<User>() {
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

        ));
    }


    private void jumpToHome() {
        iView.hideLoading();
       iView.updateData(null);
    }


    private void registerAccount() {
//        RandomData.initAllRanDomData();
        User user = new User();
//                                默认注册为男性
//                                 设备类型
        user.setDeviceType("android");
//                                与设备ID绑定
        user.setInstallId(new CustomInstallation().getInstallationId());
        if (userInfoEvent!=null) {
            user.setNick(userInfoEvent.getNick());
            user.setAvatar(userInfoEvent.getAvatar());
            user.setSex(userInfoEvent.getSex());
            user.setName(userInfoEvent.getName());
            user.setCollege(userInfoEvent.getCollege());
            user.setMajor(userInfoEvent.getMajor());
            user.setSchool("中国地质大学（武汉）");
            user.setClassNumber(userInfoEvent.getClassNumber());
            user.setYear(account.substring(0,4));
            user.setEducation(userInfoEvent.getStudentType());
        }else {
            user.setNick(RandomData.getRandomNick());
            user.setAvatar(RandomData.getRandomAvatar());
            user.setSex(true);
        }
        user.setPw(password);
        user.setUsername(account);
        user.setPassword(password);
        user.setTitleWallPaper(RandomData.getRandomTitleWallPaper());
        user.setWallPaper(RandomData.getRandomWallPaper());
        user.signUp( new SaveListener<User>() {
            @Override
            public void done(User s, BmobException e) {
                iView.hideLoading();
                if (e == null) {
                    ToastUtils.showShortToast("注册成功，登录中...........");
                    BaseApplication.getAppComponent()
                            .getSharedPreferences().edit().putBoolean(ConstantUtil.FIRST_STATUS, true)
                            .apply();
                    login(account, password, userInfoEvent);
                }else {
                    ToastUtils.showShortToast("注册失败" + s + i);
                }
            }
        });
    }
}
