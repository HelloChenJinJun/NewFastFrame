package com.example.commonlibrary.keeplive.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.keeplive.onepix.OnePxActivity;
import com.example.commonlibrary.keeplive.service.KeepLiveService;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.rxbus.event.NetStatusEvent;
import com.example.commonlibrary.rxbus.event.SystemNotificationEvent;
import com.example.commonlibrary.utils.AppUtil;
import com.example.commonlibrary.utils.CommonLogger;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/10     14:42
 */
public class ScreenNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) {
            return;
        }
        switch (action) {
            case Intent.ACTION_SCREEN_ON:
                if (BaseApplication.getInstance() != null) {
                    BaseApplication.getAppComponent().getActivityManager().finish(OnePxActivity.class);
                }
                break;
            case Intent.ACTION_SCREEN_OFF:
                if (BaseApplication.getInstance() != null) {
                    BaseApplication.getAppComponent().getActivityManager().start(OnePxActivity.class, false);
                }
                break;
            case Intent.ACTION_USER_PRESENT:
                break;
            case "android.net.conn.CONNECTIVITY_CHANGE":
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = null;
                if (connectivityManager != null) {
                    networkInfo = connectivityManager.getActiveNetworkInfo();
                }
                if (networkInfo != null) {
                    RxBusManager.getInstance().post(new NetStatusEvent(networkInfo.isConnected(), networkInfo.getType()));
                } else {
                    CommonLogger.e("当前没有网络连接");
                    RxBusManager.getInstance().post(new NetStatusEvent(false, 0));
                }
                if (!AppUtil.isAppAlive()) {
                    String serviceName = KeepLiveService.getKeepLiveService(context);
                    if (!TextUtils.isEmpty(serviceName)) {
                        try {
                            Intent intent1 = new Intent(context, Class.forName(serviceName));
                            context.startService(intent1);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case Intent.ACTION_BOOT_COMPLETED:
                if (!AppUtil.isAppAlive()) {
                    String serviceName = KeepLiveService.getKeepLiveService(context);
                    if (!TextUtils.isEmpty(serviceName)) {
                        try {
                            Intent intent1 = new Intent(context, Class.forName(serviceName));
                            context.startService(intent1);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
        RxBusManager.getInstance().post(new SystemNotificationEvent(action));
    }
}
