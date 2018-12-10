package com.example.cootek.newfastframe;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.manager.music.MusicPlayerManager;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.event.ServiceStateEvent;

import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by COOTEK on 2017/8/7.
 */

public class MusicManager {
    private static MusicManager instance;
    private MusicService.MusicBinder service;
    private WeakHashMap<Context, BindConnection> connectionWeakHashMap;

    public static MusicManager getInstance() {
        if (instance == null) {
            synchronized (MusicManager.class) {
                if (instance == null) {
                    instance = new MusicManager();
                }
            }
        }
        return instance;
    }

    private MusicManager() {
        connectionWeakHashMap = new WeakHashMap<>();
    }


    public void bindService(Context context) {
        Intent intent = new Intent(context, MusicService.class);
        context.startService(intent);
        BindConnection bindConnection = new BindConnection();
        if (context.bindService(intent, bindConnection, Service.BIND_AUTO_CREATE)) {
            connectionWeakHashMap.put(context, bindConnection);
        }
    }


    public void unBindService(Context context) {
        if (context == null) {
            return;
        }
        if (connectionWeakHashMap.containsKey(context)) {
            BindConnection connection = connectionWeakHashMap.remove(context);
            if (connection != null) {
                context.unbindService(connection);
            }
        }
        if (connectionWeakHashMap.isEmpty()) {
            service = null;
        }
    }

    public void playOrPause() {
        if (service != null) {
            if (service.getCurrentState() == MusicPlayerManager.PLAY_STATE_PLAYING) {
                service.pause();
            } else {
                service.play();
            }
        }
    }

    public void previous() {
        if (service != null) {
            service.pre();
        }
    }


    public void next() {
        if (service != null) {
            service.next();
        }
    }


    public boolean isPlaying() {
        if (service != null) {
            return service.getCurrentState() == MusicPlayerManager.PLAY_STATE_PLAYING;
        } else {
            return false;
        }
    }

    public long getCurrentProgress() {
        if (service != null) {
            return service.getPosition();
        } else {
            return 0;
        }
    }

    public void seekTo(int progress) {
        if (service != null) {
            service.seekTo(progress);
        }

    }


    public void setPlayMode(int mode) {
        if (service != null) {
            service.setPlayMode(mode);
        }

    }

    public void play(List<MusicPlayBean> list, int position, long seekPosition) {
        if (service != null && list != null && list.size() > 0) {
            BaseApplication.getAppComponent().getDaoSession()
                    .getMusicPlayBeanDao().insertOrReplaceInTx(list);

            for (MusicPlayBean bean : list
                    ) {
                CommonLogger.e("插入播放的音乐数据" + bean.toString());
            }
            service.play(list, position, seekPosition);
        }
    }


    private class BindConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CommonLogger.e("连接远程服务成功");
            MusicManager.this.service = (MusicService.MusicBinder) service;
            RxBusManager.getInstance().post(new ServiceStateEvent(true));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            CommonLogger.e("断开远程服务成功");
            RxBusManager.getInstance().post(new ServiceStateEvent(false));
            MusicManager.this.service = null;


        }
    }
}
