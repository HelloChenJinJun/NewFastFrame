package com.example.commonlibrary;

import android.app.Application;
import android.content.Context;

import com.example.commonlibrary.module.IAppLife;
import com.example.commonlibrary.utils.ManifestParser;

import java.util.List;

/**
 * Created by COOTEK on 2017/8/29.
 */

public class ApplicationDelegate implements IAppLife {
    private  List<IAppLife> list;


    public ApplicationDelegate(Context base) {
       list=new ManifestParser(base).parse();
    }

    @Override
    public void attachBaseContext(Context base) {
        if (list != null && list.size() > 0) {
            for (IAppLife life :
                    list) {
                life.attachBaseContext(base);
            }
        }

    }

    @Override
    public void onCreate(Application application) {
        if (list != null && list.size() > 0) {
            for (IAppLife life :
                    list) {
                life.onCreate(application);
            }
        }
    }

    @Override
    public void onTerminate(Application application) {
        if (list != null && list.size() > 0) {
            for (IAppLife life :
                    list) {
                life.onTerminate(application);
            }
        }
        list=null;
    }
}
