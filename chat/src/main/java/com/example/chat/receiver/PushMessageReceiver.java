package com.example.chat.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.chat.R;
import com.example.chat.base.Constant;
import com.example.chat.bean.BaseMessage;
import com.example.chat.bean.ChatMessage;
import com.example.chat.events.MessageInfoEvent;
import com.example.chat.events.OffLineEvent;
import com.example.chat.listener.OnReceiveListener;
import com.example.chat.manager.ChatNotificationManager;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserManager;
import com.example.chat.util.JsonUtil;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;

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

    @Override
    public void onReceive(Context context, Intent intent) {
        CommonLogger.e("接收到的json消息格式：" + intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING));
        this.context = context;
        mUserManager = UserManager.getInstance();
        mMsgManager = MsgManager.getInstance();
        parseMsg(intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING));
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
                RxBusManager.getInstance().post(new OffLineEvent());
                UserManager.getInstance().logout();
                return;
            }
            if (!jsonObject.has(Constant.TAG_BELONG_ID)) {
//                                系统消息
                String systemInfo = JsonUtil.getString(jsonObject, Constant.PUSH_ALERT);
//                        系统通知的消息
                Toast.makeText(context, systemInfo, Toast.LENGTH_SHORT).show();
                CommonLogger.e("系统信息");
                ChatNotificationManager.getInstance(context).showNotification(null, context, "系统", R.drawable.head, systemInfo, null);
                return;
            }
            String fromId = JsonUtil.getString(jsonObject, Constant.TAG_BELONG_ID);
            String toId = JsonUtil.getString(jsonObject, Constant.TAG_TO_ID);
            if (!TextUtils.isEmpty(toId) && !TextUtils.isEmpty(fromId)) {
                if (mUserManager.getCurrentUser() != null && mUserManager.getCurrentUser().getObjectId().equals(toId)) {
                    if (UserManager.getInstance().isFriend(fromId)) {
                        CommonLogger.e("本用户的好友");
                        mMsgManager.createReceiveMsg(json, this);
                    } else if (UserManager.getInstance().isStranger(fromId)) {
                        CommonLogger.e("陌生人用户");
                        //                                        陌生人发来消息的情况,,,直接接受
                        mMsgManager.createReceiveMsg(json, this);
                    } else if (UserManager.getInstance().isAddBlack(fromId)) {
                        CommonLogger.e("黑名单用户");
                        //      黑名单的情况,直接在服务器上面更新为已读状态,防止从定时服务那里又可以获取到
                        mMsgManager.updateMsgReaded(false, JsonUtil.getString(jsonObject, Constant.TAG_CONVERSATION), JsonUtil.getLong(jsonObject, Constant.TAG_CREATE_TIME));
                    } else {
//                                                被添加的黑名单
                        mMsgManager.createReceiveMsg(json, this);
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




    @Override
    public void onSuccess(BaseMessage baseMessage) {
        if (baseMessage instanceof ChatMessage) {
            ChatMessage chatMessage = (ChatMessage) baseMessage;
            CommonLogger.e("接受成功");
            LogUtil.e(chatMessage);
            List<ChatMessage> list = new ArrayList<>(1);
            list.add(chatMessage);
            MessageInfoEvent messageInfoEvent = new MessageInfoEvent(MessageInfoEvent.TYPE_PERSON);
            list.add(chatMessage);
            messageInfoEvent.setChatMessageList(list);
            //                        聊天消息
            RxBusManager.getInstance().post(messageInfoEvent);
            ChatNotificationManager.getInstance(context).sendChatMessageNotification(chatMessage, context);
        }
    }

    @Override
    public void onFailed(BmobException e) {
        LogUtil.e("接受消息失败!>>>>" + e.getMessage() + e.getErrorCode());
    }
}
