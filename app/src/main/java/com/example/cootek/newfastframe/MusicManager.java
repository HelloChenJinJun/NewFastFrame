package com.example.cootek.newfastframe;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.commonlibrary.utils.CommonLogger;

import java.util.Arrays;
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
        CommonLogger.e("启动服务啦啦啦1");
        context.startService(intent);
        BindConnection bindConnection = new BindConnection();
        CommonLogger.e("绑定服务1");
        if (context.bindService(intent, bindConnection, Service.BIND_AUTO_CREATE)) {
            connectionWeakHashMap.put(context, bindConnection);
            CommonLogger.e("绑定服务成功");
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
                service.prev(force);
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
     * @param context
     * @param idList
     * @param position     为-1的时候表示第一次播放该列表的歌曲
     * @param typeId
     * @param type
     * @param forceShuffle
     */
    public void play(Context context, long[] idList, int position, long typeId, @MusicIdType.IdType int type, boolean forceShuffle) {
        CommonLogger.e("1");
        if (idList == null || idList.length == 0 || service == null) {
            if (idList == null) {
                CommonLogger.e("idList");
            } else if (idList.length == 0) {
                CommonLogger.e("idList1");
            } else {
                CommonLogger.e("service");
            }
            return;
        }
        CommonLogger.e("2");
        try {
            if (forceShuffle) {
                service.setShuffleMode(MusicService.SHUFFLE_NORMAL);
            }
//            这里判断是否是同一首歌
            long currentId = service.getAudioId();
            int currentPosition = service.getQueuePosition();
            long[] currentList = getCurrentListId();
            if (position != -1 && currentId == idList[position] && currentPosition == position && Arrays.equals(idList, currentList)) {
                service.play();
                return;
            }
            position = position < 0 ? 0 : position;
//            准备资源
            service.open(idList, forceShuffle ? -1 : position, typeId, type);
            service.setRepeatMode(MusicService.REPEAT_NONE);
            service.play();
        } catch (RemoteException e) {
            e.printStackTrace();
            CommonLogger.e("异常" + e.getMessage());
        }
    }

    private long[] getCurrentListId() {
        try {
            return service.getQueue();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return new long[0];
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


    private class BindConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CommonLogger.e("连接远程服务成功");
            MusicManager.this.service = IMusicService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            CommonLogger.e("断开远程服务成功");
            MusicManager.this.service = null;

        }
    }
}
