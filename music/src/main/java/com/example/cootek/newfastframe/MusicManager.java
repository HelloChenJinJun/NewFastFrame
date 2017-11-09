package com.example.cootek.newfastframe;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.event.MusicStatusEvent;

import java.util.Arrays;
import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by COOTEK on 2017/8/7.
 */

public class MusicManager {
    private static MusicManager instance;
    private IMusicService service;
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
        try {
            if (service != null) {
                if (service.isPlaying()) {
                    service.pause();
                } else {
                    service.play();
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            CommonLogger.e("远程服务失败" + e.getMessage());
        }
    }

    public void previous(boolean force) {
        try {
            if (service != null) {
                service.prev();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            CommonLogger.e("远程服务失败" + e.getMessage());
        }
    }


    public void next() {
        try {
            if (service != null) {
                service.next();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            CommonLogger.e("远程服务失败" + e.getMessage());
        }
    }


    /**
     * @param musicPlayBeanList
     * @param position          为-1的时候表示第一次播放该列表的歌曲
     * @param mode
     */
    public void play(List<MusicPlayBean> musicPlayBeanList, int position, int mode) {
        if (musicPlayBeanList == null || musicPlayBeanList.size() == 0 || service == null) {
            return;
        }
        try {
            service.setPlayMode(mode);
//            这里判断是否是同一首歌
            long currentId = 0;
            int currentPosition = service.getQueuePosition();
            long[] currentList = getQueue();
            long[] list = new long[musicPlayBeanList.size()];
            for (int i = 0; i < musicPlayBeanList.size(); i++) {
                list[i] = musicPlayBeanList.get(i).getSongId();
            }
            if (position != -1 && currentId == musicPlayBeanList.get(position).getSongId() && currentPosition == position && Arrays.equals(list, currentList)) {
                service.play();
                return;
            }
            position = position < 0 ? 0 : position;
//            准备资源
            CommonLogger.e("大小" + musicPlayBeanList.size());
            service.open(musicPlayBeanList, position);
            service.play();
        } catch (RemoteException e) {
            e.printStackTrace();
            CommonLogger.e("异常" + e.getMessage());
        }
    }


    public boolean isPlaying() {
        try {
            if (service != null) {
                return service.isPlaying();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            CommonLogger.e("远程服务失败" + e.getMessage());
        }
        return false;
    }

    public long getCurrentProgress() {
        try {
            if (service != null) {
                return service.position();
            }
        } catch (RemoteException | IllegalStateException e) {
            e.printStackTrace();
            CommonLogger.e("获取进度异常" + e.getMessage());
        }
        return 0;
    }

    public void seekTo(int progress) {
        try {
            if (service != null) {
                service.seek(progress);
            }
        } catch (RemoteException | IllegalStateException e) {
            e.printStackTrace();
            CommonLogger.e("异常" + e.getMessage());
        }
    }

    public void refresh() {
        try {
            if (service != null) {
                service.refresh();
            } else {
                CommonLogger.e("service为空?");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            CommonLogger.e("刷新出错" + e.getMessage());
        }
    }

    public long[] getQueue() {
        try {
            if (service != null) {
                return service.getQueue();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void remove(int position) {
        try {
            if (service != null) {
                service.remove(position);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setMode(int mode) {
        try {
            if (service != null) {
                service.setPlayMode(mode);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public int getAudioSessionId(){
        if (service != null) {
            try {
                return service.getAudioSessionId();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }


    private class BindConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CommonLogger.e("连接远程服务成功");
            MusicManager.this.service = IMusicService.Stub.asInterface(service);
            CommonLogger.e("发送刷新消息啦啦");
            RxBusManager.getInstance().post(new MusicStatusEvent(MusicStatusEvent.REFRESH_CHANGED));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            CommonLogger.e("断开远程服务成功");
            MusicManager.this.service = null;

        }
    }
}
