package com.example.chat.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;

import com.example.chat.base.Constant;
import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.GroupTableMessage;
import com.example.chat.bean.SharedMessage;
import com.example.chat.manager.MessageCacheManager;
import com.example.chat.manager.UserCacheManager;
import com.example.chat.manager.UserManager;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.BaseApplication;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/13      17:00
 * QQ:             1981367757
 */

public class PollService extends Service {

        private Handler mHandler;
        private int time;

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
                return null;
        }


        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
                if (intent != null) {
                        time = intent.getIntExtra("time", 6);
                } else {
                        time = 5;
                }
                LogUtil.e("time" + time);
                new Thread(new Runnable() {
                        @Override
                        public void run() {
                                LogUtil.e("后台定时服务run22334566789");
                                Looper.prepare();
                                mHandler = new Handler() {
                                        @Override
                                        public void handleMessage(Message msg) {
                                                dealWork();
                                                sendEmptyMessageDelayed(0, time * 1000);
                                        }
                                };
                                mHandler.sendEmptyMessage(0);
                                Looper.loop();
                                LogUtil.e("12221");
                        }
                }).start();
                LogUtil.e("这里Thread2");
//                new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//
//
//                        }
//                }).start();
                return super.onStartCommand(intent, START_FLAG_RETRY, startId);
        }

        private void dealWork() {
                LogUtil.e("1dealWork");
                LogUtil.e("拉取单聊消息");
                BmobQuery<ChatMessage> query = new BmobQuery<>();
                if (UserManager.getInstance().getCurrentUser() != null) {
                        query.addWhereEqualTo(Constant.TAG_TO_ID, UserManager.getInstance().getCurrentUserObjectId());
                } else {
                        return;
                }
                query.addWhereEqualTo(Constant.TAG_MESSAGE_SEND_STATUS, Constant.SEND_STATUS_SUCCESS);
                query.addWhereEqualTo(Constant.TAG_MESSAGE_READ_STATUS, Constant.READ_STATUS_UNREAD);
//                按升序进行排序
                query.setLimit(50);
                query.order("createdAt");
                query.findObjects(BaseApplication.getInstance(), new FindListener<ChatMessage>() {
                        @Override
                        public void onSuccess(List<ChatMessage> list) {
                                LogUtil.e("1拉取单聊消息成功");
                                if (list != null && list.size() > 0) {
                                        Intent intent = new Intent(Constant.NEW_MESSAGE_ACTION);
                                        intent.putExtra("from", "person");
                                        intent.putExtra(Constant.NEW_MESSAGE, (Serializable) list);
                                        sendOrderedBroadcast(intent, null);
                                }
                        }

                        @Override
                        public void onError(int i, String s) {
                                LogUtil.e("拉取单聊消息失败");
                                LogUtil.e("在服务器上查询聊天消息失败：" + s + i);
                        }
                });
//                                这里开始检测群结构消息
                BmobQuery<GroupTableMessage> groupTableQuery = new BmobQuery<>();
//                                第一次检测不需要该条件

                String lastGroupMessageTime = MessageCacheManager.getInstance().getLastGroupMessageTime(Constant.GROUP_TABLE_TIME);
                if (lastGroupMessageTime == null) {
                        return;
                }
                groupTableQuery.addWhereEqualTo("readStatus", Constant.READ_STATUS_UNREAD);
                if (lastGroupMessageTime.equals("0000-00-00 01:00:00")) {
                        LogUtil.e("第一次检测群结构消息，不设条件");
                } else {
                        LogUtil.e("1不是第一次检测消息");
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                                Date oldDate=sdf1.parse(MessageCacheManager.getInstance().getLastShareMessageTime(Constant.GROUP_TABLE_TIME));
                                long currentTime=oldDate.getTime();
                                LogUtil.e("之前的时间"+currentTime+",,,"+sdf1.format(oldDate));
                                currentTime+=1000;
                                Date date=new Date(currentTime);
                                String newTime=sdf1.format(date);
                                LogUtil.e("Long时间"+currentTime);
                                LogUtil.e("现在的时间"+newTime);
                                groupTableQuery.addWhereGreaterThan("updatedAt", new BmobDate(date));
//                                groupTableQuery.addWhereGreaterThan("updatedAt", new BmobDate(sdf1.parse(lastGroupMessageTime)));
                        } catch (ParseException e) {
                                e.printStackTrace();
                                LogUtil.e("解析时间出错");
                        }
                }
                if (UserManager.getInstance().getCurrentUser() != null) {
                        groupTableQuery.addWhereEqualTo("toId", UserManager.getInstance().getCurrentUserObjectId());
                } else {
                        return;
                }
                groupTableQuery.order("updatedAt");
                groupTableQuery.findObjects(BaseApplication.getInstance(), new FindListener<GroupTableMessage>() {
                                @Override
                                public void onSuccess(List<GroupTableMessage> list) {
                                        if (list != null && list.size() > 0) {
                                                String time = list.get(list.size() - 1).getUpdatedAt();
                                                LogUtil.e("最大的群结构更新时间：" + time);
                                                MessageCacheManager.getInstance().setLastGroupMessageTime(Constant.GROUP_TABLE_TIME, time);
                                                Intent tableIntent = new Intent(Constant.NEW_MESSAGE_ACTION);
                                                tableIntent.putExtra("from", "groupTable");
                                                tableIntent.putExtra(Constant.NEW_MESSAGE, (Serializable) list);
                                                sendOrderedBroadcast(tableIntent, null);
                                        }
                                }

                                @Override
                                public void onError(int i, String s) {
                                        LogUtil.e("在服务器上查询群结构表消息失败：" + s + i);
                                }
                        }
                );
                LogUtil.e("这里开始在服务器上拉取说说消息1");
