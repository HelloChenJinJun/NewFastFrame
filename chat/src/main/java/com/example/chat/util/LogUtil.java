package com.example.chat.util;

import android.util.Log;

import com.example.chat.base.Constant;
import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.GroupChatMessage;
import com.example.chat.bean.GroupTableMessage;
import com.example.chat.bean.User;

/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/11      21:17
 * QQ:             1981367757
 */
public class LogUtil {
        private static final String TAG = "TestChat";

        public static void v(String tag, String msg) {
                if (Constant.DEBUG) {
                        Log.v(tag, msg);
                }
        }


        public static void i(String msg) {
                if (Constant.DEBUG) {
                        Log.i(TAG, msg);
                }
        }

        public static void e(String msg) {
                if (Constant.DEBUG) {
                        Log.e(TAG, msg);
                }
        }


        public static void e(GroupTableMessage message) {
                if (Constant.DEBUG) {
                        LogUtil.e("群结构消息格式如下:\n");
                        LogUtil.e("groupId:" + message.getGroupId() + "\n"
                                + "toId" + message.getToId() + "\n"
                                + "groupName:" + message.getGroupName() + "\n"
                                + "groupAvatar" + message.getGroupAvatar() + "\n"
                                + "groupDescription:" + message.getGroupDescription() + "\n"
                                + "creatorId" + message.getCreatorId() + "\n"
                                + "sendStatus" + message.getSendStatus() + "\n"
                                + "readStatus" + message.getReadStatus() + "\n"
                                + "createTime" + message.getCreatedTime() + "\n"
                                + "notification" + message.getNotification() + "\n"
                                + "objectId" + message.getObjectId());
                        LogUtil.e("群消息成员如下:" + "\n");
                        for (int i = 0; i < message.getGroupNumber().size(); i++) {
                                LogUtil.e(message.getGroupNumber().get(i) + "\n");
                        }
                }
        }

        public static void e(String tag, String msg) {
                if (Constant.DEBUG) {
                        Log.e(tag, msg);
                }
        }

        public static void e(GroupChatMessage groupChatMessage) {
                if (Constant.DEBUG) {
                        LogUtil.e(groupChatMessage.toString());
                }
        }

        public static void e(ChatMessage chatMessage) {
                if (Constant.DEBUG) {
                        LogUtil.e(chatMessage.toString());
                }
        }




        public static void e(User user) {
                if (Constant.DEBUG) {
                        LogUtil.e("用户信息如下");
                        LogUtil.e("avatar" + user.getAvatar() + "\n"
                                + "name" + user.getUsername() + "\n"
                                + "nick" + user.getNick() + "\n"
                                + "address" + user.getAddress() + "\n"
                                + "signature" + user.getSignature() + "\n"
                                + "birthday" + user.getBirthDay() + "\n"
                                + "phone" + user.getMobilePhoneNumber() + "\n"
                                + "email" + user.getEmail() + "\n"
                                + "gender" + user.isSex() + "\n");
                        if (user.getLocation() != null) {
                                LogUtil.e("location" + user.getLocation().getLongitude() + "&" + user.getLocation()
                                        .getLatitude());
                        }
                }
        }
}
