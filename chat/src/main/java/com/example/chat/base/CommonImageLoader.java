package com.example.chat.base;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.example.chat.bean.ImageFolder;
import com.example.chat.bean.ImageItem;
import com.example.chat.util.FileUtil;
import com.example.chat.util.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/29      13:01
 * QQ:             1981367757
 */

public class CommonImageLoader {

        public static final String CURRENT_POSITION = "current_position";
        public static final String PREVIEW_FROM = "preview_from";
        public static final String PREVIEW_DELETE = "preview_delete";
        public static final String PREVIEW_SELECT = "preview_select";
        public static final String IS_ORIGIN = "is_origin";
        public static final String PREVIEW_BASE = "preview_base";

        private static CommonImageLoader instance;
        private ImageLoader mImageLoader;
        /**
         * 最大的选择图片的个数
         */
        private int mMaxSelectedCount=8;

        /**
         * 已选择的图片信息
         */
        private List<ImageItem> mSelectedImages = new ArrayList<>();


        private List<ImageFolder> mImageFolders = new ArrayList<>();
        private int mCurrentImageFolderPosition = 0;

        public ImageFolder getCurrentImageFolder() {
                return getImageFolders().get(getCurrentImageFolderPosition());
        }

        public static CommonImageLoader getInstance() {
                if (instance == null) {
                        synchronized (CommonImageLoader.class) {
                                if (instance == null) {
                                        instance = new CommonImageLoader();
                                }
                        }
                }
                return instance;
        }


        public void setImageLoader(ImageLoader imageLoader) {
                mImageLoader = imageLoader;
        }

        public ImageLoader getImageLoader() {
                if (mImageLoader == null) {
                        LogUtil.e("图片加载器为空，所以new 一个新的");
                        mImageLoader = new GlideImageLoader();
                }
                return mImageLoader;
        }

        private void setMaxSelectedCount(int maxSelectedCount) {
                mMaxSelectedCount = maxSelectedCount;
        }

        public int getMaxSelectedCount() {
                return mMaxSelectedCount;
        }


        public List<ImageItem> getSelectedImages() {
                return mSelectedImages;
        }

        public void setSelectedImages(List<ImageItem> selectedImages) {
                mSelectedImages = selectedImages;
        }


        public void setImageFolders(List<ImageFolder> imageFolders) {
                mImageFolders = imageFolders;
        }

        public List<ImageFolder> getImageFolders() {
                return mImageFolders;
        }


        public void setCurrentImageFolderPosition(int currentImageFolderPosition) {
                mCurrentImageFolderPosition = currentImageFolderPosition;
        }

        private int getCurrentImageFolderPosition() {
                return mCurrentImageFolderPosition;
        }


        public void clearAllData() {
                if (mSelectedImages.size() > 0) {
                        mSelectedImages.clear();
                }
                if (mImageFolders.size() > 0) {
                        mImageFolders.clear();
                }
                setImageLoader(null);
        }

        public void initStanderConfig(int maxSelectedCount) {
                clearAllData();
                setMaxSelectedCount(maxSelectedCount);
                setImageLoader(new GlideImageLoader());
        }


        public void pickPhoto(Activity activity, int requestCode) {
                Intent intent = new Intent();
                if (Build.VERSION.SDK_INT < 19) {
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                } else {
                        intent.setAction(Intent.ACTION_PICK);
                        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                }
                activity.startActivityForResult(intent, requestCode);
        }

        public File takePhoto(Activity activity, int requestCodeTakePicture) {

                File dir;
                if (FileUtil.isExistSDCard()) {
                        dir = FileUtil.newDir(Constant.IMAGE_CACHE_DIR + "take_picture/");
                } else {
                        dir = Environment.getDataDirectory();
                }
                File file=null;
                if (dir != null) {
                        file = FileUtil.newFile(dir.getAbsolutePath() + System.currentTimeMillis() + ".jpg");
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                activity.startActivityForResult(intent, requestCodeTakePicture);
                return file;

        }
}
