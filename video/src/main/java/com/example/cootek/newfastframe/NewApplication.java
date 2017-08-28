package com.example.cootek.newfastframe;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.IApplicationLife;

/**
 * Created by COOTEK on 2017/8/28.
 */

public class NewApplication implements IApplicationLife {
    private static MainComponent mainComponent;

    @Override
    public void onAppCreate() {
        initMain();
    }

    private void initMain() {
        mainComponent = DaggerMainComponent.builder().mainModule(new MainModule()).appComponent(BaseApplication.getAppComponent()).build();
    }

    public static MainComponent getMainComponent() {
        return mainComponent;
    }
}
