package com.example.chat.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.chat.R;
import com.example.chat.base.Constant;
import com.example.chat.bean.GroupChatMessage;
import com.example.chat.bean.GroupTableMessage;
import com.example.chat.bean.RecentMsg;
import com.example.chat.bean.SharedMessage;
import com.example.chat.bean.User;
import com.example.chat.db.ChatDB;
import com.example.chat.events.GroupInfoEvent;
import com.example.chat.listener.OnShareMessageReceivedListener;
import com.example.chat.manager.ChatNotificationManager;
import com.example.chat.manager.MessageCacheManager;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserCacheManager;
import com.example.chat.manager.UserManager;
import com.example.chat.ui.HomeActivity;

import com.example.chat.util.CommonUtils;
import com.example.chat.util.JsonUtil;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.rxbus.RxBusManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.ValueEventListener;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/17      18:47
 * QQ:             1981367757
 */

public class GroupMessageService extends Service {


        private BmobRealTimeData data;
        private List<String> uidList = new ArrayList<>();
        private String table;
        //        private List<String> groupTableList = new ArrayList<>();
        private List<String> groupChatMessageList = new ArrayList<>();


        private NotifyBinder mNotifyBinder = new NotifyBinder();
        private List<String> shareMessageList = new ArrayList<>();
        private static List<OnShareMessageReceivedListener> sListeners = new ArrayList<>();

        public static void registerListener(OnShareMessageReceivedListener onShareMessageReceivedListener) {
                if (!sListeners.contains(onShareMessageReceivedListener)) {
                        sListeners.add(onShareMessageReceivedListener);
                }
        }

        public static void unRegisterListener(OnShareMessageReceivedListener listener) {
                if (sListeners.contains(listener)) {
                        sListeners.remove(listener);
                }
        }


        @Override
        public void onCreate() {
                super.onCreate();
        }

