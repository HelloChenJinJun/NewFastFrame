// IMusicService.aidl
package com.example.cootek.newfastframe;
import com.example.commonlibrary.bean.music.MusicPlayBean;
// Declare any non-default types here with import statements

interface IMusicService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
                   void open(in List<MusicPlayBean> list, int position);
                   void stop();
                   void pause();
                   void play();
                   void prev();
                   void next();
                   void setPlayMode(int mode);
                   void refresh();
                   boolean isPlaying();
                   long [] getQueue();
                   int getQueuePosition();
                   long duration();
                   long position();
                   long seek(long pos);
                   MusicPlayBean getCurrentPlayInfo();
                   MusicPlayBean getPlayInfo(int index);
                   void remove(int position);
                   int getAudioSessionId();



}
