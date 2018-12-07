package com.example.commonlibrary.adaptScreen;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.Constant;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称:    android
 * 创建人:      陈锦军
 * 创建时间:    2018/8/22     14:16
 */
public class ScreenAdaptManager {
    private Map<String, AdaptInfo> adaptInfoMap;
    private int screenWidth;
    private int screenHeight;
    private int designedWidth;
    private int designedHeight;


    private static final String SYSTEM_CONFIG = "system_config";


    private ScreenAdaptManager(Builder builder) {
        designedWidth = builder.designedWidth;
        designedHeight = builder.designedHeight;
        BaseApplication.getAppComponent().getSharedPreferences().edit().putInt(Constant.DESIGNED_WIDTH, designedWidth)
                .putInt(Constant.DESIGNED_HEIGHT, designedHeight).apply();
        initData(BaseApplication.getInstance());
    }


    public static Builder newBuild() {
        return new Builder();
    }


    private String getKey(boolean isBaseOnWidth, int size) {
        return isBaseOnWidth + "&" + size;
    }


    private AdaptInfo getSystemAdaptInfo() {
        if (adaptInfoMap != null) {
            return adaptInfoMap.get(SYSTEM_CONFIG);
        }
        return null;
    }

    private void initData(Application application) {
        adaptInfoMap = new HashMap<>();
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        application.registerComponentCallbacks(new ComponentCallbacks() {
            @Override
            public void onConfigurationChanged(Configuration newConfig) {
                DisplayMetrics displayMetrics1 = Resources.getSystem().getDisplayMetrics();
                if (newConfig != null && newConfig.fontScale > 0) {
                    if (getSystemAdaptInfo() != null) {
                        getSystemAdaptInfo().scaledDensity = displayMetrics1.scaledDensity;
                    }
                }
                screenHeight = displayMetrics1.heightPixels;
                screenWidth = displayMetrics1.widthPixels;
            }

            @Override
            public void onLowMemory() {

            }
        });


        adaptInfoMap.put(SYSTEM_CONFIG, new AdaptInfo(displayMetrics.density, displayMetrics.densityDpi, displayMetrics.scaledDensity));
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (activity instanceof IAdaptScreen) {
                    IAdaptScreen iAdaptScreen = ((IAdaptScreen) activity);
                    if (!iAdaptScreen.cancelAdapt()) {
                        adaptScreen(activity, iAdaptScreen.isBaseOnWidth(), iAdaptScreen.getScreenSize());
                    }
                } else {
                    cancelAdapt(activity);
                }

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (activity instanceof IAdaptScreen) {
                    IAdaptScreen iAdaptScreen = (IAdaptScreen) activity;
                    if (!iAdaptScreen.cancelAdapt() && iAdaptScreen.needResetAdapt()) {
                        resetScreen(activity);
                    }
                }
            }
        });
    }

    private void cancelAdapt(Activity activity) {
        if (getSystemAdaptInfo() != null) {
            update(activity, getSystemAdaptInfo());
        }
    }

    private void adaptScreen(Activity activity, boolean isBaseOnWidth, int screenSize) {
        if (screenSize <= 0) {
            screenSize = isBaseOnWidth ? designedWidth : designedHeight;
        }
        String key = getKey(isBaseOnWidth, screenSize);
        AdaptInfo adaptInfo = adaptInfoMap.get(key);
        if (adaptInfo == null) {
            float density;
            AdaptInfo systemAdaptInfo = getSystemAdaptInfo();
            if (isBaseOnWidth) {
                density = screenWidth * 1.0f / screenSize;
            } else {
                density = screenHeight * 1.0f / screenSize;
            }
            int densityDpi = (int) (density * 160);
            float scaledDensity = density * systemAdaptInfo.scaledDensity / systemAdaptInfo.density;
            adaptInfo = new AdaptInfo(density, densityDpi, scaledDensity);
            CommonLogger.e("key:" + key);
            CommonLogger.e("info:" + adaptInfo);
            CommonLogger.e("screenWidth:" + screenWidth + "    screenHeight:" + screenHeight);
            CommonLogger.e("designedWidth:" + designedWidth + "    designedHeight:" + designedHeight);
            adaptInfoMap.put(key, adaptInfo);
        }
        update(activity, adaptInfo);
    }


    private void resetScreen(Activity activity) {
        update(activity, adaptInfoMap.get(getKey(true, designedWidth)));
    }

    private void update(Activity activity, AdaptInfo adaptInfo) {
        DisplayMetrics AppDisplayMetrics = activity.getApplication().getResources().getDisplayMetrics();
        AppDisplayMetrics.density = adaptInfo.density;
        AppDisplayMetrics.densityDpi = adaptInfo.densityDpi;
        AppDisplayMetrics.scaledDensity = adaptInfo.scaledDensity;
        DisplayMetrics activityMetrics = activity.getResources().getDisplayMetrics();
        activityMetrics.scaledDensity = adaptInfo.scaledDensity;
        activityMetrics.densityDpi = adaptInfo.densityDpi;
        activityMetrics.density = adaptInfo.density;
        List<Resources> list = new ArrayList<>(2);
        list.add(activity.getApplication().getResources());
        list.add(activity.getResources());
        for (Resources resource :
                list) {
            if ("MiuiResources".equals(resource.getClass().getSimpleName()) || "XResources".equals(resource.getClass().getSimpleName())) {
                try {
                    Field field = Resources.class.getDeclaredField("mTmpMetrics");
                    field.setAccessible(true);
                    DisplayMetrics miDisplayMetrics = (DisplayMetrics) field.get(resource);
                    miDisplayMetrics.scaledDensity = adaptInfo.scaledDensity;
                    miDisplayMetrics.densityDpi = adaptInfo.densityDpi;
                    miDisplayMetrics.density = adaptInfo.density;
                } catch (Exception ignored) {

                }
            }
        }


    }

    private static class AdaptInfo {
        private float density;
        private int densityDpi;
        private float scaledDensity;


        @Override
        public String toString() {
            return "AdaptInfo{" +
                    "density=" + density +
                    ", densityDpi=" + densityDpi +
                    ", scaledDensity=" + scaledDensity +
                    '}';
        }

        AdaptInfo(float density, int densityDpi, float scaledDensity) {
            this.density = density;
            this.densityDpi = densityDpi;
            this.scaledDensity = scaledDensity;
        }

        public float getDensity() {
            return density;
        }

        public int getDensityDpi() {
            return densityDpi;
        }

        public float getScaledDensity() {
            return scaledDensity;
        }
    }


    public static final class Builder {
        private int designedWidth;
        private int designedHeight;

        public Builder() {
        }

        public Builder designedWidth(int val) {
            designedWidth = val;
            return this;
        }

        public Builder designedHeight(int val) {
            designedHeight = val;
            return this;
        }

        public ScreenAdaptManager build() {
            return new ScreenAdaptManager(this);
        }
    }
}
