package com.example.commonlibrary.skin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.net.NetManager;
import com.example.commonlibrary.net.download.DownloadListener;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.rxbus.event.SkinUpdateEvent;
import com.example.commonlibrary.utils.CommonLogger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.LayoutInflaterCompat;

/**
 * Created by COOTEK on 2017/9/3.
 */

public class SkinManager {


    private static SkinManager instance;
    private Context context;
    private Resources resources;
    private String packageName;
    private boolean isLocal = true;

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
        factoryMap = new HashMap<>();
//        复制所有资源文件到缓存目录中
//        SkinUtil.setUpSkinFile();
        context = BaseApplication.getInstance();
        update(null);
    }


    public void update(String path) {
        if (path==null) {
            resources = context.getResources();
            packageName = context.getPackageName();
            isLocal = true;
            refreshSkin();
        }else {
            updateSkin(path, exception -> {

            });
        }
    }






    public void loadSkinResource(String path, final DownloadListener listener) {
        NetManager.getInstance().downLoad(path,listener);
    }

    public void updateSkin(String path,SkinUpdateListener skinUpdateListener) {
        try {
            CommonLogger.e("path:" +path);
            PackageInfo packageInfo = context.getPackageManager().getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
            packageName = packageInfo.packageName;
            isLocal = false;
            AssetManager assetManager = AssetManager.class.newInstance();
            Method method = assetManager.getClass().getMethod("addAssetPath", String.class);
            method.invoke(assetManager, path);
            Resources oldResource = context.getResources();
            SkinManager.this.resources=new Resources(assetManager, oldResource.getDisplayMetrics(), oldResource.getConfiguration());
            refreshSkin();
            skinUpdateListener.onUpdate(null);
        } catch (InstantiationException e) {
            e.printStackTrace();
            skinUpdateListener.onUpdate(e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            skinUpdateListener.onUpdate(e);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            skinUpdateListener.onUpdate(e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            skinUpdateListener.onUpdate(e);
        }
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
        if (factoryMap != null && factoryMap.size() > 0) {
            for (SkinLayoutInflaterFactory skin :
                    factoryMap.values()) {
                skin.applyAllViewSkin();
            }
        }
        RxBusManager.getInstance().post(new SkinUpdateEvent());
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


    private Map<Activity, SkinLayoutInflaterFactory> factoryMap;

    public void apply(AppCompatActivity activity) {
//        if (activity.getSharedPreferences(ThemeUtil.NAME, Context.MODE_PRIVATE).getBoolean(ThemeUtil.IS_NIGHT, false)) {
//            activity.setTheme(R.style.CustomTheme_Night);
//        } else {
//            activity.setTheme(R.style.CustomTheme_Day);
//        }
        factoryMap.put(activity, new SkinLayoutInflaterFactory(activity));
        LayoutInflaterCompat.setFactory(activity.getLayoutInflater(), factoryMap.get(activity));
    }



    public void clear(Activity activity){
        if (factoryMap.containsKey(activity)) {
           SkinLayoutInflaterFactory skinLayoutInflaterFactory= factoryMap.remove(activity);
            skinLayoutInflaterFactory.clear();
        }
    }






    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public  SkinLayoutInflaterFactory getSkinFactory(Activity activity) {
        return factoryMap.get(activity);
    }
}