        private void dealListener() {
                LogUtil.e("111111111111启动接受实时监听的服务啦啦啦");
                table = "_User";
                data = new BmobRealTimeData();
                data.start(BaseApplication.getInstance(), new ValueEventListener() {
                                @Override
                                public void onConnectCompleted() {
                                        LogUtil.e("实时监听：连接服务器成功啦啦啦");
                                        if (data.isConnected()) {
                                                LogUtil.e("连接啦啦啦");
                                                if (UserCacheManager.getInstance().getContacts() != null && UserCacheManager.getInstance().getContacts().keySet().size() > 0) {
                                                        LogUtil.e("有好友数据");
                                                        uidList.clear();
                                                        uidList.addAll(UserCacheManager.getInstance().getContacts().keySet());
                                                        for (String uid : uidList) {
                                                                if (!uid.equals(UserManager.getInstance().getCurrentUserObjectId())) {
                                                                        data.subRowUpdate(table, uid);
                                                                } else {
                                                                        LogUtil.e("本地用户信息的修改，不监听");
                                                                }
                                                        }
                                                } else {
                                                        LogUtil.e("没有好友数据");
                                                }
                                                LogUtil.e("这里开始监听群数据");
                                                if (MessageCacheManager.getInstance().getAllGroupId() != null && MessageCacheManager.getInstance().getAllGroupId().size() > 0) {
                                                        groupChatMessageList.clear();
                                                        groupChatMessageList.addAll(MessageCacheManager.getInstance().getAllGroupId());
                                                        for (String groupId :
                                                                groupChatMessageList) {
                                                                data.subTableUpdate("g" + groupId);
                                                                data.subRowUpdate("GroupTableMessage", groupId);
                                                        }

                                                } else {
                                                        LogUtil.e("没有群数据可监听");
                                                }
                                                if (shareMessageList.size() > 0) {
                                                        for (String id :
                                                                shareMessageList) {
                                                                data.subRowUpdate("SharedMessage", id);
                                                                data.subRowDelete("SharedMessage", id);
                                                        }
                                                }

                                        } else {
                                                LogUtil.e("未连接");
                                        }
                                }

                                @Override
                                public void onDataChange(JSONObject jsonObject) {
                                        LogUtil.e("监听到数据啦啦啦\n");
                                        LogUtil.e("数据格式如下:\n");
                                        LogUtil.e("json数据" + jsonObject.toString());
                                        try {
                                                JSONObject object = jsonObject.getJSONObject("data");
                                                String action = jsonObject.getString("action");
                                                if (action.equals("updateRow")) {
                                                        LogUtil.e("更新行的监听到啦");
                                                } else if (action.equals("deleteRow")) {
                                                        LogUtil.e("删除行的监听到啦");
//                                                        这里只有说说消息监听行的删除
                                                        String id = jsonObject.getString("objectId");
                                                        LogUtil.e("删除的说说ID为" + id);
                                                        long result = ChatDB.create().deleteSharedMessage(id);
                                                        LogUtil.e("删除的行号为" + result);
                                                        if (result > 0) {
                                                                if (sListeners.size() > 0) {
                                                                        for (OnShareMessageReceivedListener listener :
                                                                                sListeners) {
                                                                                listener.onDeleteShareMessage(id);
                                                                        }
                                                                }
                                                        } else {
                                                                LogUtil.e("删除不成功");
                                                        }
                                                        return;
                                                }
                                                if (!JsonUtil.getString(object, "creatorId").equals("")) {
                                                        LogUtil.e("实时监听的群结构更新消息到啦1");
                                                        final GroupTableMessage groupTableMessage = MsgManager.getInstance().createReceiveGroupTableMsg(object.toString());
//                                                        判断是否是踢出群的消息
                                                        if (!groupTableMessage.getGroupNumber().contains(UserManager.getInstance().getCurrentUserObjectId())) {
//                                                                退出群的消息
//                                                                在服务器上删除自己的群结构消息
                                                                LogUtil.e("这里了没??");
                                                                mNotifyBinder.removeGroup(groupTableMessage.getGroupId());
                                                                LogUtil.e(MessageCacheManager.getInstance().getGroupTableMessage(groupTableMessage.getGroupId()));
                                                                MsgManager.getInstance().deleteGroupTableMessage(MessageCacheManager.getInstance().getGroupTableMessage(groupTableMessage.getGroupId()).getObjectId(), new DeleteListener() {
                                                                        @Override
                                                                        public void onSuccess() {
                                                                                LogUtil.e("在服务器上删除自己的群结构消息成功1");
                                                                                MessageCacheManager.getInstance().deleteGroupTableMessage(groupTableMessage.getGroupId());
                                                                                ChatDB.create().deleteRecentMsg(groupTableMessage.getGroupId());
                                                                                ChatDB.create().deleteGroupTableMessage(groupTableMessage.getGroupId());
                                                                                RxBusManager.getInstance().post(new GroupInfoEvent(groupTableMessage.getGroupId(),GroupInfoEvent.TYPE_GROUP_NUMBER));
                                                                        }
                                                                        @Override
                                                                        public void onFailure(int i, String s) {
                                                                                LogUtil.e("在服务器上删除自己的群结构消息失败");
                                                                        }
                                                                });
                                                                return;
                                                        }
                                                        GroupTableMessage message = MessageCacheManager.getInstance().getGroupTableMessage(groupTableMessage.getGroupId());
                                                        message.setGroupAvatar(groupTableMessage.getGroupAvatar());
                                                        message.setNotification(groupTableMessage.getNotification());
                                                        message.setGroupDescription(groupTableMessage.getGroupDescription());
                                                        message.setGroupNumber(groupTableMessage.getGroupNumber());
                                                        message.setGroupName(groupTableMessage.getGroupName());
//                                                        这里也要同步更新自己的群结构消息,如果是群主就不需要更新，非群主才需要
                                                        if (UserManager.getInstance().getCurrentUser() != null && !UserManager.getInstance().getCurrentUser().getObjectId().equals(message.getCreatorId())) {
                                                                LogUtil.e("非群主，需要更新");
                                                                MsgManager.getInstance().updateGroupTableMessage(message);
                                                        }
                                                        ChatDB.create().saveGroupTableMessage(message);
                                                        MessageCacheManager.getInstance().addGroupTableMessage(message);
                                                        RecentMsg recentMsg=ChatDB.create().getRecentMsg(message.getGroupId());
                                                        LogUtil.e("1这里更改最近群消息");
                                                        if (recentMsg != null) {
                                                                LogUtil.e("这里正式更改最近群消息");
                                                                recentMsg.setAvatar(message.getGroupAvatar());
                                                                recentMsg.setName(message.getGroupName());
                                                                ChatDB.create().saveRecentMessage(recentMsg);
                                                        }
                                                        Intent intent1 = new Intent(Constant.NEW_MESSAGE_ACTION);
                                                        intent1.putExtra("from", "table");
                                                        intent1.putExtra(Constant.NEW_MESSAGE, message);
                                                        sendOrderedBroadcast(intent1, null);
                                                } else if (!JsonUtil.getString(object, "groupId").equals("")) {
                                                        LogUtil.e("实时监听的群消息到啦");
                                                        GroupChatMessage groupChatMessage = MsgManager.getInstance().createReceiveGroupChatMsg(object);
                                                        if (groupChatMessage.getBelongId().equals(UserManager.getInstance().getCurrentUserObjectId())) {
//                                                                不接受本用户发过来的消息
                                                                LogUtil.e("实时检测到本用户的群消息，不接受");
                                                                return;
                                                        }
                                                        Intent intent1 = new Intent(Constant.NEW_MESSAGE_ACTION);

                                                        if (ChatDB.create().isExistGroupChatMessage(groupChatMessage.getGroupId(), groupChatMessage.getCreateTime())) {
                                                                LogUtil.e("这里是更新群消息的昵称或头像");
                                                                ChatDB.create().saveGroupChatMessage(groupChatMessage);
                                                                intent1.putExtra("isRefresh",true);
                                                        }
                                                        intent1.putExtra("from", "group");
                                                        intent1.putExtra(Constant.NEW_MESSAGE, groupChatMessage);
                                                        sendOrderedBroadcast(intent1, null);
                                                        if (!CommonUtils.isAppOnForeground(getBaseContext())) {
                                                                if (BaseApplication.getAppComponent().getSharedPreferences().getBoolean(groupChatMessage.getGroupId(),true)) {
                                                                        ChatNotificationManager.getInstance(getBaseContext()).notify(Constant.NOTIFICATION_TAG_GROUP_MESSAGE, groupChatMessage.getGroupId(), true, true, getBaseContext(),
                                                                                MessageCacheManager.getInstance().getGroupTableMessage(groupChatMessage.getGroupId()).getGroupName(), R.mipmap.ic_launcher, groupChatMessage.getContent(),
                                                                                HomeActivity.class);
                                                                }
                                                        }
                                                } else if (!JsonUtil.getString(object, "username").equals("")) {
                                                        LogUtil.e("实时监听的用户消息到啦");
                                                        User user = MsgManager.getInstance().createUserFromJsonObject(object);
                                                        MessageCacheManager.getInstance().setUserDataLastUpdateTime(user.getObjectId(), JsonUtil.getString(object, "updatedAt"));
                                                        ChatDB.create().addOrUpdateContacts(user);
                                                        UserCacheManager.getInstance().addContact(user);
                                                } else if (JsonUtil.getInt(object, "visibleType") != 0) {
                                                        LogUtil.e("监听到分享消息");
                                                        LogUtil.e("监听到说说数据更新操作");
                                                        SharedMessage sharedMessage = MsgManager.getInstance().createSharedMessageFromJson(object);
                                                        if (sharedMessage != null) {
                                                                ChatDB.create().saveSharedMessage(sharedMessage);
                                                                LogUtil.e(sharedMessage);
                                                                if (sListeners.size() > 0) {
                                                                        for (OnShareMessageReceivedListener listener :
                                                                                sListeners) {
                                                                                listener.onAddShareMessage(sharedMessage);
                                                                        }
                                                                }
                                                        }
                                                }
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                                LogUtil.e("解析出错");
                                        }
                                }
                        }
                );

        }

