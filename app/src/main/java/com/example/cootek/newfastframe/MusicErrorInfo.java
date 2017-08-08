package com.example.cootek.newfastframe;

import java.io.Serializable;

/**
 * Created by COOTEK on 2017/8/8.
 */

class MusicErrorInfo implements Serializable{
private String name;
    private long songId;

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
