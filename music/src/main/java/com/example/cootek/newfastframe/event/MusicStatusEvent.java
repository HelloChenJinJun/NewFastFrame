package com.example.cootek.newfastframe.event;

/**
 * Created by COOTEK on 2017/8/14.
 */

public class MusicStatusEvent {

    public static final String META_CHANGED = "meta_changed";
    public static final String PLAYLIST_CHANGED = "play_list_changed";
    public static final String PLAYSTATE_CHANGED = "PLAY_STATE_CHANGED";
    public static final String POSITION_CHANGED = "POSITION_CHANGED";
    public static final String QUEUE_CHANGED = "QUEUE_CHANGED";
    public static final String REFRESH_CHANGED = "REFRESH_CHANGED";
    public static final String BUFFER_UPDATE_CHANGED = "BUFFER_UPDATE_CHANGED";
    public static final String REFRESH_DATA = "REFRESH_DATA";
    private String currentStatus;
    private MusicContent musicContent;


    public MusicStatusEvent(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public MusicStatusEvent() {
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public MusicContent getMusicContent() {
        return musicContent;
    }

    public void setMusicContent(MusicContent musicContent) {
        this.musicContent = musicContent;
    }


    public static class MusicContent {
        private long id;
        private String songName;
        private String artistName;
        private String albumName;
        private boolean isPlaying;
        private long maxProgress;
        private String albumUrl;
        private int mode;
        private int secondProgress;


        public MusicContent(long id, String songName, String artistName, String albumName, boolean isPlaying, long maxProgress, String albumUrl, int mode
                , int secondProgress) {
            this.id = id;
            this.songName = songName;
            this.artistName = artistName;
            this.albumName = albumName;
            this.maxProgress = maxProgress;
            this.isPlaying = isPlaying;
            this.albumUrl = albumUrl;
            this.mode = mode;
            this.secondProgress = secondProgress;
        }


        public void setSecondProgress(int secondProgress) {
            this.secondProgress = secondProgress;
        }

        public int getSecondProgress() {
            return secondProgress;
        }


        public void setMode(int mode) {
            this.mode = mode;
        }

        public int getMode() {
            return mode;
        }


        public String getAlbumUrl() {
            return albumUrl;
        }

        public void setAlbumUrl(String albumUrl) {
            this.albumUrl = albumUrl;
        }

        public long getMaxProgress() {
            return maxProgress;
        }

        public void setMaxProgress(long maxProgress) {
            this.maxProgress = maxProgress;
        }

        public MusicContent() {
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getSongName() {
            return songName;
        }

        public void setSongName(String songName) {
            this.songName = songName;
        }

        public String getArtistName() {
            return artistName;
        }

        public void setArtistName(String artistName) {
            this.artistName = artistName;
        }

        public String getAlbumName() {
            return albumName;
        }

        public void setAlbumName(String albumName) {
            this.albumName = albumName;
        }

        public boolean isPlaying() {
            return isPlaying;
        }

        public void setPlaying(boolean playing) {
            isPlaying = playing;
        }
    }
}
