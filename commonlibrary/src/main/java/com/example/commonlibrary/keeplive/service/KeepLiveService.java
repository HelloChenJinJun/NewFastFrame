package com.example.commonlibrary.keeplive.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.IBinder;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.utils.CommonLogger;

import androidx.annotation.Nullable;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/10     14:05
 */
public class KeepLiveService extends Service {


    public static final int NOTIFICATION_ID = 10;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        CommonLogger.e("保活服务启动");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            startService(new Intent(this, InnerService.class));
        } else {
            //            透明通知栏
            startForeground(NOTIFICATION_ID, new Notification());
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.cancel(NOTIFICATION_ID);
            }
        }
        startService(new Intent(BaseApplication.getInstance(), getClass()));
    }

    private class InnerService extends Service {

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(NOTIFICATION_ID, new Notification());
            stopForeground(true);
            // 移除DaemonService弹出的通知
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.cancel(NOTIFICATION_ID);
            }
            return super.onStartCommand(intent, flags, startId);
            // 任务完成，终止自己
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    public static String getKeepLiveService(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SERVICES);
            ServiceInfo[] activityInfos = packageInfo.services;
            if (null != activityInfos && activityInfos.length > 0) {
                for (int i = 0; i < activityInfos.length; i++) {
                    String receiverName = activityInfos[i].name;
                    Class superClazz = Class.forName(receiverName).getSuperclass();
                    int count = 1;
                    while (null != superClazz) {
                        if (superClazz.getName().equals("java.lang.Object")) {
                            break;
                        }
                        if (superClazz.getName().equals(KeepLiveService.class.getName())) {
                            CommonLogger.e("receiverName : " + receiverName);
                            return receiverName;
                        }
                        if (count > 20) {
                            //用来做容错，如果20个基类还不到Object直接跳出防止while死循环
                            break;
                        }
                        count++;
                        superClazz = superClazz.getSuperclass();
                        CommonLogger.e("superClazz : " + superClazz);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
