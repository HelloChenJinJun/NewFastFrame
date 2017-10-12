package com.example.chat.bean;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/29      13:38
 * QQ:             1981367757
 */


import com.example.commonlibrary.baseadapter.baseitem.MultipleItem;

import java.io.Serializable;

/**
 * 图片实体
 */
public class ImageItem implements Serializable, MultipleItem {


        public static final int ITEM_CAMERA = 10;
        public static final int ITEM_NORMAL = 11;


        private int itemType = ITEM_NORMAL;

        public void setItemType(int itemType) {
                this.itemType = itemType;
        }

        public int getItemType() {
                return itemType;
        }

        private static final long serialVerisionUID = 1L;
        /**
         * 图片的名称
         */
        private String name;
        /**
         * 图片的路径
         */
        private String path;
        /**
         * 图片的大小
         */
        private long size;
        /**
         * 图片的类型
         */
        private String imageType;
        /**
         * 图片的宽度
         */
        private int width;
        /**
         * 图片的高度
         */
        private int height;
        /**
         * 图片的创建时间
         */
        private long createdTime;


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

        public long getSize() {
                return size;
        }

        public void setSize(long size) {
                this.size = size;
        }

        public int getWidth() {
                return width;
        }

        public void setWidth(int width) {
                this.width = width;
        }

        public String getImageType() {
                return imageType;
        }

        public void setImageType(String imageType) {
                this.imageType = imageType;
        }

        public int getHeight() {
                return height;
        }

        public void setHeight(int height) {
                this.height = height;
        }

        public long getCreatedTime() {
                return createdTime;
        }

        public void setCreatedTime(long createdTime) {
                this.createdTime = createdTime;
        }


        @Override
        public boolean equals(Object obj) {
                if (obj instanceof ImageItem) {
                        ImageItem imageItem = (ImageItem) obj;
//                        只有图片的路径和创建时间一样就被认为是相同的
                        return imageItem.getPath().equalsIgnoreCase(getPath()) && imageItem.getCreatedTime() == getCreatedTime();
                }
                return super.equals(obj);
        }

        @Override
        public int getItemViewType() {
                return getItemType();
        }
}
