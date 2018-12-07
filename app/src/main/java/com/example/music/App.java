package com.example.music;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.router.BaseAction;
import com.example.commonlibrary.router.Router;
import com.example.commonlibrary.router.RouterConfig;
import com.example.commonlibrary.router.RouterRequest;
import com.example.commonlibrary.router.RouterResult;
import com.example.commonlibrary.utils.Constant;

import androidx.multidex.MultiDex;


/**
 * Created by COOTEK on 2017/8/30.
 */

public class App extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        initRouterConfig();
        getAppComponent().getSharedPreferences()
                .edit().putBoolean(Constant.ALONE, true).apply();
    }

    private void initRouterConfig() {
        Router.getInstance().registerProvider(RouterConfig.MAIN_PROVIDE_NAME, "login", new BaseAction() {
            @Override
            public RouterResult invoke(RouterRequest routerRequest) {
                Activity activity = (Activity) routerRequest.getContext();
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                if (routerRequest.isFinish()) {
                    activity.finish();
                }
                return null;
            }
        });
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }


}
