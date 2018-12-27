package com.example.commonlibrary.bean.music;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

import androidx.annotation.Nullable;

/**
 * Created by COOTEK on 2017/8/18.
 */
@Entity
public class MusicPlayBean implements Serializable {


    private static final long serialVersionUID = 1L;
    @Id
    private long songId;
    private long albumId;
    private String artistId;
    private String songName;
    private String albumName;
    private String artistName;
    private String albumUrl;
    private String lrcUrl;
    private String songUrl;
    private long duration;
    private boolean isLocal;
    private String tingId;


    private long updateTime;

    @Override
    public String toString() {
        return "MusicPlayBean{" +
                "songId=" + songId +
                ", albumId=" + albumId +
                ", artistId='" + artistId + '\'' +
                ", songName='" + songName + '\'' +
                ", albumName='" + albumName + '\'' +
                ", artistName='" + artistName + '\'' +
                ", albumUrl='" + albumUrl + '\'' +
                ", lrcUrl='" + lrcUrl + '\'' +
                ", songUrl='" + songUrl + '\'' +
                ", duration=" + duration +
                ", isLocal=" + isLocal +
                ", tingId='" + tingId + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }

    @Generated(hash = 979128700)
    public MusicPlayBean(long songId, long albumId, String artistId,
                         String songName, String albumName, String artistName, String albumUrl,
                         String lrcUrl, String songUrl, long duration, boolean isLocal,
                         String tingId, long updateTime) {
        this.songId = songId;
        this.albumId = albumId;
        this.artistId = artistId;
        this.songName = songName;
        this.albumName = albumName;
        this.artistName = artistName;
        this.albumUrl = albumUrl;
        this.lrcUrl = lrcUrl;
        this.songUrl = songUrl;
        this.duration = duration;
        this.isLocal = isLocal;
        this.tingId = tingId;
        this.updateTime = updateTime;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof MusicPlayBean && ((MusicPlayBean) obj).getSongId() == getSongId();
    }

    @Generated(hash = 1105425544)
    public MusicPlayBean() {
    }


    public long getSongId() {
        return this.songId;
    }


    public void setSongId(long songId) {
        this.songId = songId;
    }


    public long getAlbumId() {
        return this.albumId;
    }


    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }


    public String getArtistId() {
        return this.artistId;
    }


    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }


    public String getSongName() {
        return this.songName;
    }


    public void setSongName(String songName) {
        this.songName = songName;
    }


    public String getAlbumName() {
        return this.albumName;
    }


    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }


    public String getArtistName() {
        return this.artistName;
    }


    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }


    public String getAlbumUrl() {
        return this.albumUrl;
    }


    public void setAlbumUrl(String albumUrl) {
        this.albumUrl = albumUrl;
    }


    public String getLrcUrl() {
        return this.lrcUrl;
    }


    public void setLrcUrl(String lrcUrl) {
        this.lrcUrl = lrcUrl;
    }


    public String getSongUrl() {
        return this.songUrl;
    }


    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }


    public long getDuration() {
        return this.duration;
    }


    public void setDuration(long duration) {
        this.duration = duration;
    }


    public boolean getIsLocal() {
        return this.isLocal;
    }


    public void setIsLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }


    public String getTingId() {
        return this.tingId;
    }


    public void setTingId(String tingId) {
        this.tingId = tingId;
    }


    public long getUpdateTime() {
        return this.updateTime;
    }


    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}