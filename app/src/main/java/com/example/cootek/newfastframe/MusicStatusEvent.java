package com.example.cootek.newfastframe;

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
    public static final String REPEATMODE_CHANGED = "repeatmode_changed";
    public static final String SHUFFLEMODE_CHANGED = "shuffle_mode_changed";
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


        public MusicContent(long id, String songName, String artistName, String albumName, boolean isPlaying, long maxProgress) {
            this.id = id;
            this.songName = songName;
            this.artistName = artistName;
            this.albumName = albumName;
            this.maxProgress = maxProgress;
            this.isPlaying = isPlaying;
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
