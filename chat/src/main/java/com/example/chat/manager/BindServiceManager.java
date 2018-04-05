package com.example.chat.manager;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.example.chat.service.GroupMessageService;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.utils.CommonLogger;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/27     20:03
 * QQ:         1981367757
 */

public class BindServiceManager {
    private static BindServiceManager instance;
    private GroupMessageService.NotifyBinder binder;
    private ServiceConnection connection;
    private Intent intent;
    private Context context;


    public static BindServiceManager getInstance() {
        if (instance == null) {
            instance=new BindServiceManager();
        }
        return instance;
    }

    public void  bindService(Context context){
        this.context=context;
        intent=new Intent(context, GroupMessageService.class);
        context.startService(intent);
        connection=new BinderConnection();
        context.bindService(intent,connection, Service.BIND_AUTO_CREATE);
    }


    public void makeSure(){
        if (connection != null && binder != null) {
            return;
        }
        if (context != null) {
            bindService(context);
        }
    }



    public void onDestroy(){
        if (connection != null) {
            context.unbindService(connection);
            context.stopService(intent);
            context=null;
            connection = null;
            intent=null;
            binder=null;
        }
    }


    private class BinderConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (GroupMessageService.NotifyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }






    public void notifySharedMessageChanged(String objectId, boolean isAdd) {
        makeSure();
        if (binder != null) {
            if (isAdd) {
                CommonLogger.e("通知实时监听说说消息拉拉");
                binder.addShareMessage(objectId);
            } else {
                LogUtil.e("移除实时监听说说消息拉拉");
                binder.removeShareMessage(objectId);
            }
        } else {
            LogUtil.e("binder为空");
        }
    }


    public void notifyGroupTableMsgCome(String groupId) {
        makeSure();
        LogUtil.e("这里通知服务监听群消息");
        if (binder != null) {
            binder.addGroup(groupId);
        } else {
            LogUtil.e("binder 是空的，监听不了群消息");
        }
    }





}
