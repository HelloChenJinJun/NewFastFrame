package com.example.commonlibrary.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by COOTEK on 2017/8/18.
 */
@Entity
public class MusicPlayBean implements Parcelable {
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
    private int duration;
    private boolean isLocal;


    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null) && (obj instanceof MusicPlayBean) && (((MusicPlayBean) obj).getSongId() == getSongId());
    }

    public MusicPlayBean() {
    }


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getAlbumUrl() {
        return albumUrl;
    }

    public void setAlbumUrl(String albumUrl) {
        this.albumUrl = albumUrl;
    }

    public String getLrcUrl() {
        return lrcUrl;
    }

    public void setLrcUrl(String lrcUrl) {
        this.lrcUrl = lrcUrl;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public boolean getIsLocal() {
        return this.isLocal;
    }

    public void setIsLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.songId);
        dest.writeLong(this.albumId);
        dest.writeString(this.artistId);
        dest.writeString(this.songName);
        dest.writeString(this.albumName);
        dest.writeString(this.artistName);
        dest.writeString(this.albumUrl);
        dest.writeString(this.lrcUrl);
        dest.writeString(this.songUrl);
        dest.writeInt(this.duration);
        dest.writeByte(this.isLocal ? (byte) 1 : (byte) 0);
    }

    protected MusicPlayBean(Parcel in) {
        this.songId = in.readLong();
        this.albumId = in.readLong();
        this.artistId = in.readString();
        this.songName = in.readString();
        this.albumName = in.readString();
        this.artistName = in.readString();
        this.albumUrl = in.readString();
        this.lrcUrl = in.readString();
        this.songUrl = in.readString();
        this.duration = in.readInt();
        this.isLocal = in.readByte() != 0;
    }

    @Generated(hash = 25955599)
    public MusicPlayBean(long songId, long albumId, String artistId, String songName, String albumName, String artistName,
            String albumUrl, String lrcUrl, String songUrl, int duration, boolean isLocal) {
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
    }







    public static final Creator<MusicPlayBean> CREATOR = new Creator<MusicPlayBean>() {
        @Override
        public MusicPlayBean createFromParcel(Parcel source) {
            return new MusicPlayBean(source);
        }

        @Override
        public MusicPlayBean[] newArray(int size) {
            return new MusicPlayBean[size];
        }
    };
}
