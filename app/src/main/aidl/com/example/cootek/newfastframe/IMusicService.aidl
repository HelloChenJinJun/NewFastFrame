// IMusicService.aidl
package com.example.cootek.newfastframe;

// Declare any non-default types here with import statements
import com.example.cootek.newfastframe.MusicPlayInfo;

interface IMusicService {
void openFile(String path);
        void open(in long [] list, int position, long sourceId, int sourceType);
        void stop();
        void pause();
        void play();
        void prev(boolean forcePrevious);
        void next();
        void enqueue(in long [] list, int action, long sourceId, int sourceType);
        void setQueuePosition(int index);
        void setShuffleMode(int shufflemode);
        void setRepeatMode(int repeatmode);
        void moveQueueItem(int from, int to);
        void refresh();
        void playlistChanged();
        boolean isPlaying();
        long [] getQueue();
        long getQueueItemAtPosition(int position);
        int getQueueSize();
        int getQueuePosition();
        int getQueueHistoryPosition(int position);
        int getQueueHistorySize();
        int[] getQueueHistoryList();
        long duration();
        long position();
        long seek(long pos);
        void seekRelative(long deltaInMs);
        long getAudioId();
        MusicPlayInfo getCurrentPlayInfo();
        MusicPlayInfo getPlayInfo(int index);
        long getNextAudioId();
        long getPreviousAudioId();
        long getArtistId();
        long getAlbumId();
        String getArtistName();
        String getPlayInfoName();
        String getAlbumName();
        String getPath();
        int getShuffleMode();
        int removePlayInfos(int first, int last);
        int removePlayInfo(long id);
        boolean removePlayInfoAtPosition(long id, int position);
        int getRepeatMode();
        int getMediaMountedCount();
        int getAudioSessionId();
        void setLockscreenAlbumArt(boolean enabled);

}