        public class NotifyBinder extends Binder {


                public void addGroup(String groupId) {
                        if (data.isConnected()) {
                                if (!groupChatMessageList.contains(groupId)) {
                                        LogUtil.e("还没有监听过该群消息，这里开始监听");
                                        groupChatMessageList.add(groupId);
                                        data.subTableUpdate("g" + groupId);
                                        data.subRowUpdate("GroupTableMessage", groupId);
                                } else {
                                        LogUtil.e("已经监听过该群消息拉，无需再次监听");
                                }
                        } else {
                                LogUtil.e("网络异常，数据与服务器上连接不上group");
                        }
                }


                public void addUser(String uid){
                        if (data.isConnected()) {
                                if (!uidList.contains(uid)) {
                                        data.subRowUpdate(table, uid);
                                }else {
                                        LogUtil.e("已经监听该好友");
                                }
                        }else {
                                LogUtil.e("网络异常，数据也服务器连接不上");
                        }
                }


                public void removeGroup(String groupId) {
                        if (data.isConnected()) {
                                data.unsubTableUpdate("g" + groupId);
                                data.unsubRowUpdate("GroupTableMessage", groupId);
                                if (groupChatMessageList.contains(groupId)) {
                                        groupChatMessageList.remove(groupId);
                                }
                        }
                }


