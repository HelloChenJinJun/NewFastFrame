package com.example.commonlibrary.module;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentManager;


import java.util.List;


public interface IModuleConfig {
    void injectAppLifecycle(Context context, List<IAppLife> iAppLifes);

    void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycleCallbackses);

}
