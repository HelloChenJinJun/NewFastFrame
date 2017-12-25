package com.example.commonlibrary.manager;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * <p>
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/12/9     18:42
 * QQ:             1981367757
 */

public class ActivityManager implements Application.ActivityLifecycleCallbacks {
    private List<Activity> activityList;
    private Application application;
    private Activity currentActivity;

    @Inject
    public ActivityManager(Application application) {
        this.application = application;
        activityList = new ArrayList<>();
    }

    public void addActivity(Activity activity) {
        if (activityList == null) {
            activityList = new ArrayList<>();
        }
        if (!activityList.contains(activity)) {
            activityList.add(activity);
        }
    }





    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void startActivity(Intent intent) {
        if (currentActivity != null) {
            currentActivity.startActivity(intent);
        } else if (getTopActivity() != null) {
            getTopActivity().startActivity(intent);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            application.startActivity(intent);
        }
    }

    private Activity getTopActivity() {
        if (activityList != null && activityList.size() > 0) {
            return activityList.get(activityList.size() - 1);
        }
        return null;
    }


    public void clearAllActivity() {
        if (activityList != null) {
            activityList.clear();
            activityList = null;
        }
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        addActivity(activity);
        currentActivity = activity;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
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
    }

    private void removeActivity(Activity activity) {
        if (activityList != null && activityList.contains(activity)) {
            activityList.remove(activity);
        }
    }

}
