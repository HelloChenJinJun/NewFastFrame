package com.example.commonlibrary.manager;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.utils.Constant;

import androidx.core.app.NotificationCompat;

/**
 * 项目名称:    zhuayu_android
 * 创建人:      陈锦军
 * 创建时间:    2018/10/29     20:42
 */
public class NotificationManager {


    private android.app.NotificationManager mManager;


    private static NotificationManager instance;

    public static NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }


    private NotificationManager() {
        mManager = (android.app.NotificationManager) BaseApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
    }


    /**
     * 在通知栏展示通知
     *
     * @param context  context
     * @param userName 用户名
     * @param icon     通知栏图标
     * @param content  通知栏内容
     */
    public void showNotification(Context context, String userName, int icon, CharSequence content, Class<? extends Activity> targetClass) {
        boolean isAllowPushNotify = BaseApplication.getAppComponent()
                .getSharedPreferences().getBoolean(Constant.PUSH_NOTIFY, true);
        boolean isAllowVoice = BaseApplication.getAppComponent()
                .getSharedPreferences().getBoolean(Constant.VOICE_STATUS, true);
        boolean isAllowVibrate = BaseApplication.getAppComponent().getSharedPreferences().getBoolean(Constant.VIBRATE_STATUS, true);
        if (isAllowPushNotify) {
            notify(isAllowVibrate, isAllowVoice, context, userName, icon, content, targetClass);
        }
    }


    /**
     * 发送通知到通知栏
     *
     * @param isAllowVibrate 是否允许振动
     * @param isAllowVoice   是否允许声音
     * @param context        context
     * @param title          标题
     * @param icon           图标
     * @param content        内容
     * @param targetClass    目标Activity
     */
    public void notify(boolean isAllowVibrate, boolean isAllowVoice, Context context, String title, int icon, CharSequence content, Class<? extends Activity> targetClass) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(icon);
        builder.setContentText(content);
        builder.setContentTitle(title);
        builder.setTicker(title);
        builder.setAutoCancel(true);
        if (isAllowVoice) {
            builder.setDefaults(Notification.DEFAULT_SOUND);
        }
        if (isAllowVibrate) {
            builder.setDefaults(Notification.DEFAULT_VIBRATE);
        }
        if (targetClass != null) {
            Intent intent = new Intent(context, targetClass);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
        }
        mManager.notify(1, builder.build());
    }
}
