package com.example.chat.mvp.main.friends;

import com.example.chat.base.AppBasePresenter;
import com.example.chat.bean.User;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.commonlibrary.bean.chat.UserEntity;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/16     23:45
 * QQ:         1981367757
 */

public class FriendsPresenter extends AppBasePresenter<IView<List<UserEntity>>,DefaultModel> {
    public FriendsPresenter(IView<List<UserEntity>> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }

    public void getAllFriends(boolean isRefresh) {
        if (isRefresh) {
            iView.showLoading("加载中");
        }
        addSubscription(UserManager.getInstance().queryAndSaveCurrentContactsList(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    ToastUtils.showShortToast("获取服务器所有好友成功");
                    
                    List<UserEntity>  userEntityList=new ArrayList<>(list.size());
                    for (User item :
                            list) {
                        userEntityList.add(UserManager.getInstance().cover(item));
                    }
                    UserDBManager.getInstance().addOrUpdateUser(userEntityList);
                    iView.updateData(userEntityList);
                }else {
                    ToastUtils.showShortToast("获取服务器所有好友失败"+e.toString());
                    List<UserEntity> entityList =UserDBManager.getInstance()
                            .getAllFriend();
                    iView.updateData(entityList);
                }
                iView.hideLoading();
            }
        }));
    }
}
