package com.example.chat.util;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;

import com.example.chat.bean.ImageFolder;
import com.example.chat.bean.ImageItem;
import com.example.chat.listener.OnImageLoadListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/29      21:57
 * QQ:             1981367757
 */

public class LoadPictureUtil {
        private static final int LOAD_ALL = 0;
        private List<ImageFolder> imageFolderList;


        private Activity activity;
        private final String[] IMAGE_PROJECTION = {     //查询图片需要的数据列
                MediaStore.Images.Media.DISPLAY_NAME,   //图片的显示名称  aaa.jpg
                MediaStore.Images.Media.DATA,           //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
                MediaStore.Images.Media.SIZE,           //图片的大小，long型  132492
                MediaStore.Images.Media.WIDTH,          //图片的宽度，int型  1920
                MediaStore.Images.Media.HEIGHT,         //图片的高度，int型  1080
                MediaStore.Images.Media.MIME_TYPE,      //图片的类型     image/jpeg
                MediaStore.Images.Media.DATE_ADDED};    //图片被添加的时间，long型  1450518608


        public void getAllImageFolder(Activity activity, final OnImageLoadListener listener) {
                this.activity = activity;
                imageFolderList = new ArrayList<>();
                LoaderManager loaderManager = activity.getLoaderManager();
                loaderManager.initLoader(LoadPictureUtil.LOAD_ALL, null, new LoaderManager.LoaderCallbacks<Cursor>() {
                        @Override
                        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                                Loader<Cursor> loader = null;
                                if (id == LOAD_ALL) {
                                        LogUtil.e("加载系统数据");
                                        loader = new CursorLoader(LoadPictureUtil.this.activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[6] + " DESC");
                                }
                                return loader;
                        }

                        @Override
                        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                                LogUtil.e("加载完成");
                                imageFolderList.clear();
                                if (data != null) {
                                        ArrayList<ImageItem> images = new ArrayList<>();
                                        ImageItem imageItem;
                                        while (data.moveToNext()) {
                                                imageItem = new ImageItem();
                                                imageItem.setName(data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0])));
                                                imageItem.setPath(data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1])));
                                                imageItem.setSize(data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]))/1024);
                                                imageItem.setWidth(data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3])));
                                                imageItem.setHeight(data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4])));
                                                imageItem.setImageType(data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5])));
                                                imageItem.setCreatedTime(data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6])));
                                                images.add(imageItem);
                                                ImageFolder imageFolder = new ImageFolder();
                                                File file = FileUtil.newFile(imageItem.getPath());
                                                imageFolder.setName(file.getParentFile().getName());
                                                imageFolder.setPath(file.getParentFile().getAbsolutePath());
                                                if (!imageFolderList.contains(imageFolder)) {
                                                        ArrayList<ImageItem> list = new ArrayList<>();
                                                        list.add(imageItem);
                                                        imageFolder.setDisplay(imageItem);
                                                        imageFolder.setAllImages(list);
                                                        imageFolderList.add(imageFolder);
                                                } else {
                                                        imageFolderList.get(imageFolderList.indexOf(imageFolder)).getAllImages().add(imageItem);
                                                }
                                        }

                                        if (data.getCount() > 0) {
//                                                新建一个所有图片的文件夹
                                                ImageFolder allImageFolder = new ImageFolder();
                                                allImageFolder.setName("全部图片");
                                                allImageFolder.setPath("/");
                                                allImageFolder.setAllImages(images);
                                                allImageFolder.setDisplay(images.get(0));
                                                LogUtil.e("第一个封面信息\n");
                                                LogUtil.e("name" + images.get(0).getName() + "\n"
                                                        + "path" + images.get(0).getPath() + "\n"
                                                        + "size" + images.get(0).getSize() + "\n"
                                                        + "type" + images.get(0).getImageType() + "\n");
                                                imageFolderList.add(0, allImageFolder);
                                        }
                                }
                                listener.onImageLoaded(imageFolderList);
                        }

                        @Override
                        public void onLoaderReset(Loader<Cursor> loader) {
                                LogUtil.e("onLoaderReset：");
                        }
                });
        }
}
