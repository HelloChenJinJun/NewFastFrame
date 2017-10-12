package com.example.chat.manager;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.example.chat.R;
import com.example.chat.base.Constant;
import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.GroupChatMessage;
import com.example.chat.bean.GroupTableMessage;
import com.example.chat.ui.MainActivity;
import com.example.chat.util.ChatUtil;
import com.example.chat.util.FaceTextUtil;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.BaseApplication;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/10/12      20:23
 * QQ:             1981367757
 */
public class ChatNotificationManager {
        private static final Object LOCK = new Object();
        private NotificationManager sNotificationManager;
        private static ChatNotificationManager instance;

        private ChatNotificationManager(Context context) {
                sNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }


        /**
         * 单例模式
         * 双重锁定
         *
         * @param context context
         * @return 单例
         */
        public static ChatNotificationManager getInstance(Context context) {
                if (instance == null) {
                        synchronized (LOCK) {
                                if (instance == null) {
                                        instance = new ChatNotificationManager(context);
                                }
                        }
                }
                return instance;
        }


        public void sendChatMessageNotification(ChatMessage chatMessage, Context context) {
                LogUtil.e("接受成功");
                LogUtil.e(chatMessage);
                //                                                                        这里进行监听回调到主页面
                String tag = chatMessage.getTag();
                if (tag == null || tag.equals("")) {
                        if (chatMessage.getMsgType().equals(Constant.TAG_MSG_TYPE_IMAGE)) {
                                showNotification(Constant.NOTIFICATION_TAG_MESSAGE, context, chatMessage.getBelongNick(), R.mipmap.ic_launcher, "[图片]", MainActivity.class);
                        } else if (chatMessage.getMsgType().equals(Constant.TAG_MSG_TYPE_LOCATION)) {
                                showNotification(Constant.NOTIFICATION_TAG_MESSAGE, context, chatMessage.getBelongNick(), R.mipmap.ic_launcher, "[位置]", MainActivity.class);
                        } else if (chatMessage.getMsgType().equals(Constant.TAG_MSG_TYPE_VOICE)) {
                                showNotification(Constant.NOTIFICATION_TAG_MESSAGE, context, chatMessage.getBelongNick(), R.mipmap.ic_launcher, "[语音]", MainActivity.class);
                        } else {
                                showNotification(Constant.NOTIFICATION_TAG_MESSAGE, context, chatMessage.getBelongNick(), R.mipmap.ic_launcher, FaceTextUtil.toSpannableString(context, chatMessage.getContent()), MainActivity.class);
                        }

                } else if (tag.equals(Constant.TAG_AGREE)) {
                        showNotification(Constant.NOTIFICATION_TAG_AGREE, context, chatMessage.getBelongNick(), R.mipmap.ic_launcher, chatMessage.getBelongUserName() + "已同意添加你为好友", MainActivity.class);
                } else if (tag.equals(Constant.TAG_ADD_FRIEND)) {
                        showNotification(Constant.NOTIFICATION_TAG_ADD, context, chatMessage.getBelongNick(), R.mipmap.ic_launcher, chatMessage.getBelongUserName() + "请求添加你为好友", MainActivity.class);
                }
        }


        public void sendGroupMessageNotification(final GroupChatMessage message, final Context context) {
                GroupTableMessage groupTableMessage = MessageCacheManager.getInstance().getGroupTableMessage(message.getGroupId());
                realSendGroupMessageNotification(groupTableMessage, message, context);
        }


        private void realSendGroupMessageNotification(GroupTableMessage groupTableMessage, GroupChatMessage groupChatMessage, Context context) {

                if (groupChatMessage.getMsgType().equals(Constant.TAG_MSG_TYPE_IMAGE)) {
                        showNotification(Constant.NOTIFICATION_TAG_MESSAGE, context, groupTableMessage.getGroupName(), R.mipmap.ic_launcher, groupChatMessage.getBelongNick() + "：[图片]", MainActivity.class);
                } else if (groupChatMessage.getMsgType().equals(Constant.TAG_MSG_TYPE_LOCATION)) {
                        showNotification(Constant.NOTIFICATION_TAG_MESSAGE, context, groupTableMessage.getGroupName(), R.mipmap.ic_launcher, groupChatMessage.getBelongNick() + "：[位置]", MainActivity.class);
                } else if (groupChatMessage.getMsgType().equals(Constant.TAG_MSG_TYPE_VOICE)) {
                        showNotification(Constant.NOTIFICATION_TAG_MESSAGE, context, groupTableMessage.getGroupName(), R.mipmap.ic_launcher, groupChatMessage.getBelongNick() + "：[语音]", MainActivity.class);
                } else {
                        showNotification(Constant.NOTIFICATION_TAG_MESSAGE, context, groupTableMessage.getGroupName(), R.mipmap.ic_launcher, groupChatMessage.getBelongNick() + "：" + FaceTextUtil.toSpannableString(context, groupChatMessage.getContent()), MainActivity.class);
                }
        }


        /**
         * 在通知栏展示通知
         *
         * @param notificationTagAdd 通知消息的类型标签
         * @param context            context
         * @param userName           用户名
         * @param icon               通知栏图标
         * @param content            通知栏内容
         */
        public void showNotification(String notificationTagAdd, Context context, String userName, int icon, CharSequence content, Class<? extends Activity> targetClass) {
                boolean isAllowPushNotify = BaseApplication.getAppComponent()
                        .getSharedPreferences().getBoolean(ChatUtil.PUSH_NOTIFY,false);
                boolean isAllowVoice = BaseApplication.getAppComponent()
                        .getSharedPreferences().getBoolean(ChatUtil.VOICE_STATUS,false);
                boolean isAllowVibrate = BaseApplication.getAppComponent().getSharedPreferences().getBoolean(ChatUtil.VIBRATE_STATUS,false);
                if (isAllowPushNotify) {
                        ChatNotificationManager.getInstance(context).notify(notificationTagAdd, null, isAllowVibrate, isAllowVoice, context, userName, icon, content, targetClass);
                        LogUtil.e("发送通知到通知栏啦啦啦");
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
        public void notify(String tag, String groupId, boolean isAllowVibrate, boolean isAllowVoice, Context context, String title, int icon, CharSequence content, Class<? extends Activity> targetClass) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setSmallIcon(icon);
                builder.setContentText(content);
                builder.setContentTitle(title);
                builder.setTicker(title);
                builder.setAutoCancel(true);
                if (isAllowVibrate) {
                        builder.setDefaults(Notification.DEFAULT_VIBRATE);
                }
                if (isAllowVoice) {
                        builder.setDefaults(Notification.DEFAULT_SOUND);
                }
                LogUtil.e("设置通知123");
                if (targetClass!=null) {
                        Intent intent = new Intent(context, targetClass);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra(Constant.NOTIFICATION_TAG, tag);
                        if (groupId != null) {
                                intent.putExtra("groupId", groupId);
                        }
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentIntent(pendingIntent);
                }
                sNotificationManager.notify(Constant.NOTIFY_ID, builder.build());
                sNotificationManager.notify(Constant.NOTIFY_ID, builder.build());
        }
}
