package com.example.commonlibrary.bean.news;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

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
    private boolean hasSelected;


    @Override
    public boolean equals(Object obj) {
        return obj!=null&&obj instanceof OtherNewsTypeBean
                && ((OtherNewsTypeBean) obj).getTypeId()!=null
                &&((OtherNewsTypeBean) obj).getTypeId().equals(getTypeId());
    }

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


    @Generated(hash = 2143294176)
    public OtherNewsTypeBean(String typeId, String name, boolean hasSelected) {
        this.typeId = typeId;
        this.name = name;
        this.hasSelected = hasSelected;
    }

    @Generated(hash = 722525601)
    public OtherNewsTypeBean() {
    }

    @Override
    public String toString() {
        return "OtherNewsTypeBean{" +
                "typeId='" + typeId + '\'' +
                ", name='" + name + '\'' +
                ", hasSelected=" + hasSelected +
                '}';
    }

    public boolean getHasSelected() {
        return this.hasSelected;
    }

    public void setHasSelected(boolean hasSelected) {
        this.hasSelected = hasSelected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.typeId);
        dest.writeString(this.name);
        dest.writeByte(hasSelected ? (byte) 1 : (byte) 0);
    }

    protected OtherNewsTypeBean(Parcel in) {
        this.typeId = in.readString();
        this.name = in.readString();
        this.hasSelected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<OtherNewsTypeBean> CREATOR = new Parcelable.Creator<OtherNewsTypeBean>() {
        public OtherNewsTypeBean createFromParcel(Parcel source) {
            return new OtherNewsTypeBean(source);
        }

        public OtherNewsTypeBean[] newArray(int size) {
            return new OtherNewsTypeBean[size];
        }
    };
}
