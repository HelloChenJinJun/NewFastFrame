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
import com.example.chat.bean.MessageContent;
import com.example.chat.bean.User;
import com.example.chat.mvp.main.HomeActivity;
import com.example.chat.util.FaceTextUtil;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.chat.GroupTableEntity;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ConstantUtil;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


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
                //                                                                        这里进行监听回调到主页面
                int messageType = chatMessage.getMessageType();
                UserManager.getInstance().findUserById(chatMessage.getBelongId(), new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                                if (list != null && list.size() > 0) {
                                        if (messageType==ChatMessage.MESSAGE_TYPE_NORMAL) {
                                                if (chatMessage.getContentType().equals(Constant.TAG_MSG_TYPE_IMAGE)) {
                                                        showNotification(Constant.NOTIFICATION_TAG_MESSAGE, context, list.get(0).getNick(), R.mipmap.ic_launcher, "[图片]", HomeActivity.class);
                                                } else if (chatMessage.getContentType().equals(Constant.TAG_MSG_TYPE_LOCATION)) {
                                                        showNotification(Constant.NOTIFICATION_TAG_MESSAGE, context, list.get(0).getNick(), R.mipmap.ic_launcher, "[位置]", HomeActivity.class);
                                                } else if (chatMessage.getContentType().equals(Constant.TAG_MSG_TYPE_VOICE)) {
                                                        showNotification(Constant.NOTIFICATION_TAG_MESSAGE, context, list.get(0).getNick(), R.mipmap.ic_launcher, "[语音]", HomeActivity.class);
                                                } else if (chatMessage.getContentType().equals(Constant.TAG_MSG_TYPE_VIDEO)){
                                                        showNotification(Constant.NOTIFICATION_TAG_MESSAGE, context, list.get(0).getNick(), R.mipmap.ic_launcher, "[视频]", HomeActivity.class);
                                                }else {
                                                        String content=BaseApplication.getAppComponent().getGson().fromJson(chatMessage.getContent(),MessageContent.class).getContent();
                                                        showNotification(Constant.NOTIFICATION_TAG_MESSAGE, context, list.get(0).getNick(), R.mipmap.ic_launcher, FaceTextUtil.toSpannableString(context, content), HomeActivity.class);
                                                }
                                        } else if (messageType==ChatMessage.MESSAGE_TYPE_AGREE) {
                                                showNotification(Constant.NOTIFICATION_TAG_AGREE, context,list.get(0).getNick(), R.mipmap.ic_launcher, list.get(0).getName() + "已同意添加你为好友", HomeActivity.class);
                                        } else if (messageType==ChatMessage.MESSAGE_TYPE_ADD) {
                                                showNotification(Constant.NOTIFICATION_TAG_ADD, context, list.get(0).getNick(), R.mipmap.ic_launcher, list.get(0).getName() + "请求添加你为好友", HomeActivity.class);
                                        }
                                }
                        }
                });
        }


        public void sendGroupMessageNotification(final GroupChatMessage message, final Context context) {
                GroupTableEntity groupTableEntity =UserDBManager.getInstance()
                        .getGroupTableEntity(message.getGroupId());
                realSendGroupMessageNotification(groupTableEntity.getGroupName(), message, context);
        }


        private void realSendGroupMessageNotification(String name, GroupChatMessage groupChatMessage, Context context) {
               UserManager.getInstance().findUserById(groupChatMessage.getBelongId(), new FindListener<User>() {
                       @Override
                       public void done(List<User> list, BmobException e) {
                               if (list != null && list.size() > 0) {
                                       if (groupChatMessage.getContentType().equals(Constant.TAG_MSG_TYPE_IMAGE)) {
                                               showNotification(Constant.NOTIFICATION_TAG_MESSAGE, context, name, R.mipmap.ic_launcher, list.get(0).getName() + "：[图片]", HomeActivity.class);
                                       } else if (groupChatMessage.getContentType().equals(Constant.TAG_MSG_TYPE_LOCATION)) {
                                               showNotification(Constant.NOTIFICATION_TAG_MESSAGE, context, name, R.mipmap.ic_launcher, list.get(0).getName() + "：[位置]", HomeActivity.class);
                                       } else if (groupChatMessage.getContentType().equals(Constant.TAG_MSG_TYPE_VOICE)) {
                                               showNotification(Constant.NOTIFICATION_TAG_MESSAGE, context, name, R.mipmap.ic_launcher, list.get(0).getName() + "：[语音]", HomeActivity.class);
                                       } else {
                                               showNotification(Constant.NOTIFICATION_TAG_MESSAGE, context, name, R.mipmap.ic_launcher, list.get(0).getName() + "：" + FaceTextUtil.toSpannableString(context, groupChatMessage.getContent()), HomeActivity.class);
                                       }
                               }
                       }
               });
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
                        .getSharedPreferences().getBoolean(Constant.PUSH_NOTIFY,true);
                boolean isAllowVoice = BaseApplication.getAppComponent()
                        .getSharedPreferences().getBoolean(Constant.VOICE_STATUS,true);
                boolean isAllowVibrate = BaseApplication.getAppComponent().getSharedPreferences().getBoolean(Constant.VIBRATE_STATUS,true);
                if (isAllowPushNotify) {
                        ChatNotificationManager.getInstance(context).notify(notificationTagAdd, null, isAllowVibrate, isAllowVoice, context, userName, icon, content, targetClass);
                        CommonLogger.e("发送通知到通知栏啦啦啦");
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
                if (targetClass!=null) {
                        Intent intent = null;
                        if (BaseApplication.getAppComponent()
                                .getSharedPreferences()
                                .getBoolean(ConstantUtil.IS_ALONE,true)) {
                                intent = new Intent(context, targetClass);
                        }else {
                                intent=new Intent("custom.activity.action.main");
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra(Constant.NOTIFICATION_TAG, tag);
                        if (groupId != null) {
                                intent.putExtra(Constant.GROUP_ID, groupId);
                        }
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentIntent(pendingIntent);
                }
                sNotificationManager.notify(Constant.NOTIFY_ID, builder.build());
        }
}
