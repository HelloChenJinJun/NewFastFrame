package com.example.chat.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.chat.R;
import com.example.chat.base.Constant;
import com.example.chat.bean.BaseMessage;
import com.example.chat.bean.ChatMessage;
import com.example.chat.db.ChatDB;
import com.example.chat.listener.OnMessageReceiveListener;
import com.example.chat.listener.OnReceiveListener;
import com.example.chat.manager.ChatNotificationManager;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserManager;
import com.example.chat.util.CommonUtils;
import com.example.chat.util.JsonUtil;
import com.example.chat.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.push.PushConstants;
import cn.bmob.v3.exception.BmobException;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/10/9      17:09
 * QQ:             1981367757
 */

public class PushMessageReceiver extends BroadcastReceiver implements OnReceiveListener {
        UserManager mUserManager;
        MsgManager mMsgManager;
        private Context context;
        private static List<OnMessageReceiveListener> sOnMessageReceiveListenerList = new ArrayList<>();

        @Override
        public void onReceive(Context context, Intent intent) {
                LogUtil.e("接收到的json消息格式：" + intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING));
                String json;
                json = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
//                Toast.makeText(context, json, Toast.LENGTH_SHORT).show();
                this.context = context;
                mUserManager = UserManager.getInstance();
                mMsgManager = MsgManager.getInstance();
                boolean isConnected = CommonUtils.isNetWorkAvailable(context);
                if (sOnMessageReceiveListenerList != null && sOnMessageReceiveListenerList.size() > 0) {
                        for (OnMessageReceiveListener listener :
                                sOnMessageReceiveListenerList) {
                                listener.onNetWorkChanged(isConnected);
                        }
                }
                if (isConnected) {
                        parseMsg(json);
                }
        }

        /**
         * 解析的步骤如下：
         * 1、判断是否是群结构消息
         * 2、判断是否是下线通知
         * 3、判断是否是系统消息
         * 4、判断是否是聊天消息
         * <p>
         * <p>
         * <p>
         * 解析json数据
         *
         * @param json json数据
         */
        private void parseMsg(String json) {
                try {
                        JSONObject jsonObject = new JSONObject(json);
                        String tag = JsonUtil.getString(jsonObject, Constant.MESSAGE_TAG);
                        if (tag != null && tag.equals(Constant.TAG_OFFLINE)) {
//                                下线通知
//                                帐号在其他手机上登录，强迫该手机帐号下线
                                if (sOnMessageReceiveListenerList != null && sOnMessageReceiveListenerList.size() > 0) {
                                        for (OnMessageReceiveListener listener :
                                                sOnMessageReceiveListenerList) {
                                                LogUtil.e("OnMessageReceiveListener：下线了");
                                                listener.onOffline();
                                        }
                                } else {
                                        UserManager.getInstance().logout();
                                }
                                return;
                        }
                        if (!jsonObject.has(Constant.TAG_BELONG_ID)) {
//                                系统消息
                                String systemInfo = JsonUtil.getString(jsonObject, Constant.PUSH_ALERT);
//                        系统通知的消息
                                Toast.makeText(context, systemInfo, Toast.LENGTH_SHORT).show();
                                LogUtil.e("系统信息");
                                ChatNotificationManager.getInstance(context).showNotification("", context, "系统", R.drawable.head, systemInfo,null);
                                return;
                        }
                        String fromId = JsonUtil.getString(jsonObject, Constant.TAG_BELONG_ID);
                        String toId = JsonUtil.getString(jsonObject, Constant.TAG_TO_ID);
                        if (toId != null && !toId.equals("") && fromId != null && !fromId.equals("")) {
                                if (mUserManager.getCurrentUser() != null && mUserManager.getCurrentUser().getObjectId().equals(toId)) {
                                        if (ChatDB.create(toId).hasFriend(fromId) && !ChatDB.create(toId).isBlackUser(fromId)) {
                                                LogUtil.e("本用户的好友");
                                                mMsgManager.createReceiveMsg(json, this);
                                        } else if (!ChatDB.create().hasFriend(fromId)) {
                                                LogUtil.e("陌生人用户");
                                                //                                        陌生人发来消息的情况,,,直接接受
                                                mMsgManager.createReceiveMsg(json, this);
                                        } else {
                                                LogUtil.e("黑名单用户");
                                                //      黑名单的情况,直接在服务器上面更新为已读状态,防止从定时服务那里又可以获取到
                                                mMsgManager.updateMsgReaded(false, JsonUtil.getString(jsonObject, Constant.TAG_CONVERSATION), JsonUtil.getString(jsonObject, Constant.TAG_CREATE_TIME));
                                        }
                                } else {
                                        LogUtil.e("聊天消息的接收方不为当前用户，不做任何操作");
                                }
                        } else {
                                LogUtil.e("传输过程中用户ID丢失");
                        }
                } catch (JSONException e) {
                        e.printStackTrace();
                }
        }



        public static void registerListener(OnMessageReceiveListener listener) {
                LogUtil.e("注册监听");
                if (sOnMessageReceiveListenerList.contains(listener)) return;
                LogUtil.e("正在注册监听");
                sOnMessageReceiveListenerList.add(listener);
        }

        public static void unRegisterListener(OnMessageReceiveListener listener) {
                if (!sOnMessageReceiveListenerList.contains(listener)) return;
                sOnMessageReceiveListenerList.remove(listener);
        }

        @Override
        public void onSuccess(BaseMessage baseMessage) {
                if (baseMessage instanceof ChatMessage) {
                        ChatMessage chatMessage = (ChatMessage) baseMessage;
                        LogUtil.e("接受成功");
                        LogUtil.e(chatMessage);
                        String tag = chatMessage.getTag();
                        if (sOnMessageReceiveListenerList != null && sOnMessageReceiveListenerList.size() > 0) {
                                for (OnMessageReceiveListener listener :
                                        sOnMessageReceiveListenerList) {
                                        LogUtil.e("casc");
                                        if (tag == null || tag.equals("")) {
                                                LogUtil.e("下发消息");
                                                listener.onNewChatMessageCome(chatMessage);
                                        } else if (tag.equals(Constant.TAG_ADD_FRIEND)) {
                                                listener.onAddFriendMessageCome(chatMessage);
                                        } else if (tag.equals(Constant.TAG_AGREE)) {
                                                listener.onAgreeMessageCome(chatMessage);
                                        } else if (tag.equals(Constant.TAG_ASK_READ)) {
                                                listener.onAskReadMessageCome(chatMessage);
                                        }
                                }
                        }

                        //                        聊天消息
                        ChatNotificationManager.getInstance(context).sendChatMessageNotification(chatMessage, context);
                }
        }

        @Override
        public void onFailed(BmobException e) {
                LogUtil.e("接受消息失败!>>>>" + e.getMessage() + e.getErrorCode());
        }
}
