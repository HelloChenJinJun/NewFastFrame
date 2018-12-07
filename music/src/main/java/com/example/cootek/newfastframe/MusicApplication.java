package com.example.cootek.newfastframe;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.module.IAppLife;
import com.example.commonlibrary.router.BaseAction;
import com.example.commonlibrary.router.Router;
import com.example.commonlibrary.router.RouterConfig;
import com.example.commonlibrary.router.RouterRequest;
import com.example.commonlibrary.router.RouterResult;
import com.example.cootek.newfastframe.dagger.main.DaggerMainComponent;
import com.example.cootek.newfastframe.dagger.main.MainComponent;
import com.example.cootek.newfastframe.dagger.main.MainModule;
import com.example.cootek.newfastframe.ui.MainActivity;


/**
 * Created by COOTEK on 2017/8/29.
 */

public class MusicApplication implements IAppLife {
    private static MainComponent mainComponent;


    @Override
    public void attachBaseContext(Context base) {
    }

    @Override
    public void onCreate(Application application) {
        mainComponent = DaggerMainComponent.builder().mainModule(new MainModule()).appComponent(BaseApplication.getAppComponent()).build();
        Router.getInstance().registerProvider(RouterConfig.MUSIC_PROVIDE_NAME, "enter", new BaseAction() {
            @Override
            public RouterResult invoke(RouterRequest routerRequest) {
                MainActivity.start((Activity) routerRequest.getContext());
                return null;
            }
        });
    }

    @Override
    public void onTerminate(Application application) {
        if (mainComponent != null) {
            mainComponent = null;
        }
    }


    public static MainComponent getMainComponent() {
        return mainComponent;
    }
}
