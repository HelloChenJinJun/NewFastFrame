package com.example.commonlibrary.adaptScreen;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.router.RouterRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称:    android
 * 创建人:      陈锦军
 * 创建时间:    2018/8/22     14:16
 */
public class ScreenAdaptManager {
//    private static ScreenAdaptManager sInstance;

    private Map<String,AdaptInfo>  adaptInfoMap;




    private int screenWidth;
    private int screenHeight;
    private int designedWidth;
    private int designedHeight;




//    public static ScreenAdaptManager getInstance() {
//        if (sInstance == null) {
//            synchronized (ScreenAdaptManager.class){
//                if (sInstance == null) {
//                    sInstance=new ScreenAdaptManager();
//                }
//            }
//        }
//        return sInstance;
//    }








    private static final String SYSTEM_CONFIG="system_config";

    private ScreenAdaptManager() {
        initData(BaseApplication.getInstance());
    }

    private ScreenAdaptManager(Builder builder) {
        designedWidth = builder.designedWidth;
        designedHeight = builder.designedHeight;
    }


    private String getKey(boolean isBaseOnWidth,int size){
        return isBaseOnWidth+"&"+size;
    }







    public AdaptInfo getSystemAdaptInfo(){
        if (adaptInfoMap != null) {
            return adaptInfoMap.get(SYSTEM_CONFIG);
        }
        return null;
    }

    public void initData(Application application) {
        adaptInfoMap=new HashMap<>();
        DisplayMetrics displayMetrics= Resources.getSystem().getDisplayMetrics();
        screenHeight=displayMetrics.heightPixels;
        screenWidth=displayMetrics.widthPixels;
        application.registerComponentCallbacks(new ComponentCallbacks() {
            @Override
            public void onConfigurationChanged(Configuration newConfig) {
                if (newConfig != null && newConfig.fontScale > 0) {
                    getSystemAdaptInfo().scaledDensity=Resources.getSystem().getDisplayMetrics().scaledDensity;
                }
            }

            @Override
            public void onLowMemory() {

            }
        });


        adaptInfoMap.put(SYSTEM_CONFIG,new AdaptInfo(displayMetrics.density,displayMetrics.densityDpi,displayMetrics.scaledDensity));
            application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    if (activity instanceof IAdaptScreen) {
                       IAdaptScreen iAdaptScreen=((IAdaptScreen) activity);
                        if (!iAdaptScreen.cancelAdapt()) {
                            adaptScreen(activity,iAdaptScreen.isBaseOnWidth(),iAdaptScreen.getScreenSize());
                        }
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

                }
            });
    }

    public void adaptScreen(Activity activity,boolean isBaseOnWidth,int screenSize) {
        if (screenSize <= 0) {
            screenSize=designedWidth;
        }
        String key=getKey(isBaseOnWidth,screenSize);
        AdaptInfo adaptInfo=adaptInfoMap.get(key);
        if (adaptInfo == null) {
            float density;
            if (isBaseOnWidth) {
                density=screenWidth*1.0f/screenSize;
            }else {
                density=screenHeight*1.0f/screenSize;
            }
            int densityDpi= (int) (density*160);
            AdaptInfo systemAdaptInfo=getSystemAdaptInfo();
            float scaledDensity=density*systemAdaptInfo.scaledDensity/systemAdaptInfo.density;
            adaptInfo=new AdaptInfo(density,densityDpi,scaledDensity);
            adaptInfoMap.put(key,adaptInfo);
        }
        update(activity,adaptInfo);

    }

    private void update(Activity activity, AdaptInfo adaptInfo) {
       DisplayMetrics AppDisplayMetrics= activity.getApplication().getResources().getDisplayMetrics();
        AppDisplayMetrics.density=adaptInfo.density;
        AppDisplayMetrics.densityDpi=adaptInfo.densityDpi;
        AppDisplayMetrics.scaledDensity=adaptInfo.scaledDensity;
        DisplayMetrics activityMetrics=activity.getResources().getDisplayMetrics();
        activityMetrics.scaledDensity=adaptInfo.scaledDensity;
        activityMetrics.densityDpi=adaptInfo.densityDpi;
        activityMetrics.density=adaptInfo.density;
    }

    private static class AdaptInfo{
        private float density;
        private int densityDpi;
        private float scaledDensity;

        public AdaptInfo(float density, int densityDpi, float scaledDensity) {
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
