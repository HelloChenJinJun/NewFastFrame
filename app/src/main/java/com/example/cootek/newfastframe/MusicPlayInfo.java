package com.example.cootek.newfastframe;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by COOTEK on 2017/8/7.
 */


@Entity
public class MusicPlayInfo implements Parcelable {

    @Id
    private long id;
    private long typeId;
    private int originType;
    private int position;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public int getOriginType() {
        return originType;
    }

    public void setOriginType(int originType) {
        this.originType = originType;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Generated(hash = 1074760373)
    public MusicPlayInfo(long id, long typeId, int originType, int position) {
        this.id = id;
        this.typeId = typeId;
        this.originType = originType;
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.typeId);
        dest.writeInt(this.originType);
        dest.writeInt(this.position);
    }

    protected MusicPlayInfo(Parcel in) {
        this.id = in.readLong();
        this.typeId = in.readLong();
        this.originType = in.readInt();
        this.position = in.readInt();
    }

    @Generated(hash = 2033127759)
    public MusicPlayInfo() {
    }

    public static final Parcelable.Creator<MusicPlayInfo> CREATOR = new Parcelable.Creator<MusicPlayInfo>() {
        @Override
        public MusicPlayInfo createFromParcel(Parcel source) {
            return new MusicPlayInfo(source);
        }

        @Override
        public MusicPlayInfo[] newArray(int size) {
            return new MusicPlayInfo[size];
        }
    };
}
