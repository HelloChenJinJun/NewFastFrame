package com.example.news;

import android.app.Application;
import android.content.Context;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.news.OtherNewsTypeBean;
import com.example.commonlibrary.module.IAppLife;
import com.example.commonlibrary.utils.FileUtil;
import com.example.news.dagger.DaggerNewsComponent;
import com.example.news.dagger.NewsComponent;
import com.example.news.dagger.NewsModule;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/16      16:12
 * QQ:             1981367757
 */

public class NewsApplication implements IAppLife {
    private static NewsComponent newsComponent;

    @Override
    public void attachBaseContext(Context base) {

    }

    @Override
    public void onCreate(Application application) {
        newsComponent = DaggerNewsComponent.builder().appComponent(BaseApplication.getAppComponent())
                .newsModule(new NewsModule()).build();
        initDB(application);
        initRouter();
    }

    private void initRouter() {
    }

    private void initDB(Application application) {
        if (newsComponent.getRepositoryManager()
                .getDaoSession().getOtherNewsTypeBeanDao()
                .queryBuilder().build().list().size() == 0) {
            List<OtherNewsTypeBean> result;
            JsonParser jsonParser = new JsonParser();
            JsonArray jsonElements = jsonParser.parse(FileUtil.readData(application, "NewsChannel")).getAsJsonArray();
            result = new ArrayList<>();
            Gson gson = BaseApplication.getAppComponent().getGson();
            for (JsonElement item :
                    jsonElements) {
                OtherNewsTypeBean bean = gson.fromJson(item, OtherNewsTypeBean.class);
                bean.setHasSelected(true);
                result.add(bean);
            }
            newsComponent.getRepositoryManager().getDaoSession().getOtherNewsTypeBeanDao()
                    .insertInTx(result);
        }
    }

    @Override
    public void onTerminate(Application application) {
        if (newsComponent != null) {
            newsComponent = null;
        }
    }


    public static NewsComponent getNewsComponent() {
        return newsComponent;
    }
}
