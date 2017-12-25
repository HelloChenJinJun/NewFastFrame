package com.example.chat.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.chat.base.Constant;
import com.example.chat.ui.HomeActivity;
import com.example.chat.util.CommonUtils;
import com.example.chat.util.LogUtil;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/11      13:10
 * QQ:             1981367757
 */

public class ProcessNotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
                LogUtil.e("接受到发来的通知，这里统一处理");
                Intent intent1;
                if (CommonUtils.isAppAlive(context)) {
                        intent1 = new Intent(context, HomeActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                } else {
                        intent1 = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                }
                intent1.putExtra(Constant.NOTIFICATION_TAG, intent1.getStringExtra(Constant.NOTIFICATION_TAG));
                context.startActivity(intent1);
        }
}
