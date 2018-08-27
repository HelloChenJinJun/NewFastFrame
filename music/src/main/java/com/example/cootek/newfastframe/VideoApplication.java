package com.example.cootek.newfastframe;

import android.app.Application;
import android.content.Context;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.module.IAppLife;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.dagger.main.DaggerMainComponent;
import com.example.cootek.newfastframe.dagger.main.MainComponent;
import com.example.cootek.newfastframe.dagger.main.MainModule;


/**
 * Created by COOTEK on 2017/8/29.
 */

public class VideoApplication implements  IAppLife {
    private static MainComponent mainComponent;



    @Override
    public void attachBaseContext(Context base) {
        CommonLogger.e("代理attachBaseContext");
    }

    @Override
    public void onCreate(Application application) {
        CommonLogger.e("代理onCreate");
        mainComponent = DaggerMainComponent.builder().mainModule(new MainModule()).appComponent(BaseApplication.getAppComponent()).build();
    }

    @Override
    public void onTerminate(Application application) {
        CommonLogger.e("代理onTerminate");
        if (mainComponent != null) {
            mainComponent = null;
        }
    }


    public static MainComponent getMainComponent() {
        return mainComponent;
    }
}
