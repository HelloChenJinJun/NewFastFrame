package com.example.chat.mvp.login;

import android.content.SharedPreferences;

import com.example.chat.base.AppBasePresenter;
import com.example.chat.base.ConstantUtil;
import com.example.chat.bean.CustomInstallation;
import com.example.chat.bean.GroupTableMessage;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.chat.User;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/27     13:50
 * QQ:         1981367757
 */

public class LoginPresenter extends AppBasePresenter<IView<Object>, DefaultModel> {
    private String account, password;

    public LoginPresenter(IView<Object> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }

    public void login(String account, String password) {
        this.account = account;
        this.password = password;
        iView.showLoading("正在登录......");
        final User user = new User();
        user.setUsername(account);
        user.setPassword(password);
        SharedPreferences sharedPreferences = BaseApplication.getAppComponent().getSharedPreferences();
        if (sharedPreferences.getString(ConstantUtil.LONGITUDE, null) != null) {
            user.setLocation(new BmobGeoPoint(Double.parseDouble(sharedPreferences.getString(ConstantUtil.LONGITUDE, null)),
                    Double.parseDouble(sharedPreferences.getString(ConstantUtil.LATITUDE, null))));
        }
        addSubscription(user.login(new SaveListener<BmobUser>() {
                                       @Override
                                       public void done(BmobUser bmobUser, BmobException e) {
                                           if (e == null) {
                                               ToastUtils.showShortToast("登录成功");
                                               LogUtil.e("登录成功");
                                               BaseApplication.getAppComponent()
                                                       .getSharedPreferences()
                                                       .edit().putBoolean(ConstantUtil.LOGIN_STATUS, true)
                                                       .apply();
                                               //                                        登录成功之后，
                                               //                                        检查其他设备绑定的用户，强迫其下线
                                               LogUtil.e("检查该用户绑定的其他设备.....");
                                               addSubscription(UserManager.getInstance().checkInstallation(new UpdateListener() {
                                                   @Override
                                                   public void done(BmobException e) {
                                                       UserManager.getInstance().updateUserInfo(ConstantUtil.INSTALL_ID,new CustomInstallation().getInstallationId()
                                                       ,null);
                                                       if (e == null) {
                                                           iView.showLoading("正在获取好友资料.........");
                                                           updateUserInfo();
                                                       } else {
                                                           ToastUtils.showShortToast("登录失败,请重新登录" + e.toString());
                                                           CommonLogger.e("登录失败" + e.toString());
                                                           iView.hideLoading();
                                                       }
                                                   }
                                               }));
                                           } else {
                                               ToastUtils.showShortToast("登录失败" + e.toString());
                                               iView.hideLoading();
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
                                                                                                                      List<GroupTableMessage> newData = new ArrayList<>();
                                                                                                                      for (GroupTableMessage message :
                                                                                                                              list) {
                                                                                                                          //                                                                                                                                  这里进行判断出现是因为有可能建群失败的时候，未能把groupId上传上去
                                                                                                                          if (message.getGroupId() != null) {
                                                                                                                              newData.add(message);
                                                                                                                          }
                                                                                                                      }
                                                                                                                      UserDBManager.getInstance().addOrUpdateGroupTable(newData);
                                                                                                                  } else {
                                                                                                                      LogUtil.e("服务器上没有查到该用户所拥有的群");
                                                                                                                  }
                                                                                                              } else {
                                                                                                                  LogUtil.e("在服务器上查询所有群结构消息失败" + e.toString());
                                                                                                                  if (e.getErrorCode() == 101) {
                                                                                                                      CommonLogger.e("没有创立群表");
                                                                                                                  }
                                                                                                              }
                                                                                                              jumpToHome();
                                                                                                          }
                                                                                                      }

                                                                                              );
                                                                                          } else {
                                                                                              CommonLogger.e("查询好友失败" + e.toString());
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
}
