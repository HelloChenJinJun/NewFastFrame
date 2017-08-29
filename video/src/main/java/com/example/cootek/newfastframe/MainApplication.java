package com.example.cootek.newfastframe;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.IApplicationLife;
import com.example.cootek.newfastframe.dagger.DaggerMainComponent;
import com.example.cootek.newfastframe.dagger.MainComponent;
import com.example.cootek.newfastframe.dagger.MainModule;


/**
 * Created by COOTEK on 2017/8/8.
 */

public class MainApplication extends BaseApplication implements IApplicationLife {

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

    @Override
    public void onAppCreate() {
    }
}
