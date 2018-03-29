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
public class ImageItem implements MultipleItem, Serializable {
        public static final int ITEM_CAMERA = 1;
        public static final int ITEM_NORMAL = 2;


        @Override
        public boolean equals(Object obj) {
                return obj != null && obj instanceof ImageItem && ((((ImageItem) obj).getPath() != null
                        && ((ImageItem) obj).getPath().equals(getPath())) ||
                        (((ImageItem) obj).getItemViewType() == ITEM_CAMERA && getItemViewType() == ITEM_CAMERA));
        }

        private int layoutType;
        private String path;


        private boolean isCheck;

        public boolean isCheck() {
                return isCheck;
        }

        public void setCheck(boolean check) {
                isCheck = check;
        }

        public int getLayoutType() {
                return layoutType;
        }

        public void setLayoutType(int layoutType) {
                this.layoutType = layoutType;
        }

        public String getPath() {
                return path;
        }

        public void setPath(String path) {
                this.path = path;
        }

        @Override
        public int getItemViewType() {
                return getLayoutType();
        }
}
