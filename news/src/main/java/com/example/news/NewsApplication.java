package com.example.news;

import android.app.Application;
import android.content.Context;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.news.OtherNewsTypeBean;
import com.example.commonlibrary.module.IAppLife;
import com.example.commonlibrary.module.IModuleConfig;
import com.example.commonlibrary.router.BaseAction;
import com.example.commonlibrary.router.Router;
import com.example.commonlibrary.router.RouterRequest;
import com.example.commonlibrary.router.RouterResult;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.FileUtil;
import com.example.news.dagger.DaggerNewsComponent;
import com.example.news.dagger.NewsComponent;
import com.example.news.dagger.NewsModule;
import com.example.news.event.UserInfoEvent;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/16      16:12
 * QQ:             1981367757
 */

public class NewsApplication implements IModuleConfig, IAppLife {
    private static NewsComponent newsComponent;

    @Override
    public void injectAppLifecycle(Context context, List<IAppLife> iAppLifes) {
        iAppLifes.add(this);
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycleCallbackses) {

    }

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
        Router.getInstance().registerProvider("news:person"
                , new BaseAction() {
                    @Override
                    public RouterResult invoke(RouterRequest routerRequest) {
                        Map<String, Object> map = routerRequest.getParamMap();
                        UserInfoEvent userInfoEvent = new UserInfoEvent();
                        for (Map.Entry<String, Object> entry :
                                map.entrySet()) {
                            if (entry.getValue() instanceof String) {
                                if (entry.getKey().equals(ConstantUtil.AVATAR)) {
                                    userInfoEvent.setAvatar(((String) entry.getValue()));
                                } else if (entry.getKey().equals(ConstantUtil.ACCOUNT)) {
                                    userInfoEvent.setAccount(((String) entry.getValue()));
                                } else if (entry.getKey().equals(ConstantUtil.PASSWORD)) {
                                    userInfoEvent.setPassword(((String) entry.getValue()));
                                } else if (entry.getKey().equals(ConstantUtil.NICK)) {
                                    userInfoEvent.setNick(((String) entry.getValue()));
                                } else if (entry.getKey().equals(ConstantUtil.NAME)) {
                                    userInfoEvent.setName(((String) entry.getValue()));
                                } else if (entry.getKey().equals(ConstantUtil.FROM)) {
                                    userInfoEvent.setFrom(((String) entry.getValue()));
                                } else if (entry.getKey().equals(ConstantUtil.BG_ALL)) {
                                    userInfoEvent.setAllBg(((String) entry.getValue()));
                                } else if (entry.getKey().equals(ConstantUtil.BG_HALF)) {
                                    userInfoEvent.setHalfBg(((String) entry.getValue()));
                                }
                            } else if (entry.getValue() instanceof Boolean) {
                                if (entry.getKey().equals(ConstantUtil.SEX)) {
                                    userInfoEvent.setSex(((Boolean) entry.getValue()));
                                }
                            }
                        }
                        BaseApplication.getAppComponent().getSharedPreferences()
                                .edit().putBoolean(ConstantUtil.LOGIN_STATUS, true)
                                .putString(ConstantUtil.ACCOUNT, userInfoEvent.getAccount())
                                .putString(ConstantUtil.PASSWORD, userInfoEvent.getPassword())
                                .putString(ConstantUtil.AVATAR, userInfoEvent.getAvatar())
                                .putString(ConstantUtil.NAME, userInfoEvent.getNick())
                                .putBoolean(ConstantUtil.SEX, userInfoEvent.getSex())
                                .putString(ConstantUtil.BG_HALF, userInfoEvent.getHalfBg())
                                .putString(ConstantUtil.BG_ALL, userInfoEvent.getAllBg())
                                .putString(ConstantUtil.NICK, userInfoEvent.getNick()).apply();
                        RxBusManager.getInstance()
                                .post(userInfoEvent);
                        return null;
                    }
                });
    }

    private void initDB(Application application) {
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonElements = jsonParser.parse(FileUtil.readData(application, "NewsChannel")).getAsJsonArray();
        List<OtherNewsTypeBean> result = new ArrayList<>();
        for (JsonElement item :
                jsonElements) {
            OtherNewsTypeBean bean = gson.fromJson(item, OtherNewsTypeBean.class);
            bean.setHasSelected(true);
            result.add(bean);
            CommonLogger.e("bean:" + bean.toString());
        }
        newsComponent.getRepositoryManager().getDaoSession().getOtherNewsTypeBeanDao()
                .insertOrReplaceInTx(result);
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
