package com.example.chat.events;


import com.example.chat.bean.ImageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/26     22:13
 * QQ:         1981367757
 *
 * 图片集合类，用于在选择图片界面、系统图片夹界面和编辑说说界面之间传递
 *
 */

public class ImageFolderEvent {

    private List<ImageItem> imageItems = new ArrayList<>();
    private int imageFolderPosition=0;
    private String imageFolderName;


    public static final String FROM_SELECT="SELECT";

    public static final String FROM_FOLDER="FOLDER";
    public String getImageFolderName() {
        return imageFolderName;
    }

    public void setImageFolderName(String imageFolderName) {
        this.imageFolderName = imageFolderName;
    }

    public int getImageFolderPosition() {
        return imageFolderPosition;
    }

    public void setImageFolderPosition(int imageFolderPosition) {
        this.imageFolderPosition = imageFolderPosition;
    }

    private String from;


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public ImageFolderEvent(String from, List<ImageItem> imageItems, int imageFolderPosition) {
        this.imageItems.addAll(imageItems);
        this.from=from;
        this.imageFolderPosition=imageFolderPosition;
    }

    public List<ImageItem> getImageItems() {
        return imageItems;
    }

    public void setImageItems(List<ImageItem> imageItems) {
        this.imageItems = imageItems;
    }
}
