package com.example.manager;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.utils.CommonLogger;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/5/18     13:20
 */

public class ManagerApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "06cefecaee1c01cac71cb2f7de18dc9c");
        BmobInstallationManager.getInstance().initialize(new InstallationListener<BmobInstallation>() {
            @Override
            public void done(BmobInstallation bmobInstallation, BmobException e) {
                if (e == null) {
                    CommonLogger.e("初始化成功");
                }else {
                    CommonLogger.e("初始化失败"+e.toString());
                }
            }
        });
        BmobPush.startWork(this);
    }
}
