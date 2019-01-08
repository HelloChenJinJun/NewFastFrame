package com.snew.video.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.news.OtherNewsTypeBean;
import com.example.commonlibrary.bean.video.VideoTabBean;
import com.example.commonlibrary.module.IAppLife;
import com.example.commonlibrary.router.BaseAction;
import com.example.commonlibrary.router.Router;
import com.example.commonlibrary.router.RouterConfig;
import com.example.commonlibrary.router.RouterRequest;
import com.example.commonlibrary.router.RouterResult;
import com.example.commonlibrary.utils.FileUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.snew.video.MainActivity;
import com.snew.video.dagger.DaggerVideoComponent;
import com.snew.video.dagger.VideoComponent;
import com.snew.video.dagger.VideoModule;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    Update
 * 创建人:      陈锦军
 * 创建时间:    2018/11/29     10:13
 */
public class VideoApplication implements IAppLife {

    private static VideoComponent mMainComponent;

    @Override
    public void attachBaseContext(Context base) {

    }

    @Override
    public void onCreate(Application application) {
        mMainComponent = DaggerVideoComponent.builder().appComponent(BaseApplication.getAppComponent())
                .videoModule(new VideoModule()).build();
        initRouter();
        initDB(application);
    }


    private void initDB(Application application) {
        if (mMainComponent.getRepositoryManager()
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
            mMainComponent.getRepositoryManager().getDaoSession().getOtherNewsTypeBeanDao()
                    .insertInTx(result);
        }
        if (mMainComponent.getRepositoryManager()
                .getDaoSession().getVideoTabBeanDao()
                .queryBuilder().buildCount().count() == 0) {
            List<VideoTabBean> list = new ArrayList<>();
            JsonParser jsonParser = new JsonParser();
            JsonArray jsonElements = jsonParser.parse(FileUtil.readData(application, "videotab")).getAsJsonArray();
            Gson gson = BaseApplication.getAppComponent().getGson();
            for (JsonElement item :
                    jsonElements) {
                VideoTabBean bean = gson.fromJson(item, VideoTabBean.class);
                list.add(bean);
            }
            mMainComponent.getRepositoryManager().getDaoSession().getVideoTabBeanDao()
                    .insertInTx(list);
        }


    }

    private void initRouter() {
        Router.getInstance().registerProvider(RouterConfig.VIDEO_PROVIDE_NAME, "enter", new BaseAction() {
            @Override
            public RouterResult invoke(RouterRequest routerRequest) {
                MainActivity.start((Activity) routerRequest.getContext());
                return null;
            }
        });
    }


    public static VideoComponent getMainComponent() {
        return mMainComponent;
    }

    @Override
    public void onTerminate(Application application) {
    }
}
