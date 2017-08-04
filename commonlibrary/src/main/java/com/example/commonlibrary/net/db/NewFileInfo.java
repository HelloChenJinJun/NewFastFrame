package com.example.commonlibrary.net.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by COOTEK on 2017/8/3.
 */
@Entity
public class NewFileInfo {
    @Id
    private String url;
    private String name;
    private int totalBytes;
    private int loadBytes;
    private int speed;
    private int status;
    private String path;
    @Generated(hash = 1306494811)
    public NewFileInfo(String url, String name, int totalBytes, int loadBytes,
            int speed, int status, String path) {
        this.url = url;
        this.name = name;
        this.totalBytes = totalBytes;
        this.loadBytes = loadBytes;
        this.speed = speed;
        this.status = status;
        this.path = path;
    }
    @Generated(hash = 1884440514)
    public NewFileInfo() {
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getTotalBytes() {
        return this.totalBytes;
    }
    public void setTotalBytes(int totalBytes) {
        this.totalBytes = totalBytes;
    }
    public int getLoadBytes() {
        return this.loadBytes;
    }
    public void setLoadBytes(int loadBytes) {
        this.loadBytes = loadBytes;
    }
    public int getSpeed() {
        return this.speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    
}
