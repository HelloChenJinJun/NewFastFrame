package com.example.chat.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.chat.bean.GroupChatMessage;
import com.example.chat.bean.GroupTableMessage;
import com.example.chat.bean.NotifyPostResult;
import com.example.chat.bean.User;
import com.example.chat.events.GroupTableEvent;
import com.example.chat.events.MessageInfoEvent;
import com.example.chat.events.RecentEvent;
import com.example.chat.events.RefreshMenuEvent;
import com.example.chat.manager.ChatNotificationManager;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;

import com.example.chat.util.JsonUtil;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.rxbus.RxBusManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobRealTimeData;
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
        private List<String> groupChatMessageList = new ArrayList<>();
        private NotifyBinder mNotifyBinder = new NotifyBinder();
        private List<String> shareMessageList = new ArrayList<>();

        @Override
        public void onCreate() {
                super.onCreate();
        }

        private void dealListener() {
                LogUtil.e("启动接受实时监听的服务啦啦啦");
                table = "_User";
                data = new BmobRealTimeData();
                data.start(new ValueEventListener() {
                        @Override
                        public void onConnectCompleted(Exception e) {
                                if (e == null) {
                                        LogUtil.e("实时监听：连接服务器成功啦啦啦");
                                        if (data.isConnected()) {
                                                LogUtil.e("连接啦啦啦");
                                                List<String>  userIdList= UserDBManager.getInstance()
                                                        .getAllFriendId();
                                                uidList.clear();
                                                uidList.addAll(userIdList);
                                                for (String uid : uidList) {
                                                        if (!uid.equals(UserManager.getInstance().getCurrentUserObjectId())) {
                                                                data.subRowUpdate(table, uid);
                                                        } else {
                                                                LogUtil.e("本地用户信息的修改，不监听");
                                                        }
                                                }

                                                List<String>  groupIdList=UserDBManager.getInstance().getAllGroupId();
                                                groupChatMessageList.clear();
                                                groupChatMessageList.addAll(groupIdList);
                                                for (String groupId :
                                                        groupChatMessageList) {
                                                        data.subTableUpdate("g" + groupId);
                                                        data.subRowUpdate("GroupTableMessage", groupId);
                                                }
                                                LogUtil.e("这里开始监听群数据");

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
                                                        return;
                                                }
                                                if (!TextUtils.isEmpty(JsonUtil.getString(object, "creatorId"))) {
                                                        LogUtil.e("实时监听的群结构更新消息到啦1");
                                                        final GroupTableMessage groupTableMessage = MsgManager.getInstance().createReceiveGroupTableMsg(object.toString());
//                                                        判断是否是踢出群的消息
                                                        if (!groupTableMessage.getGroupNumber().contains(UserManager.getInstance().getCurrentUserObjectId())) {
//                                                                退出群的消息
//                                                                在服务器上删除自己的群结构消息
                                                                mNotifyBinder.removeGroup(groupTableMessage.getGroupId());
                                                                UserDBManager.getInstance()
                                                                        .deleteRecentMessage(groupTableMessage.getGroupId());
                                                                UserDBManager.getInstance().deleteGroupTableMessage(groupTableMessage.getGroupId());
                                                                RxBusManager.getInstance().post(new RecentEvent(groupTableMessage.getGroupId(),RecentEvent.ACTION_DELETE));
                                                                RxBusManager.getInstance().post(new RefreshMenuEvent(0));
                                                                RxBusManager.getInstance().post(new GroupTableEvent(groupTableMessage.getGroupId()
                                                                        ,GroupTableEvent.TYPE_GROUP_NUMBER,GroupTableEvent.ACTION_DELETE,UserManager.getInstance().getCurrentUserObjectId()));
                                                                return;
                                                        }
                                                        UserDBManager
                                                                .getInstance().addOrUpdateGroupTable(groupTableMessage);
                                                        MessageInfoEvent messageInfoEvent=new MessageInfoEvent(MessageInfoEvent.TYPE_GROUP_TABLE);
                                                        List<GroupTableMessage>  list=new ArrayList<>();
                                                        list.add(groupTableMessage);
                                                        messageInfoEvent.setGroupTableMessageList(list);
                                                        RxBusManager.getInstance().post(messageInfoEvent);
                                                } else if (!JsonUtil.getString(object, "groupId").equals("")) {
                                                        LogUtil.e("实时监听的群消息到啦");
                                                        GroupChatMessage groupChatMessage = MsgManager.getInstance().createReceiveGroupChatMsg(object);
                                                        if (groupChatMessage.getBelongId().equals(UserManager.getInstance().getCurrentUserObjectId())) {
//                                                                不接受本用户发过来的消息
                                                                LogUtil.e("实时检测到本用户的群消息，不接受");
                                                                return;
                                                        }
                                                        MsgManager.getInstance().dealReceiveGroupChatMessage(groupChatMessage);
                                                        ChatNotificationManager
                                                                .getInstance(getBaseContext())
                                                                .sendGroupMessageNotification(groupChatMessage,getBaseContext());
                                                        MessageInfoEvent messageInfoEvent=new MessageInfoEvent(MessageInfoEvent.TYPE_GROUP_CHAT);
                                                        List<GroupChatMessage>  list=new ArrayList<>();
                                                        list.add(groupChatMessage);
                                                        messageInfoEvent.setGroupChatMessageList(list);
                                                        RxBusManager.getInstance().post(messageInfoEvent);


                                                }else {
//                                                        监听到公共说说
                                                        String author=JsonUtil.getString(object,"author");
                                                        if (!TextUtils.isEmpty(author)&&!author.contains("type")){
                                                                if (!author.equals(UserManager.getInstance().getCurrentUserObjectId())) {
                                                                        NotifyPostResult result=new NotifyPostResult();
                                                                        NotifyPostResult.DataBean bean=new NotifyPostResult.DataBean();
                                                                        bean.setAuthor(author);
                                                                        result.setData(bean);
                                                                        RxBusManager.getInstance().post(result);
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
                        data.unsubTableUpdate("PublicPostBean");
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
