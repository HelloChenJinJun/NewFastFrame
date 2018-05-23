package com.example.commonlibrary.bean.chat;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Arrays;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/5/23     12:00
 */
@Entity
public class SkinEntity implements Parcelable {
    @Id
    private String url;

    private String path;

    private String title;


    protected SkinEntity(Parcel in) {
        url = in.readString();
        path = in.readString();
        title = in.readString();
        hasSelected = in.readByte() != 0;
        imageList = in.createStringArrayList();
    }

    public static final Creator<SkinEntity> CREATOR = new Creator<SkinEntity>() {
        @Override
        public SkinEntity createFromParcel(Parcel in) {
            return new SkinEntity(in);
        }

        @Override
        public SkinEntity[] newArray(int size) {
            return new SkinEntity[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        return obj!=null&&obj instanceof SkinEntity
                && ((SkinEntity) obj).getUrl().equals(getUrl());
    }

    private boolean hasSelected;
    @Convert(columnType = String.class, converter =StringConverter.class)

    private List<String> imageList;

    @Generated(hash = 27652777)
    public SkinEntity(String url, String path, String title, boolean hasSelected,
            List<String> imageList) {
        this.url = url;
        this.path = path;
        this.title = title;
        this.hasSelected = hasSelected;
        this.imageList = imageList;
    }

    @Generated(hash = 237489517)
    public SkinEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(url);
        parcel.writeString(path);
        parcel.writeString(title);
        parcel.writeByte((byte) (hasSelected ? 1 : 0));
        parcel.writeStringList(imageList);
    }

    public static class StringConverter implements PropertyConverter<List<String>, String> {
        @Override
        public List<String> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            } else {
                return Arrays.asList(databaseValue.split(","));
            }
        }

        @Override
        public String convertToDatabaseValue(List<String> entityProperty) {
            if (entityProperty == null) {
                return null;
            } else {
                StringBuilder sb = new StringBuilder();
                for (String link : entityProperty) {
                    sb.append(link);
                    sb.append(",");
                }
                return sb.toString();
            }
        }
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isHasSelected() {
        return hasSelected;
    }

    public void setHasSelected(boolean hasSelected) {
        this.hasSelected = hasSelected;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public boolean getHasSelected() {
        return this.hasSelected;
    }
}
