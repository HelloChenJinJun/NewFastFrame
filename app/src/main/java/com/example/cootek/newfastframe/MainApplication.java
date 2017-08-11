package com.example.cootek.newfastframe;

import com.example.commonlibrary.BaseApplication;

/**
 * Created by COOTEK on 2017/8/8.
 */

public class MainApplication extends BaseApplication {
    private static MainComponent mainComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        initMain();
    }

    private void initMain() {
        mainComponent = DaggerMainComponent.builder().mainModule(new MainModule()).appComponent(getAppComponent()).build();
    }


    public static MainComponent getMainComponent() {
        return mainComponent;
    }
}
