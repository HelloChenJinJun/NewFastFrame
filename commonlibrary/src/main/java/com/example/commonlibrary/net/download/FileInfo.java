package com.example.commonlibrary.net.download;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.commonlibrary.net.NetManager;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;


/**
 * 文件实体
 */
@Entity
public class FileInfo implements Parcelable {


    @Id
    private String url;

    private String name;
    private int totalBytes;
    private int loadBytes;
    private int speed;
    private int status;
    private String path;

    

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(int totalBytes) {
        this.totalBytes = totalBytes;
    }

    public int getLoadBytes() {
        return loadBytes;
    }

    public void setLoadBytes(int loadBytes) {
        this.loadBytes = loadBytes;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.name);
        dest.writeInt(this.totalBytes);
        dest.writeInt(this.loadBytes);
        dest.writeInt(this.speed);
        dest.writeInt(this.status);
        dest.writeString(this.path);
    }

    protected FileInfo(Parcel in) {
        this.url = in.readString();
        this.name = in.readString();
        this.totalBytes = in.readInt();
        this.loadBytes = in.readInt();
        this.speed = in.readInt();
        this.status = in.readInt();
        this.path = in.readString();
    }

    @Generated(hash = 1019480899)
    public FileInfo(String url, String name, int totalBytes, int loadBytes,
            int speed, int status, String path) {
        this.url = url;
        this.name = name;
        this.totalBytes = totalBytes;
        this.loadBytes = loadBytes;
        this.speed = speed;
        this.status = status;
        this.path = path;
    }

    @Generated(hash = 1367591352)
    public FileInfo() {
    }



    public FileInfo(int status, String url, String name, int totalSize) {
        this.status = status;
        this.url = url;
        this.name = name;
        this.totalBytes = totalSize;
        this.loadBytes = 0;
        this.speed = 0;
        this.path = NetManager.getInstance().getDownLoadCacheDir();
    }

    public FileInfo(String url, String name) {
        this.url = url;
        this.name = name;
        this.status = DownloadStatus.NORMAL;
        this.totalBytes = 0;
        this.loadBytes = 0;
        this.speed = 0;
        this.path = NetManager.getInstance().getDownLoadCacheDir();
    }




    public static final Creator<FileInfo> CREATOR = new Creator<FileInfo>() {
        @Override
        public FileInfo createFromParcel(Parcel source) {
            return new FileInfo(source);
        }

        @Override
        public FileInfo[] newArray(int size) {
            return new FileInfo[size];
        }
    };

    @Override
    public String toString() {
        return "FileInfo{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", totalBytes=" + totalBytes +
                ", loadBytes=" + loadBytes +
                ", speed=" + speed +
                ", status=" + status +
                ", path='" + path + '\'' +
                '}';
    }

    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put("url", url);
        values.put("name", name);
        values.put("path", path);
        values.put("loadBytes", loadBytes);
        values.put("totalBytes", totalBytes);
        values.put("status", status);
        return values;
    }
}
