package com.example.commonlibrary.cusotomview;

import android.view.View;

import com.example.commonlibrary.R;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/27      21:31
 * QQ:             1981367757
 */

public class ToolBarOption {
        /**
         * 图标资源
         */
        private int logoId =0;
        /**
         * 标题
         */
        private String title;


        private int titleId = 0;


        private String avatar;
        private int rightResId = 0;
        private int bgColor=-1;
        private View customView;



        private int titleColor=-10;
        private int mBackResId;

        public void setTitleColor(int titleColor) {
                this.titleColor = titleColor;
        }

        public int getTitleColor() {
                return titleColor;
        }

        public int getRightResId() {
                return rightResId;
        }

        public void setRightResId(int rightResId) {
                this.rightResId = rightResId;
        }

        public String getAvatar() {
                return avatar;
        }

        public void setAvatar(String avatar) {
                this.avatar = avatar;
        }

        /**
         * 返回按钮图片资源
         */
        private int navigateId = R.drawable.ic_back;
        private boolean isNeedNavigation = true;


        private String rightText;
        private View.OnClickListener rightListener;

        public View.OnClickListener getRightListener() {
                return rightListener;
        }

        public void setRightListener(View.OnClickListener rightListener) {
                this.rightListener = rightListener;
        }

        public String getRightText() {
                return rightText;
        }

        public void setRightText(String rightText) {
                this.rightText = rightText;
        }

        public int getLogoId() {
                return logoId;
        }

        public void setLogoId(int logoId) {
                this.logoId = logoId;
        }


        public String getTitle() {
                return title;
        }

        public void setTitle(String title) {
                this.title = title;
        }

        public int getTitleId() {
                return titleId;
        }

        public void setTitleId(int titleId) {
                this.titleId = titleId;
        }

        public int getNavigateId() {
                return navigateId;
        }

        public void setNavigateId(int navigateId) {
                this.navigateId = navigateId;
        }

        public boolean isNeedNavigation() {
                return isNeedNavigation;
        }

        public void setNeedNavigation(boolean needNavigation) {
                isNeedNavigation = needNavigation;
        }

        public void setBgColor(int bgColor) {
                this.bgColor = bgColor;
        }

        public int getBgColor() {
                return bgColor;
        }

        public void setCustomView(View customView) {
                this.customView = customView;
        }

        public View getCustomView() {
                return customView;
        }


        public int getBackResId() {
                return mBackResId;
        }

        public void setBackResId(int backResId) {
                mBackResId = backResId;
        }
}