                public void addShareMessage(String shareMessageId) {
                        LogUtil.e("到服务这里1");
                        if (data.isConnected()) {
                                data.subRowUpdate("SharedMessage", shareMessageId);
                                data.subRowDelete("SharedMessage", shareMessageId);
                                if (!shareMessageList.contains(shareMessageId)) {
                                        shareMessageList.add(shareMessageId);
                                        LogUtil.e("添加说说监听" + shareMessageId);
                                        data.subRowUpdate("SharedMessage", shareMessageId);
                                        data.subRowDelete("SharedMessage", shareMessageId);
                                } else {
                                        LogUtil.e("已经监听过该说说" + shareMessageId + "的消息,无需再次监听");
                                }
                        } else {
                                LogUtil.e("网络异常，数据与服务器上连接不上add");
                                //                                这里把ID缓存到内存中,在网络连接成功的时候，再从缓存中监听
                                if (!shareMessageList.contains(shareMessageId)) {
                                        shareMessageList.add(shareMessageId);
                                } else {
                                        LogUtil.e("已经监听过该说说" + shareMessageId + "的消息,无需再次监听");
                                }
                        }
                }


                public void removeShareMessage(String shareMessageId) {
                        LogUtil.e("到服务这里拉1");
                        if (data.isConnected()) {
                                LogUtil.e("移除说说监听" + shareMessageId);
                                data.unsubRowUpdate("SharedMessage", shareMessageId);
                                data.unsubRowDelete("SharedMessage", shareMessageId);
                                if (shareMessageList.contains(shareMessageId)) {
                                        shareMessageList.remove(shareMessageId);
                                } else {
                                        LogUtil.e("已经移除过该说说" + shareMessageId + "的消息,无需再次移除");
                                }
                        } else {
                                LogUtil.e("网络异常，数据与服务器上连接不上 remove");
                        }
                }

                public void startListener() {
                        dealListener();
                }
        }


        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
                dealListener();
                return mNotifyBinder;
        }

        @Override
        public int onStartCommand(final Intent intent, int flags, int startId) {
                return super.onStartCommand(intent, flags, startId);
        }


        @Override
        public void onDestroy() {
                super.onDestroy();
                LogUtil.e("这里是实时检测服务的onDestroy");

                if (data != null) {
                        if (uidList.size() > 0) {
                                LogUtil.e("111111111取消实时监听");
                                for (String uid : uidList) {
                                        data.unsubRowUpdate(table, uid);
                                }
                                uidList.clear();
                        }
                        if (groupChatMessageList.size() > 0) {
                                for (String groupId :
                                        groupChatMessageList) {
                                        data.unsubTableUpdate("g" + groupId);
                                }
                                groupChatMessageList.clear();
                        }
                        if (shareMessageList.size() > 0) {
                                LogUtil.e("有遗留的说说消息未取消监听，现在取消");
                                for (String id :
                                        shareMessageList) {
                                        data.unsubRowUpdate("SharedMessage", id);
                                        data.unsubRowDelete("SharedMessage", id);
                                }
                                shareMessageList.clear();
                        }
                }
        }
}
