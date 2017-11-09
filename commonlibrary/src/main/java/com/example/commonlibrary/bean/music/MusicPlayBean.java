package com.example.commonlibrary.bean.music;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.commonlibrary.baseadapter.baseitem.MultipleItem;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by COOTEK on 2017/8/18.
 */
@Entity
public class MusicPlayBean implements Parcelable,MultipleItem {
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
    private String tingId;


    private boolean isRecent;


    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null) && (obj instanceof MusicPlayBean) && (((MusicPlayBean) obj).getSongId() == getSongId())&&
                (((MusicPlayBean) obj).getType() == getType());
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
                ", isRecent=" + isRecent +
                '}';
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
        dest.writeString(this.tingId);
        dest.writeString(this.songName);
        dest.writeString(this.albumName);
        dest.writeString(this.artistName);
        dest.writeString(this.albumUrl);
        dest.writeString(this.lrcUrl);
        dest.writeString(this.songUrl);
        dest.writeInt(this.duration);
        dest.writeInt(this.type);
        dest.writeByte(this.isLocal ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isRecent ? (byte) 1 : (byte) 0);
    }

    protected MusicPlayBean(Parcel in) {
        this.songId = in.readLong();
        this.albumId = in.readLong();
        this.artistId = in.readString();
        this.tingId = in.readString();
        this.songName = in.readString();
        this.albumName = in.readString();
        this.artistName = in.readString();
        this.albumUrl = in.readString();
        this.lrcUrl = in.readString();
        this.songUrl = in.readString();
        this.duration = in.readInt();
        this.type=in.readInt();
        this.isLocal = in.readByte() != 0;
        this.isRecent = in.readByte() != 0;
    }

    @Generated(hash = 609172616)
    public MusicPlayBean(long songId, long albumId, String artistId, String songName, String albumName, String artistName,
            String albumUrl, String lrcUrl, String songUrl, int duration, boolean isLocal, String tingId, boolean isRecent,
            int type) {
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
        this.isRecent = isRecent;
        this.type = type;
    }

    public boolean getIsRecent() {
        return this.isRecent;
    }

    public void setIsRecent(boolean isRecent) {
        this.isRecent = isRecent;
    }

    public String getTingId() {
        return this.tingId;
    }

    public void setTingId(String tingId) {
        this.tingId = tingId;
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




    public static final int NORMAL=0;
    public static final int DETAIL=1;
    public static final int ACTION=2;



    private int type=NORMAL;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemViewType() {
        return type;
    }
}
