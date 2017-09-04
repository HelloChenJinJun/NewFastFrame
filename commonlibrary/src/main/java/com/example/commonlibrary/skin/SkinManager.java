package com.example.commonlibrary.skin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.SkinUtil;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by COOTEK on 2017/9/3.
 */

public class SkinManager {


    private static SkinManager instance;
    private Context context;
    private Resources resources;
    private String packageName;
    private boolean isLocal = true;
    private List<SkinUpdateListener> listeners;

    public static SkinManager getInstance() {
        if (instance == null) {
            instance = new SkinManager();
        }
        return instance;
    }


    private SkinManager() {
        init();
    }

    private void init() {
        CommonLogger.e("开始加载");
        listeners = new ArrayList<>();
//        复制所有资源文件到缓存目录中
        SkinUtil.setUpSkinFile();
        context = BaseApplication.getInstance();
        reset();
    }


    public void reset() {
        resources = context.getResources();
        packageName = context.getPackageName();
        isLocal = true;
    }


    public void loadSkinResource(String path, final LoadSkinListener listener) {
        new AsyncTask<String, Void, Resources>() {


            @Override
            protected void onPreExecute() {
                if (listener != null) {
                    listener.onStart();
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected Resources doInBackground(String... params) {
                File skinFile = new File(params[0]);
                if (!skinFile.exists()) {
                    return null;
                }
                try {
                    CommonLogger.e("path:" + params[0]);
                    PackageInfo packageInfo = context.getPackageManager().getPackageArchiveInfo(params[0], PackageManager.GET_ACTIVITIES);
                    packageName = packageInfo.packageName;
                    isLocal = false;
                    AssetManager assetManager = AssetManager.class.newInstance();
                    Method method = assetManager.getClass().getMethod("addAssetPath", String.class);
                    method.invoke(assetManager, params[0]);
                    Resources oldResource = context.getResources();
                    return new Resources(assetManager, oldResource.getDisplayMetrics(), oldResource.getConfiguration());
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                    String message = null;
                    CommonLogger.e("加载皮肤资源出错啦啦啦11" + message);
                    if (e.getStackTrace() != null) {
                        for (StackTraceElement bean :
                                e.getStackTrace()) {
                            CommonLogger.e(bean.toString());
                        }
                    }
                }
                return null;
            }


            @Override
            protected void onPostExecute(Resources resources) {
                if (resources != null) {
                    SkinManager.this.resources = resources;
                    if (listener != null) {
                        listener.onSuccess();
                    }
                    CommonLogger.e("加载资源成功啦啦啦");
                    notifySkinResourceUpdate();
                } else {
                    if (listener != null) {
                        listener.onFailed();
                    }
                    reset();
                }
            }
        }.execute(path);
    }

    private void notifySkinResourceUpdate() {
    }


    public int getColor(int resId) {
        if (isLocal) {
            return ContextCompat.getColor(context, resId);
        }
        int id = resources.getIdentifier(context.getResources().getResourceEntryName(resId), "color", packageName);
        if (id == 0) {
//            如果找不到资源，返回本地资源
            return ContextCompat.getColor(context, resId);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return resources.getColor(id, null);
            } else {
                return resources.getColor(id);
            }
        }
    }


    public void refreshSkin() {
        if (skinLayoutInflaterFactory != null) {
            skinLayoutInflaterFactory.applyAllViewSkin();
        }
    }


    public Drawable getDrawable(int resId) {
        if (isLocal) {
            return ContextCompat.getDrawable(context, resId);
        }
        int drawableId = resources.getIdentifier(context.getResources().getResourceEntryName(resId), "drawable", packageName);
        if (drawableId == 0) {
            return ContextCompat.getDrawable(context, resId);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return resources.getDrawable(drawableId, null);
            } else {
                return resources.getDrawable(drawableId);
            }
        }
    }

    public ColorStateList getColorStateList(int resId) {
        if (isLocal) {
            CommonLogger.e("放回本地");
            return ContextCompat.getColorStateList(context, resId);
        }
        int id = resources.getIdentifier(context.getResources().getResourceEntryName(resId), "color", packageName);
        if (id == 0) {
//            如果找不到资源，返回本地资源
            return ContextCompat.getColorStateList(context, resId);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return resources.getColorStateList(id, null);
            } else {
                return resources.getColorStateList(id);
            }
        }
    }


    private SkinLayoutInflaterFactory skinLayoutInflaterFactory;

    public void apply(AppCompatActivity activity) {
        LayoutInflaterCompat.setFactory(activity.getLayoutInflater(), skinLayoutInflaterFactory = new SkinLayoutInflaterFactory(activity));
    }


    public void apply(String attrName, int attrResId, View view) {
        skinLayoutInflaterFactory.createSkinFromAttrName(attrName, attrResId, view).apply(view);
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }
}
