package com.example.chat.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/29      21:59
 * QQ:             1981367757
 */
public class ImageFolder implements Serializable {


        private static final long serialVerisionUID = 1L;
        /**
         * 当前的文件夹的名字
         */
        private String name;
        /**
         * 文件夹的路径
         */
        private String path;
        /**
         * 封面图信息
         */
        private ImageItem display;
        /**
         * 所有图片信息
         */
        private ArrayList<ImageItem> allImages;


        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getPath() {
                return path;
        }

        public void setPath(String path) {
                this.path = path;
        }

        public ImageItem getDisplay() {
                return display;
        }

        public void setDisplay(ImageItem display) {
                this.display = display;
        }

        public ArrayList<ImageItem> getAllImages() {
                return allImages;
        }

        public void setAllImages(ArrayList<ImageItem> allImages) {
                this.allImages = allImages;
        }

        @Override
        public boolean equals(Object obj) {
                if (obj instanceof ImageFolder) {
                        ImageFolder imageFolder = (ImageFolder) obj;
                        return imageFolder.getName().equals(getName()) && imageFolder.getPath().equals(getPath());
                } else {
                        return false;
                }
        }
}
