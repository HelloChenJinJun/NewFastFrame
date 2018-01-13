package com.example.chat.mvp.login;

import android.content.Intent;

import com.example.chat.base.Constant;
import com.example.chat.base.RandomData;
import com.example.chat.bean.CustomInstallation;
import com.example.chat.bean.GroupTableMessage;
import com.example.chat.bean.User;
import com.example.chat.db.ChatDB;
import com.example.chat.manager.LocationManager;
import com.example.chat.manager.MessageCacheManager;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserCacheManager;
import com.example.chat.manager.UserManager;
import com.example.chat.ui.EditUserInfoActivity;
import com.example.chat.ui.HomeActivity;
import com.example.chat.ui.LoginActivity;
import com.example.chat.util.BmobUtils;
import com.example.chat.util.ChatUtil;
import com.example.chat.util.CommonUtils;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.rxbus.event.UserInfoEvent;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobInstallation;
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

public class LoginPresenter extends RxBasePresenter<IView<Object>, LoginModel> {
    private UserInfoEvent userInfoEvent;
    private String account, password;
    private List<GroupTableMessage> newData;

    public LoginPresenter(IView<Object> iView, LoginModel baseModel) {
        super(iView, baseModel);
    }

    public void login(String account, String password, UserInfoEvent userInfoEvent) {
        if (newData != null) {
            newData.clear();
        }
        this.userInfoEvent = userInfoEvent;
        this.account = account;
        this.password = password;
        iView.showLoading("正在登录......");
        final User user = new User();
        user.setUsername(account);
        user.setPassword(password);
        if (LocationManager.getInstance().getLatitude() != 0 || LocationManager.getInstance().getLongitude() != 0) {
            user.setLocation(new BmobGeoPoint(LocationManager.getInstance().getLongitude(), LocationManager.getInstance()
                    .getLatitude()));
        }
        user.login( new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if (e == null) {
                            iView.hideLoading();
                            ToastUtils.showShortToast("登录成功");
                            LogUtil.e("登录成功");
//                                        登录成功之后，
//                                        检查其他设备绑定的用户，强迫其下线
                            LogUtil.e("检查该用户绑定的其他设备.....");
                            UserManager.getInstance().checkInstallation(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        ToastUtils.showShortToast("登录成功1");
                                        LogUtil.e("登录成功");
                                        iView.showLoading("正在获取好友资料.........");
                                        updateUserInfo();
                                    }else {
                                        ToastUtils.showShortToast("登录失败,请重新登录" +e.toString());
                                        LogUtil.e("登录失败" +e.toString());
                                    }
                                }
                            });
                        }else {
                            ToastUtils.showShortToast("登录失败" + e.toString());
                            if (e.getErrorCode()== 101) {
                                ToastUtils.showShortToast("正在注册");
//                            说明现在的账号还没有注册
                                registerAccount();
                            } else {
                                iView.hideLoading();
                            }
                        }
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
                                                                      public void done(final List<User> contacts, BmobException e) {
                                                                          if (e == null) {
                                                                              //                               返回的是去除了黑名单的好友列表
//                                       否则保存到内存中，方便取用
                                                                              LogUtil.e("查询好友成功");
                                                                              LogUtil.e("已保存好友资料到内存中11");
                                                                              String uid = UserManager.getInstance().getCurrentUserObjectId();
                                                                              MsgManager.getInstance().queryGroupTableMessage(uid, new FindListener<GroupTableMessage>() {
                                                                                  @Override
                                                                                  public void done(List<GroupTableMessage> list, BmobException e) {
                                                                                      if (e == null) {
                                                                                          if (list != null && list.size() > 0) {
                                                                                              newData = new ArrayList<>();
                                                                                              for (GroupTableMessage message :
                                                                                                      list) {
//                                                                                                                                  这里进行判断出现是因为有可能建群失败的时候，未能把groupId上传上去
                                                                                                  if (message.getGroupId() != null) {
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

                                                                                          MsgManager.getInstance().updateUserInstallationId(new UpdateListener() {
                                                                                              @Override
                                                                                              public void done(BmobException e) {
                                                                                                  if (e == null) {
                                                                                                      iView.hideLoading();
                                                                                                      UserCacheManager.getInstance().setLogin(true);
                                                                                                      MessageCacheManager.getInstance().setLogin(true);
                                                                                                      BaseApplication.getAppComponent().getSharedPreferences().edit().putBoolean(ChatUtil.LOGIN_STATUS, true).apply();
                                                                                                      MessageCacheManager.getInstance().addGroupTableMessage(newData);
                                                                                                      if (contacts != null && contacts.size() > 0) {
                                                                                                          UserCacheManager.getInstance().setContactsList(BmobUtils.list2map(contacts));
                                                                                                      }
                                                                                                      jumpToMain();
                                                                                                  }else {
                                                                                                      iView.hideLoading();
                                                                                                      ToastUtils.showShortToast("登录失败，请重新登录" +e.toString());
                                                                                                      LogUtil.e("登录失败" +e.toString());
                                                                                                  }
                                                                                              }

                                                                                          });
                                                                                      }else {
                                                                                          iView.hideLoading();
                                                                                          LogUtil.e("在服务器上查询所有群结构消息失败" +e.toString());
                                                                                          if (e.getErrorCode()!= 101) {
                                                                                              ToastUtils.showShortToast("登录失败，请重新登录");
                                                                                          } else {
//                                                                                                                          这是群结构消息未创建的错误
                                                                                              MsgManager.getInstance().updateUserInstallationId(new UpdateListener() {
                                                                                                  @Override
                                                                                                  public void done(BmobException e) {
                                                                                                      if (e == null) {
                                                                                                          iView.hideLoading();
                                                                                                          UserCacheManager.getInstance().setLogin(true);
                                                                                                          MessageCacheManager.getInstance().setLogin(true);
                                                                                                          BaseApplication.getAppComponent().getSharedPreferences().edit().putBoolean(ChatUtil.LOGIN_STATUS, true).apply();
//                                                                                                                                                          MessageCacheManager.getInstance().addGroupTableMessage(list);
                                                                                                          if (contacts != null && contacts.size() > 0) {
                                                                                                              UserCacheManager.getInstance().setContactsList(BmobUtils.list2map(contacts));
                                                                                                          }
                                                                                                          jumpToMain();
                                                                                                      }else {
                                                                                                          iView.hideLoading();
                                                                                                          ToastUtils.showShortToast("登录失败，请重新登录" +e.toString());
                                                                                                          LogUtil.e("登录失败" +e.toString());
                                                                                                      }
                                                                                                  }

                                                                                              });

                                                                                          }
                                                                                      }
                                                                                  }

                                                                                      }
                                                                              );
                                                                          }else {
                                                                              iView.hideLoading();
                                                                              ToastUtils.showShortToast("登录失败，请重新登录");
                                                                              LogUtil.e("在服务器上面查询好友失败" +e.toString());
                                                                          }
                                                                      }

                                                                  }
        );
    }


    private void jumpToMain() {
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
        user.setSortedKey(CommonUtils.getSortedKey(user.getNick()));
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
