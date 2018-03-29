package com.example.chat.events;


import com.example.chat.bean.ImageItem;

/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/20     20:56
 * QQ:         1981367757
 */

public class PhotoPreViewEvent {
    public static final int TYPE_DELETE = 1;
    public static final int TYPE_ADD = 2;

    private int position;
    private int type;


    private ImageItem imageItem;

    public ImageItem getImageItem() {
        return imageItem;
    }

    public void setImageItem(ImageItem imageItem) {
        this.imageItem = imageItem;
    }

    public PhotoPreViewEvent(int position, int type, ImageItem imageItem) {
        this.position = position;
        this.type = type;
        this.imageItem = imageItem;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
