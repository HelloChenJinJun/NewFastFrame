package com.example.commonlibrary.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/24      19:42
 * QQ:             1981367757
 */



@Entity
public class OtherNewsTypeBean implements Parcelable {


    @Id
    private String typeId;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.typeId);
    }

    public OtherNewsTypeBean() {
    }

    protected OtherNewsTypeBean(Parcel in) {
        this.name = in.readString();
        this.typeId = in.readString();
    }

    @Generated(hash = 693506877)
    public OtherNewsTypeBean(String typeId, String name) {
        this.typeId = typeId;
        this.name = name;
    }

    public static final Parcelable.Creator<OtherNewsTypeBean> CREATOR = new Parcelable.Creator<OtherNewsTypeBean>() {
        public OtherNewsTypeBean createFromParcel(Parcel source) {
            return new OtherNewsTypeBean(source);
        }

        public OtherNewsTypeBean[] newArray(int size) {
            return new OtherNewsTypeBean[size];
        }
    };


    @Override
    public String toString() {
        return "OtherNewsTypeBean{" +
                "typeId='" + typeId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
