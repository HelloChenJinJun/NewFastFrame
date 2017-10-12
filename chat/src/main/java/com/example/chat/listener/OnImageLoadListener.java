package com.example.chat.listener;


import com.example.chat.bean.ImageFolder;

import java.util.List;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/29      21:59
 * QQ:             1981367757
 */
public interface OnImageLoadListener {

        void onImageLoaded(List<ImageFolder> imageFolderList);

}