//                                拉取条件是，不管发送状态为成功或者是失败（失败意味着是推送失败的情况）,首次拉取全部数据，以后每一拉取数据在之前拉取数据的时间之后
                BmobQuery<SharedMessage> shareQuery = new BmobQuery<>();
                if (UserCacheManager.getInstance().getContacts() != null && UserCacheManager.getInstance().getContacts().size() > 0) {
                        List<String> list = new ArrayList<>(UserCacheManager.getInstance().getContacts().keySet());
                        list.add(UserManager.getInstance().getCurrentUserObjectId());
//                        list.add(UserCacheManager.getInstance().getUser().getObjectId());
                        shareQuery.addWhereContainedIn("belongId", list);
                } else {
                        if (UserManager.getInstance().getCurrentUser() == null) {
                                LogUtil.e("用户已退出登录");
                                return;
                        }
                        shareQuery.addWhereEqualTo("belongId", UserManager.getInstance().getCurrentUser().getObjectId());
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (!MessageCacheManager.getInstance().getLastShareMessageTime(Constant.SHARE_TIME).equals("0000-00-00 01:00:00")) {
                        try {
//                                这里是bmob的一个bug，相同时间竟然也能查询得到，所以这里在原有的时间上多添加了一秒
                                LogUtil.e("时间是多少:"+MessageCacheManager.getInstance().getLastShareMessageTime(Constant.SHARE_TIME));
                                Date oldDate=simpleDateFormat.parse(MessageCacheManager.getInstance().getLastShareMessageTime(Constant.SHARE_TIME));
                                long currentTime=oldDate.getTime();
                                LogUtil.e("之前的时间"+currentTime+",,,"+simpleDateFormat.format(oldDate));
                                currentTime+=1000;
                                Date date=new Date(currentTime);
                               String newTime=simpleDateFormat.format(date);
                                LogUtil.e("Long时间"+currentTime);
                                LogUtil.e("现在的时间"+newTime);
                                shareQuery.addWhereGreaterThan("createdAt", new BmobDate(date));

                        } catch (ParseException e) {
                                e.printStackTrace();
                                LogUtil.e("时间解析出错"+e.getMessage());
                        }
                } else {
                        LogUtil.e("第一次拉取说说消息");
                }
//                                拉取最新的10条说说消息
                shareQuery.order("-createdAt");
                shareQuery.setLimit(10);
                shareQuery.findObjects(BaseApplication.getInstance(), new FindListener<SharedMessage>() {
                        @Override
                        public void onSuccess(List<SharedMessage> list) {
                                if (list != null && list.size() > 0) {
                                        String time = list.get(0).getUpdatedAt();
                                        MessageCacheManager.getInstance().setLastShareMessageTime(Constant.SHARE_TIME,time);
                                        LogUtil.e("最大的说说消息更新时间" + time);
//                                        这里筛选出不可见人列表是否包含本用户
                                        LogUtil.e("定时检测到的说说消息如下");
                                        List<SharedMessage> result = new ArrayList<>(list);
                                        for (SharedMessage message :
                                                list) {
                                                LogUtil.e(message);
                                                if (message.getBelongId().equals(UserManager.getInstance().getCurrentUserObjectId())) {
                                                        continue;
                                                }
                                                if (message.getVisibleType().equals(Constant.SHARE_MESSAGE_VISIBLE_TYPE_PRIVATE)) {
                                                        result.remove(message);
                                                }else {
                                                        if (message.getInVisibleUserList().contains(UserManager.getInstance().getCurrentUserObjectId())) {
                                                                result.remove(message);
                                                        }
                                                }
                                        }
                                        LogUtil.e("筛选可见后的说说列表");
                                        for (SharedMessage message :
                                                result) {
                                                LogUtil.e(message);
                                        }
                                        if (result.size() > 0) {
                                                Intent intent = new Intent(Constant.NEW_SHARE_MESSAGE_ACTION);
                                                intent.putExtra("from", "share");
                                                intent.putExtra(Constant.NEW_MESSAGE, (Serializable) result);
                                                sendBroadcast(intent);
                                        }
                                } else {
                                        LogUtil.e("服务器上面暂时没有好友发的说说消息");
                                }
                        }

                        @Override
                        public void onError(int i, String s) {
                                LogUtil.e("定时拉取数据失败" + s);
                        }
                });
        }


        @Override
        public void onDestroy() {
                super.onDestroy();

        }
}
