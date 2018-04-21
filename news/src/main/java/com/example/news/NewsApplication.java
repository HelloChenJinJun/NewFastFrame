package com.example.news;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.news.OtherNewsTypeBean;
import com.example.commonlibrary.module.IAppLife;
import com.example.commonlibrary.module.IModuleConfig;
import com.example.commonlibrary.net.NetManager;
import com.example.commonlibrary.router.BaseAction;
import com.example.commonlibrary.router.Router;
import com.example.commonlibrary.router.RouterRequest;
import com.example.commonlibrary.router.RouterResult;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.rxbus.event.LoginEvent;
import com.example.commonlibrary.rxbus.event.PwChangeEvent;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.FileUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.news.bean.ResetPwResult;
import com.example.news.bean.SystemUserBean;
import com.example.news.dagger.DaggerNewsComponent;
import com.example.news.dagger.NewsComponent;
import com.example.news.dagger.NewsModule;
import com.example.commonlibrary.rxbus.event.UserInfoEvent;
import com.example.news.util.NewsUtil;
import com.example.news.util.ReLoginUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
        Router.getInstance().registerProvider("news:pw_change", new BaseAction() {
            @Override
            public RouterResult invoke(RouterRequest routerRequest) {
                Map<String, Object> map = routerRequest.getParamMap();
                String old = (String) map.get(ConstantUtil.PASSWORD_OLD);
                String news = (String) map.get(ConstantUtil.PASSWORD_NEW);
                new ReLoginUtil().resetPw(old, news);
                return null;
            }
        });


        Router.getInstance().registerProvider("news:login", new BaseAction() {
            @Override
            public RouterResult invoke(RouterRequest routerRequest) {
                Map<String, Object> map = routerRequest.getParamMap();
                final String account = (String) map.get(ConstantUtil.ACCOUNT);
                final String password = (String) map.get(ConstantUtil.PASSWORD);
                ReLoginUtil reLoginUtil = new ReLoginUtil();
                reLoginUtil.login(account
                        , password, new ReLoginUtil.CallBack() {
                            @Override
                            public void onSuccess(SystemUserBean bean) {
                                if (bean != null) {
                                    LoginEvent loginEvent = new LoginEvent();
                                    loginEvent.setSuccess(true);
                                    UserInfoEvent userInfoEvent = new UserInfoEvent();
                                    userInfoEvent.setMajor(bean.getADMISSIONS_PIC());
                                    userInfoEvent.setClassNumber(bean.getGRADUATION_PIC());
                                    userInfoEvent.setAccount(account);
                                    userInfoEvent.setPassword(password);
                                    userInfoEvent.setAvatar(bean.getSCHOOL_PIC());
                                    userInfoEvent.setNick(bean.getUSER_NAME());
                                    userInfoEvent.setName(bean.getUSER_NAME());
                                    userInfoEvent.setSex(bean.getUSER_SEX().equals("男") ? Boolean.TRUE : Boolean.FALSE);
                                    userInfoEvent.setStudentType(bean.getID_TYPE());
                                    userInfoEvent.setCollege(bean.getUNIT_NAME());
                                    userInfoEvent.setFrom("news");
                                    loginEvent.setUserInfoEvent(userInfoEvent);
                                    RxBusManager.getInstance().post(loginEvent);
                                }
                            }

                            @Override
                            public void onFailed(String errorMessage) {
                                CommonLogger.e("出错消息" + errorMessage);
                                LoginEvent loginEvent = new LoginEvent();
                                loginEvent.setErrorMessage(errorMessage);
                                loginEvent.setSuccess(false);
                                RxBusManager.getInstance().post(loginEvent);
                            }
                        });
                return null;
            }
        });
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
//                if (bean.getName().equals("头条")
//                        || bean.getName().equals("福利")
//                        || bean.getName().equals("地大")) {
//                    bean.setHasSelected(true);
//                } else {
//                    bean.setHasSelected(false);
//                }
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
