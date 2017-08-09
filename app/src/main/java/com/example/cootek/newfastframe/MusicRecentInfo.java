package com.example.cootek.newfastframe;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by COOTEK on 2017/8/9.
 */
@Entity
public class MusicRecentInfo {
    private long time;
    @Id
    private long songId;


    @Generated(hash = 853484312)
    public MusicRecentInfo(long time, long songId) {
        this.time = time;
        this.songId = songId;
    }

    @Generated(hash = 1217133473)
    public MusicRecentInfo() {
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }
}
