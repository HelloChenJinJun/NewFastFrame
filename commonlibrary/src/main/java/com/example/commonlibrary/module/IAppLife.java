package com.example.commonlibrary.module;

import android.app.Application;
import android.content.Context;



public interface IAppLife {
    void attachBaseContext(Context base);

    void onCreate(Application application);

    void onTerminate(Application application);
}
