package com.example.commonlibrary.manager;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.keeplive.onepix.OnePxActivity;
import com.example.commonlibrary.utils.CommonLogger;

import java.util.LinkedHashMap;

import javax.inject.Inject;

/**
 * <p>
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/12/9     18:42
 * QQ:             1981367757
 */

public class ActivityManager implements Application.ActivityLifecycleCallbacks {

    private LinkedHashMap<String, Activity> mMap;

    @Inject
    public ActivityManager(Application application) {
        mMap = new LinkedHashMap<>();
        application.registerActivityLifecycleCallbacks(this);
    }

    public void addActivity(Activity activity) {
        if (!containActivity(activity.getClass().getName())) {
            mMap.put(activity.getClass().getName(), activity);
        }
    }


    public boolean containActivity(String name) {
        return mMap.containsKey(name);
    }

    public Activity getActivity(String name) {
        return mMap.get(name);
    }


    private Activity currentActivity;


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        CommonLogger.e("activity启动:" + activity.getClass().getName());
        addActivity(activity);
        currentActivity = activity;
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
        removeActivity(activity);
        CommonLogger.e("activity销毁:" + activity.getClass().getName());

        if (activity instanceof OnePxActivity) {
        }
        if (currentActivity != null && currentActivity.equals(activity)) {
            currentActivity = null;
        }
    }

    private void removeActivity(Activity activity) {
        if (containActivity(activity.getClass().getName())) {
            mMap.remove(activity.getClass().getName());
        }
    }

    public void start(Class aClass, boolean finish) {
        if (mMap.values().size() > 0) {
            Activity activity = mMap.values().iterator().next();
            Intent intent = new Intent(activity, aClass);
            activity.startActivity(intent);
            if (finish) {
                activity.finish();
            }
        } else {
            Intent intent = new Intent();
            intent.setClass(BaseApplication.getInstance(), aClass);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            BaseApplication.getInstance().startActivity(intent);
        }
    }


    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void finish(Class aClass) {
        Activity activity = getActivity(aClass.getName());
        if (activity != null) {
            activity.finish();
        }
    }
}
