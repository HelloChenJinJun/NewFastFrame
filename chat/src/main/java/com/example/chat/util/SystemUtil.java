package com.example.chat.util;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import com.example.chat.base.Constant;
import com.example.chat.bean.ImageFolder;
import com.example.chat.bean.ImageItem;
import com.example.chat.listener.OnImageLoadListener;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.DensityUtil;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/20     15:37
 * QQ:         1981367757
 */

public class SystemUtil {


    public static final int REQUEST_CODE_TAKE_PHOTO = 1;
    public static final int REQUEST_CODE_SELECT_PHOTO = 2;
    public static final int REQUEST_CODE_VIDEO_RECORDER = 3;


    public static String takePhoto(Activity activity, int requestCode) {




        File dir;
        if (FileUtil.isExistSDCard()) {
            dir = FileUtil.newDir(Constant.IMAGE_CACHE_DIR + "take_picture/");
        } else {
            dir = Environment.getDataDirectory();
        }
        File file = null;
        if (dir != null) {
            file = FileUtil.newFile(dir.getAbsolutePath() + System.currentTimeMillis() + ".jpg");
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        activity.startActivityForResult(intent, requestCode);
        return file.getAbsolutePath();
    }


    public static Bitmap getVideoThumbnail(String videoPath, int width, int height,
                                           int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }


    public static String bitmapToFile(Bitmap bitmap) {
        File dir;
        if (FileUtil.isExistSDCard()) {
            dir = FileUtil.newDir(Constant.IMAGE_CACHE_DIR + "thumb/");
        } else {
            dir = Environment.getDataDirectory();
        }
        File file = null;
        if (dir != null) {
            file = FileUtil.newFile(dir.getAbsolutePath() + System.currentTimeMillis() + ".jpg");
        }
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        try {
            if (bos != null) {
                bos.flush();
                bos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }


    public static void pickPhoto(Activity activity, int requestCode) {
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


    public static String recorderVideo(Activity activity, int requestCode) {
        File dir;
        if (FileUtil.isExistSDCard()) {
            dir = FileUtil.newDir(Constant.VIDEO_CACHE_DIR + "take_picture/");
        } else {
            dir = Environment.getDataDirectory();
        }
        File file = null;
        if (dir != null) {
            file = FileUtil.newFile(dir.getAbsolutePath() + System.currentTimeMillis() + ".mp4");
        }
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(Build.VERSION.SDK_INT < 24){
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            activity.startActivityForResult(intent, requestCode);
        }else {
            //适配安卓7.0
            ContentValues contentValues=new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA,
                    file.getAbsolutePath());
            Uri uri= activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
            activity.grantUriPermission(activity.getPackageName(),uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
            activity.startActivityForResult(intent, requestCode);
        }
        return file.getAbsolutePath();
    }

    public static void showSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        view.requestFocus();
        //imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }


    private static final String[] IMAGE_PROJECTION = {     //查询图片需要的数据列
            MediaStore.Images.Media.DISPLAY_NAME,   //图片的显示名称  aaa.jpg
            MediaStore.Images.Media.DATA,           //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
            MediaStore.Images.Media.SIZE,           //图片的大小，long型  132492
            MediaStore.Images.Media.WIDTH,          //图片的宽度，int型  1920
            MediaStore.Images.Media.HEIGHT,         //图片的高度，int型  1080
            MediaStore.Images.Media.MIME_TYPE,      //图片的类型     image/jpeg
            MediaStore.Images.Media.DATE_ADDED};


    public static void getAllImageFolder(Activity activity, final OnImageLoadListener listener) {
        List<ImageFolder> imageFolderList = new ArrayList<>();
        LoaderManager loaderManager = activity.getLoaderManager();
        loaderManager.initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                Loader<Cursor> loader = null;
                if (id == 0) {
                    loader = new CursorLoader(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[6] + " DESC");
                }
                return loader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                CommonLogger.e("加载完成");
                imageFolderList.clear();
                if (data != null) {
                    ArrayList<ImageItem> images = new ArrayList<>();
                    ImageItem imageItem;
                    while (data.moveToNext()) {
                        imageItem = new ImageItem();
//                        imageItem.setName(data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0])));
                        imageItem.setPath(data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1])));
//                        imageItem.setSize(data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]))/1024);
//                        imageItem.setWidth(data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3])));
//                        imageItem.setHeight(data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4])));
//                        imageItem.setImageType(data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5])));
//                        imageItem.setCreatedTime(data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6])));
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
                        imageFolderList.add(0, allImageFolder);
                    }
                }
                listener.onImageLoaded(imageFolderList);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                CommonLogger.e("onLoaderReset：");
            }
        });
    }


    public static int getLayoutItemSize(Context context, int count, int marginSize) {
        int screenWidth = DensityUtil.getScreenWidth(context);
        return (int) ((screenWidth - (marginSize * (count - 1))) / (float) count);
    }

    public static Uri cropPhoto(Activity activity, String path) {
        Uri uri = Uri.fromFile(new File(path));
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(uri, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 200);
        cropIntent.putExtra("outputY", 200);
        cropIntent.putExtra("return-data", false);
        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        Uri cropUri =buildUri(activity);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        if (cropIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(cropIntent, ConstantUtil.REQUEST_CODE_CROP);
        }
        return cropUri;
    }

    /**
     * 4.尺寸压缩（通过缩放图片像素来减少图片占用内存大小）
     */

    public static void sizeCompress(String path, String newPath) {
        int reqHeight = 1920;
        int reqWidth = 1080;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;//这个参数设置为true才有效，
        Bitmap bmp = BitmapFactory.decodeFile(path, options);//这里的bitmap是个空
        int outHeight = bmp.getHeight();
        int outWidth = bmp.getWidth();
        if (outHeight > reqHeight || outWidth > reqWidth) {
            final int heightRatio = Math.round((float) outHeight / (float) reqHeight);
            final int widthRatio = Math.round((float) outHeight / (float) reqWidth);
            int ratio = Math.max(heightRatio, widthRatio);
            Bitmap result = Bitmap.createBitmap(bmp.getWidth() / ratio, bmp.getHeight() / ratio, Bitmap.Config.ARGB_8888);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 把压缩后的数据存放到baos中
            result.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            try {
                FileOutputStream fos = new FileOutputStream(FileUtil.newFile(newPath));
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static String sizeCompress(String path, int rqsW, int rqsH) {
        // 用option设置返回的bitmap对象的一些属性参数
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 设置仅读取Bitmap的宽高而不读取内容
        BitmapFactory.decodeFile(path, options);// 获取到图片的宽高，放在option里边
        final int height = options.outHeight;//图片的高度放在option里的outHeight属性中
        final int width = options.outWidth;
        int inSampleSize=1;
        if (rqsW == 0 || rqsH == 0) {
            options.inSampleSize = 1;
        } else if (height > rqsH || width > rqsW) {
            final int heightRatio = Math.round((float) height / (float) rqsH);
            final int widthRatio = Math.round((float) width / (float) rqsW);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            options.inSampleSize = inSampleSize;
        }
        // 主要通过option里的inSampleSize对原图片进行按比例压缩
        options.inJustDecodeBounds=false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        File dir = FileUtil.newDir(Constant.IMAGE_COMPRESS_DIR);
        File result = null;
        if (dir!=null) {
            result = FileUtil.newFile(dir.getAbsolutePath() + System.currentTimeMillis() + ".jpg");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        try {
            FileOutputStream fos;
            if (result!=null) {
                fos = new FileOutputStream(result);
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result!=null) {
            return result.getAbsolutePath();
        }else {
            return null;
        }
    }


    private static final String CROP_NAME = "crop.jpg";

    public static Uri buildUri(Activity activity) {
        if (FileUtil.isExistSDCard()) {
            return Uri.fromFile(Environment.getExternalStorageDirectory()).buildUpon().appendPath(CROP_NAME).build();
        } else {
            return Uri.fromFile(activity.getCacheDir()).buildUpon().appendPath(CROP_NAME).build();
        }
    }
}






