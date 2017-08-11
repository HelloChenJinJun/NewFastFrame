package com.example.cootek.newfastframe;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by COOTEK on 2017/8/10.
 */
@Entity
public class Music {



    @Id
    private long songId;
    private String songTitle;
    private long artistId;
    private String artistName;
    private String path;
    private long albumId;
    private String albumName;
    private int duration;
    private int position;


    @Generated(hash = 1183506752)
    public Music(long songId, String songTitle, long artistId, String artistName,
            String path, long albumId, String albumName, int duration,
            int position) {
        this.songId = songId;
        this.songTitle = songTitle;
        this.artistId = artistId;
        this.artistName = artistName;
        this.path = path;
        this.albumId = albumId;
        this.albumName = albumName;
        this.duration = duration;
        this.position = position;
    }

    @Generated(hash = 1263212761)
    public Music() {
    }


    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
