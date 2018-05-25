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
import com.example.chat.bean.PostNotifyBean;
import com.example.chat.events.MessageInfoEvent;
import com.example.chat.events.OffLineEvent;
import com.example.chat.events.UnReadPostNotifyEvent;
import com.example.chat.events.UnReadSystemNotifyEvent;
import com.example.chat.listener.OnReceiveListener;
import com.example.chat.manager.ChatNotificationManager;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.commentnotify.CommentNotifyActivity;
import com.example.chat.mvp.notify.SystemNotifyActivity;
import com.example.chat.util.JsonUtil;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.chat.PostNotifyInfo;
import com.example.commonlibrary.bean.chat.SystemNotifyEntity;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.push.PushConstants;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

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
    private String msg=null;

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
                msg=systemInfo;
                JsonElement jsonElement=new JsonParser().parse(systemInfo);
                JsonObject jsonObject1= null;
                if (jsonElement!=null) {
                    jsonObject1 = jsonElement.getAsJsonObject();
                }
                if (jsonObject1!=null&&jsonObject1.has(Constant.TAG_CONTENT_URL)) {
//                        系统通知的消息
                    SystemNotifyEntity systemNotifyBean= BaseApplication
                            .getAppComponent().getGson()
                            .fromJson(systemInfo,SystemNotifyEntity.class);
                    Toast.makeText(context, systemInfo, Toast.LENGTH_SHORT).show();
                    if (!UserDBManager.getInstance().hasSystemNotify(systemNotifyBean.getId())) {
                        MsgManager.getInstance().updateSystemNotifyReadStatus(systemNotifyBean.getId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    CommonLogger.e("更新系统通知已读成功");
                                    UserDBManager.getInstance().addOrUpdateSystemNotify(systemNotifyBean);
                                    RxBusManager.getInstance().post(new UnReadSystemNotifyEvent());
                                    ChatNotificationManager.getInstance(context).showNotification(null, context, "系统", R.mipmap.ic_launcher, "你有一条系统通知", SystemNotifyActivity.class);
                                }else {
                                    CommonLogger.e("更新系统通知已读失败"+e.toString());
                                }
                            }
                        });
                    }
                }else if (jsonObject1!=null&&jsonObject1.has(Constant.TAG_ID)){
                    PostNotifyInfo postNotifyInfo= BaseApplication
                            .getAppComponent().getGson()
                            .fromJson(systemInfo,PostNotifyInfo.class);
                    if (!UserDBManager.getInstance().hasPostNotifyInfo(postNotifyInfo.getId())) {
                            MsgManager.getInstance().updatePostNotifyReadStatus(postNotifyInfo, new FindListener<PostNotifyBean>() {
                                @Override
                                public void done(List<PostNotifyBean> list, BmobException e) {
                                    if (e == null) {
                                        CommonLogger.e("更新帖子相关通知已读成功");
                                        if (list != null && list.size() > 0) {
                                            UserDBManager.getInstance()
                                                    .addOrUpdatePostNotify(postNotifyInfo);
                                            RxBusManager.getInstance().post(new UnReadPostNotifyEvent(list.get(0)));
                                            ChatNotificationManager.getInstance(context).showNotification(null,context,"你有一条帖子相关通知",R.mipmap.ic_launcher,systemInfo, CommentNotifyActivity.class);
                                        }else {
                                            CommonLogger.e("暂时查不到服务器上的数据");
                                        }
                                    }else {
                                        CommonLogger.e("更新帖子相关通知已读出错"+e.toString());
                                    }
                                }
                            });
                    }
                }else {
                    ChatNotificationManager.getInstance(context).showNotification(null,context,"你有一条自定义系统通知",R.mipmap.ic_launcher,systemInfo, CommentNotifyActivity.class);
                }
                return;
            }
            String fromId = JsonUtil.getString(jsonObject, Constant.TAG_BELONG_ID);
            String toId = JsonUtil.getString(jsonObject, Constant.TAG_TO_ID);
            if (!TextUtils.isEmpty(toId) && !TextUtils.isEmpty(fromId)) {
                if (mUserManager.getCurrentUser() != null && mUserManager.getCurrentUser().getObjectId().equals(toId)) {
                    if (UserDBManager.getInstance().isFriend(fromId)) {
                        CommonLogger.e("本用户的好友");
                        mMsgManager.createReceiveMsg(json, this);
                    } else if (UserDBManager.getInstance().isStranger(fromId)) {
                        CommonLogger.e("陌生人用户");
                        //                                        陌生人发来消息的情况,,,直接接受
                        mMsgManager.createReceiveMsg(json, this);
                    } else if (UserDBManager.getInstance().isAddBlack(fromId)) {
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
        } catch (JSONException |JsonSyntaxException|IllegalStateException e) {
            e.printStackTrace();
            ChatNotificationManager.getInstance(context).showNotification(null,context,"你有一条自定义系统通知",R.mipmap.ic_launcher,msg, CommentNotifyActivity.class);
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
