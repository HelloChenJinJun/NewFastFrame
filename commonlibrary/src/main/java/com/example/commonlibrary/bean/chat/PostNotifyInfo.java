package com.example.commonlibrary.bean.chat;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;


/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/5/21     11:04
 */
@Entity
public class PostNotifyInfo implements Parcelable {


    @Id
    private String id;


    private Integer readStatus;

    private Integer type;


    @Generated(hash = 1760421634)
    public PostNotifyInfo(String id, Integer readStatus, Integer type) {
        this.id = id;
        this.readStatus = readStatus;
        this.type = type;
    }


    @Generated(hash = 590558729)
    public PostNotifyInfo() {
    }


    protected PostNotifyInfo(Parcel in) {
        id = in.readString();
        if (in.readByte() == 0) {
            readStatus = null;
        } else {
            readStatus = in.readInt();
        }
        if (in.readByte() == 0) {
            type = null;
        } else {
            type = in.readInt();
        }
    }

    public static final Creator<PostNotifyInfo> CREATOR = new Creator<PostNotifyInfo>() {
        @Override
        public PostNotifyInfo createFromParcel(Parcel in) {
            return new PostNotifyInfo(in);
        }

        @Override
        public PostNotifyInfo[] newArray(int size) {
            return new PostNotifyInfo[size];
        }
    };

    public Integer getType() {
        return type;
    }


    public void setType(Integer type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        if (readStatus == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(readStatus);
        }
        if (type == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(type);
        }
    }
}
