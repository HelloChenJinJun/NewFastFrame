package com.example.commonlibrary.bean.music;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by COOTEK on 2017/8/9.
 */
@Entity
public class MusicHistoryInfo {
    @Id
    private int position;


    @Generated(hash = 1378218586)
    public MusicHistoryInfo(int position) {
        this.position = position;
    }

    @Generated(hash = 1087470908)
    public MusicHistoryInfo() {
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


}
