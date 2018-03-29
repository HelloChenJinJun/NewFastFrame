package com.example.chat.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.chat.base.Constant;
import com.example.chat.events.NetStatusEvent;
import com.example.chat.util.TimeUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/12      20:40
 * QQ:             1981367757
 */

public class NetWorkChangedReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constant.NETWORK_CONNECTION_CHANGE)) {
                        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                        if (networkInfo != null) {
                                if (networkInfo.isConnected() && BaseApplication
                                        .getAppComponent().getSharedPreferences().getLong(Constant.DELTA_TIME, 0L) == 0L) {
                                        TimeUtil.getServerTime();
                                }
                                RxBusManager.getInstance().post(new NetStatusEvent(networkInfo.isConnected(), networkInfo.getType()));
                        } else {
                                CommonLogger.e("当前没有网络连接");
                                RxBusManager.getInstance().post(new NetStatusEvent(false, 0));
                        }
                }
        }
}
