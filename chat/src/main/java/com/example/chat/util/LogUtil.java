package com.example.chat.util;

import android.util.Log;

import com.example.chat.base.Constant;
import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.GroupChatMessage;
import com.example.chat.bean.GroupTableMessage;
import com.example.chat.bean.RecentMsg;
import com.example.chat.bean.SharedMessage;
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

        public static void d(String tag, String msg) {
                if (Constant.DEBUG) {
                        Log.d(tag, msg);
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
                                + "groupNick" + message.getGroupNick() + "\n"
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
                        LogUtil.e("群消息格式如下：\n");
                        LogUtil.e("belongId:" + groupChatMessage.getBelongId() + "\n"
                                + "belongAvatar:" + groupChatMessage.getBelongAvatar() + "\n"
                                + "belongName:" + groupChatMessage.getBelongUserName() + "\n"
                                + "belongNick:" + groupChatMessage.getBelongNick() + "\n"
                                + "content:" + groupChatMessage.getContent() + "\n"
                                + "msgType:" + groupChatMessage.getMsgType() + "\n"
                                + "readStatus:" + groupChatMessage.getReadStatus() + "\n"
                                + "sendStatus:" + groupChatMessage.getSendStatus() + "\n"
                                + "groupId:" + groupChatMessage.getGroupId() + "\n"
                                + "createTime:" + groupChatMessage.getCreateTime() + "\n"
                                + "conversationType:" + groupChatMessage.getConversationType() + "\n"
                        );
                }
        }

        public static void e(ChatMessage chatMessage) {
                if (Constant.DEBUG) {
                        LogUtil.e("答应出来的ChatMessage的数据格式" + "\n");
                        LogUtil.e("tag：" + chatMessage.getTag() + "\n"
                                + "conversationId：" + chatMessage.getConversationId() + "\n"
                                + "toId：" + chatMessage.getToId() + "\n"
                                + "belongId：" + chatMessage.getBelongId() + "\n"
                                + "name：" + chatMessage.getBelongUserName() + "\n"
                                + "nick：" + chatMessage.getBelongNick() + "\n"
                                + "avatar：" + chatMessage.getBelongAvatar() + "\n"
                                + "content：" + chatMessage.getContent() + "\n"
                                + "messageType：" + chatMessage.getMsgType() + "\n"
                                + "sendStatus：" + chatMessage.getSendStatus() + "\n"
                                + "readStatus：" + chatMessage.getReadStatus() + "\n"
                                + "createTime：" + chatMessage.getCreateTime() + "\n");
                }
        }

        public static void e(SharedMessage sharedMessage) {
                if (Constant.DEBUG) {
                        LogUtil.e("说说消息的数据格式为" + "\n");
                        LogUtil.e("objectId：" + sharedMessage.getObjectId() + "\n"
                                + "content：" + sharedMessage.getContent() + "\n"
                                + "msgType：" + sharedMessage.getMsgType() + "\n"
                                + "address：" + sharedMessage.getAddress() + "\n"
                                + "visibleType：" + sharedMessage.getVisibleType() + "\n"
                                + "createTime：" + sharedMessage.getCreateTime() + "\n"
                                + "createAt：" + sharedMessage.getCreatedAt() + "\n"
                                + "urlTitle：" + sharedMessage.getUrlTitle() + "\n"
                                +"serverTime:"+sharedMessage.getCreatedAt()
                        );
                        if (sharedMessage.getVisibleType().equals(Constant.SHARE_MESSAGE_VISIBLE_TYPE_PUBLIC)) {
                                LogUtil.e("该说说对以下用户不可见；用户列表：");
                                for (String uid :
                                        sharedMessage.getInVisibleUserList()) {
                                        LogUtil.e(uid);
                                }
                        } else {
                                LogUtil.e("该说说为私人说说");
                        }
                        if (sharedMessage.getCommentMsgList() != null && sharedMessage.getCommentMsgList().size() > 0) {
                                LogUtil.e("评论列表如下:");
                                for (String commentMsg :
                                        sharedMessage.getCommentMsgList()) {
                                        LogUtil.e(commentMsg);
                                }
                        }
                        if (sharedMessage.getMsgType().equals(Constant.MSG_TYPE_SHARE_MESSAGE_IMAGE)) {
                                if (sharedMessage.getImageList() != null && sharedMessage.getImageList().size() > 0) {
                                        LogUtil.e("图片URL列表如下:");
                                        for (String url :
                                                sharedMessage.getImageList()) {
                                                LogUtil.e(url);
                                        }
                                }
                        }
                        if (sharedMessage.getMsgType().equals(Constant.MSG_TYPE_SHARE_MESSAGE_VIDEO)) {
                                if (sharedMessage.getImageList() != null && sharedMessage.getImageList().size() > 0) {
                                        LogUtil.e("视频URL和封面URL如下：");
                                        for (String url :
                                                sharedMessage.getImageList()) {
                                                LogUtil.e(url);
                                        }
                                }
                        }
                        if (sharedMessage.getMsgType().equals(Constant.MSG_TYPE_SHARE_MESSAGE_LINK)) {
                                if (sharedMessage.getUrlList() != null && sharedMessage.getUrlList().size() > 0) {
                                        LogUtil.e("链接的url内容如下：");
                                        for (String url :
                                                sharedMessage.getUrlList()) {
                                                LogUtil.e(url);
                                        }
                                }
                        }
                        LogUtil.e("点赞列表");
                        if (sharedMessage.getLikerList() != null && sharedMessage.getLikerList().size() > 0) {
                                for (String uid :
                                        sharedMessage.getLikerList()) {
                                        LogUtil.e(uid);
                                }
                        }
                }
        }

        public static void e(RecentMsg item) {
                if (Constant.DEBUG) {
                        LogUtil.e("最近的消息格式如下");
                        LogUtil.e("belongId：" + item.getBelongId() + "\n"
                                + "belongName：" + item.getName() + "\n"
                                + "nick：" + item.getNick() + "\n"
                                + "avatar：" + item.getAvatar() + "\n"
                                + "time：" + item.getTime() + "\n"
                                + "content：" + item.getLastMsgContent() + "\n"
                                + "msgType：" + item.getMsgType() + "\n");
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
