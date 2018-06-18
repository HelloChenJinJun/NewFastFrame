package com.example.chat.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.chat.R;
import com.example.chat.base.Constant;
import com.example.chat.bean.BaseMessage;
import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.PostNotifyBean;
import com.example.chat.bean.StepBean;
import com.example.chat.bean.SystemNotifyBean;
import com.example.chat.events.MessageInfoEvent;
import com.example.chat.events.StepEvent;
import com.example.chat.events.UnReadPostNotifyEvent;
import com.example.chat.events.UnReadSystemNotifyEvent;
import com.example.chat.listener.OnReceiveListener;
import com.example.chat.manager.ChatNotificationManager;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.commentnotify.CommentNotifyActivity;
import com.example.chat.mvp.notify.SystemNotifyActivity;
import com.example.chat.mvp.step.StepDetector;
import com.example.chat.util.LogUtil;
import com.example.chat.util.TimeUtil;
import com.example.commonlibrary.bean.chat.PostNotifyInfo;
import com.example.commonlibrary.bean.chat.StepData;
import com.example.commonlibrary.bean.chat.SystemNotifyEntity;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/13      17:00
 * QQ:             1981367757
 */

public class PollService extends Service implements SensorEventListener{

    private Disposable disposable;
    private StepDetector stepDetector;
    private BroadcastReceiver receiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int time;
        if (intent != null) {
            time = intent.getIntExtra(Constant.TIME, 30);
        } else {
            time = 30;
        }
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }

        if (UserDBManager.getInstance().getStepData(TimeUtil
                .getTime(System.currentTimeMillis(),"yyyy-MM-dd"))==null){
            BmobQuery<StepBean> bmobQuery=new BmobQuery<>();
            bmobQuery.addWhereEqualTo("time",TimeUtil
                    .getTime(System.currentTimeMillis(),"yyyy-MM-dd"));
            bmobQuery.addWhereEqualTo("user",new BmobPointer(UserManager.getInstance().getCurrentUser()));
            bmobQuery.include("user");
            bmobQuery.findObjects(new FindListener<StepBean>() {
                @Override
                public void done(List<StepBean> list, BmobException e) {
                    if (e == null) {
                        if (list != null && list.size() > 0) {
                            UserDBManager.getInstance().getDaoSession()
                                    .getStepDataDao()
                                    .insertOrReplace(MsgManager.getInstance().cover(list.get(0)));
                        }
                    }
                }
            });
        }
        dealStep();
        Observable.interval(time, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        dealWork();
                        saveStepData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null) {
                            CommonLogger.e("定时任务出错" + e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        return super.onStartCommand(intent, START_FLAG_RETRY, startId);
    }





    private void dealStep() {
        dealReceiver();
        SensorManager sensorManager= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager==null)return;
        Sensor sensor=null;
        if (Build.VERSION.SDK_INT > 19) {
             sensor=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if (sensor == null) {
                sensor=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            }
        }
        if (sensor == null) {
            sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        StepData stepData=UserDBManager.getInstance()
                .getStepData(TimeUtil.getTime(System.currentTimeMillis(),"yyyy-MM-dd"));
        int count=0;
        if (stepData != null) {
            count=stepData.getStepCount();
        }
        stepDetector=new StepDetector(count, stepCount -> {
            CommonLogger.e("step:"+stepCount);
            RxBusManager.getInstance().post(new StepEvent(stepCount));
        });

        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void dealReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        intentFilter.addAction(Intent.ACTION_SHUTDOWN);
        intentFilter.addAction(Intent.ACTION_DATE_CHANGED);
        registerReceiver(receiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction()==null)return;
                    if (intent.getAction().equals(Intent.ACTION_DATE_CHANGED)){
                        stepDetector.setStepCount(0);
                    }else {
                        saveStepData();
                    }
            }
        },intentFilter);
    }

    private void saveStepData() {
        String currentTime=TimeUtil.getTime(System.currentTimeMillis(),"yyyy-MM-dd");
       StepData data=UserDBManager.getInstance().getStepData(currentTime);
        if (data != null) {
            StepBean stepBean=new StepBean();
            stepBean.setStepCount(data.getStepCount());
            stepBean.setTime(currentTime);
            stepBean.setUser(UserManager.getInstance().getCurrentUser());
            if (data.getId() != null) {
                stepBean.setObjectId(data.getId());
                stepBean.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        UserDBManager.getInstance().getDaoSession().update(MsgManager.getInstance()
                                .cover(stepBean));
                    }
                });
            }else {
                stepBean.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        UserDBManager.getInstance().getDaoSession().update(MsgManager.getInstance()
                                .cover(stepBean));
                    }
                });
            }
        }else if (stepDetector.getStepCount()!=0){
            StepBean stepBean=new StepBean();
            stepBean.setStepCount(stepDetector.getStepCount());
            stepBean.setTime(currentTime);
            stepBean.setUser(UserManager.getInstance().getCurrentUser());
            stepBean.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    UserDBManager.getInstance().getDaoSession().insert(MsgManager.getInstance()
                    .cover(stepBean));
                }
            });

        }
    }

    private void dealWork() {
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
        query.findObjects(new FindListener<ChatMessage>() {
            @Override
            public void done(List<ChatMessage> list, BmobException e) {
                if (e == null) {
                    LogUtil.e("1拉取单聊消息成功");
                    if (list != null && list.size() > 0) {
                        for (ChatMessage item :
                                list) {
                            MsgManager.getInstance().dealReceiveChatMessage(item, new OnReceiveListener() {
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
                                        ChatNotificationManager.getInstance(getBaseContext()).sendChatMessageNotification(chatMessage, getBaseContext());
                                    }
                                }

                                @Override
                                public void onFailed(BmobException e) {
                                    LogUtil.e("接受消息失败!>>>>" + e.getMessage() + e.getErrorCode());
                                }
                            });
                        }
                    }
                } else {
                    LogUtil.e("拉取单聊消息失败");
                    LogUtil.e("在服务器上查询聊天消息失败：" + e.toString());
                }
            }
        });

        BmobQuery<PostNotifyBean> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("toUser", new BmobPointer(UserManager.getInstance().getCurrentUser()));
        bmobQuery.addWhereEqualTo("readStatus", Constant.READ_STATUS_UNREAD);
        bmobQuery.include("relatedUser");
        bmobQuery.findObjects(new FindListener<PostNotifyBean>() {
            @Override
            public void done(List<PostNotifyBean> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        List<PostNotifyInfo> result = new ArrayList<>(list.size());
                        for (PostNotifyBean item :
                                list) {
                            PostNotifyInfo postNotifyInfo = new PostNotifyInfo();
                            postNotifyInfo.setId(item.getObjectId());
                            postNotifyInfo.setType(item.getType());
                            postNotifyInfo.setReadStatus(Constant.READ_STATUS_UNREAD);
                            result.add(postNotifyInfo);
                            item.setReadStatus(Constant.READ_STATUS_READED);
                        }
                        List<BmobObject>  update=new ArrayList<>(list);
                        new BmobBatch().updateBatch(update).doBatch(new QueryListListener<BatchResult>() {
                            @Override
                            public void done(List<BatchResult> results, BmobException e) {
                                if (e == null) {
                                    CommonLogger.e("批量更新帖子相关通知已读成功");
                                    UserDBManager.getInstance().addOrUpdatePostNotify(result);
                                    UserDBManager.getInstance().addOrUpdateUser(list.get(0).getRelatedUser());
                                    RxBusManager.getInstance().post(new UnReadPostNotifyEvent(list.get(0)));
                                    ChatNotificationManager.getInstance(getBaseContext()).showNotification(null, getBaseContext(), "你有一条帖子相关通知", R.mipmap.ic_launcher, "你有一条帖子相关通知", CommentNotifyActivity.class);
                                }else {
                                    CommonLogger.e("批量更新帖子相关通知已读失败"+e.toString());
                                }
                            }
                        });
                    } else {
                        CommonLogger.e("定时拉去帖子相关通知数据为空");
                    }
                } else {
                    CommonLogger.e("定时拉去帖子相关通知失败" + e.toString());
                }
            }
        });
        BmobQuery<SystemNotifyBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("readStatus", Constant.READ_STATUS_UNREAD);
        query1.findObjects(new FindListener<SystemNotifyBean>() {
            @Override
            public void done(List<SystemNotifyBean> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        List<SystemNotifyEntity> result = new ArrayList<>(list.size());
                        for (SystemNotifyBean item : list
                                ) {
                            SystemNotifyEntity systemNotifyEntity = new SystemNotifyEntity();
                            systemNotifyEntity.setReadStatus(Constant.READ_STATUS_UNREAD);
                            systemNotifyEntity.setTitle(item.getTitle());
                            systemNotifyEntity.setSubTitle(item.getSubTitle());
                            systemNotifyEntity.setImageUrl(item.getImageUrl());
                            systemNotifyEntity.setContentUrl(item.getContentUrl());
                            systemNotifyEntity.setId(item.getObjectId());
                            item.setReadStatus(Constant.READ_STATUS_READED);
                            result.add(systemNotifyEntity);
                        }
                        List<BmobObject> update = new ArrayList<>(list);
                        new BmobBatch().updateBatch(update).doBatch(new QueryListListener<BatchResult>() {
                            @Override
                            public void done(List<BatchResult> list, BmobException e) {
                                if (e == null) {
                                    CommonLogger.e("批量更新系统通知成功");
                                    UserDBManager.getInstance().addOrUpdateSystemNotify(result);
                                    RxBusManager.getInstance().post(new UnReadSystemNotifyEvent());
                                    ChatNotificationManager.getInstance(getBaseContext()).showNotification(null, getBaseContext(), "系统", R.mipmap.ic_launcher, "你有一条系统通知", SystemNotifyActivity.class);
                                } else {
                                    CommonLogger.e("批量更新系统通知失败" + e.toString());
                                }
                            }
                        });

                    }
                } else {
                    CommonLogger.e("定时拉取评论通知失败" + e.toString());
                }
            }
        });


    }


    @Override
    public void onDestroy() {
        if (disposable != null) {
            disposable.dispose();
        }
        if (receiver!=null) {
            unregisterReceiver(receiver);
        }
        super.onDestroy();

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        stepDetector.dealSensorEvent(sensorEvent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
