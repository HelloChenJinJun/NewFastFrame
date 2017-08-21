// IMusicService.aidl
package com.example.cootek.newfastframe;
import com.example.cootek.newfastframe.MusicPlayBean;
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
                   void setShuffleMode(int shuffleMode);
                   void setRepeatMode(int repeatMode);
                   void refresh();
                   boolean isPlaying();
                   long [] getQueue();
                   int getQueuePosition();
                   long duration();
                   long position();
                   long seek(long pos);
                   MusicPlayBean getCurrentPlayInfo();
                   MusicPlayBean getPlayInfo(int index);
                   int getShuffleMode();
                   int getRepeatMode();



}
