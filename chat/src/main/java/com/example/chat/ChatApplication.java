package com.example.chat;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.example.chat.base.Constant;
import com.example.chat.base.RandomData;
import com.example.chat.bean.CustomInstallation;
import com.example.chat.dagger.ChatMainComponent;
import com.example.chat.dagger.ChatMainModule;
import com.example.chat.dagger.DaggerChatMainComponent;
import com.example.chat.manager.LocationManager;
import com.example.chat.ui.HappyActivity;
import com.example.chat.ui.LoginActivity;
import com.example.chat.ui.SearchActivity;
import com.example.chat.ui.SearchFriendActivity;
import com.example.chat.ui.SelectedFriendsActivity;
import com.example.chat.ui.SettingsActivity;
import com.example.chat.ui.WallPaperActivity;
import com.example.chat.ui.fragment.HomeFragment;
import com.example.chat.ui.fragment.ShareInfoFragment;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.manager.ActivityManager;
import com.example.commonlibrary.module.IAppLife;
import com.example.commonlibrary.module.IModuleConfig;
import com.example.commonlibrary.router.BaseAction;
import com.example.commonlibrary.router.Router;
import com.example.commonlibrary.router.RouterRequest;
import com.example.commonlibrary.router.RouterResult;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import mabeijianxi.camera.VCamera;
import mabeijianxi.camera.util.DeviceUtils;
import okhttp3.OkHttpClient;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/10/9      15:32
 * QQ:             1981367757
 */

public class ChatApplication implements IModuleConfig, IAppLife {


    private static ChatMainComponent chatMainComponent;

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

    public static ChatMainComponent getChatMainComponent() {
        return chatMainComponent;
    }

    @Override
    public void onCreate(Application application) {
        chatMainComponent = DaggerChatMainComponent.builder().appComponent(BaseApplication.getAppComponent())
                .chatMainModule(new ChatMainModule()).build();
        Bmob.initialize(application, Constant.KEY);
//        AppStat.i(Constant.KEY, null);
        LogUtil.e("1服务器端初始化完成");
        CustomInstallation customInstallation=new CustomInstallation();
        customInstallation.save();
        LogUtil.e("设备ID在这里上传了");
        BmobPush.startWork(application);
        LogUtil.e("推送服务初始化完成");
        initOkHttp();
        initSmallVideo(application);
        initLocationClient();
        RandomData.initAllRanDomData();
        initRouter();
    }

    private void initRouter() {
        Router.getInstance().registerProvider("chat:login", new BaseAction() {
            @Override
            public RouterResult invoke(RouterRequest routerRequest) {
                Activity activity = (Activity) routerRequest.getContext();
                Intent intent = new Intent(activity, LoginActivity.class);
                intent.putExtra(ConstantUtil.FROM, ((String) routerRequest.getParamMap().get(ConstantUtil.FROM)));
                activity.startActivity(intent);
                return null;
            }
        });
        Router.getInstance().registerProvider("chat:main", new BaseAction() {
            @Override
            public RouterResult invoke(RouterRequest routerRequest) {
                HomeFragment homeFragment = HomeFragment.newInstance();
                RouterResult routerResult = new RouterResult();
                routerResult.setObject(homeFragment);
                return routerResult;
            }
        });
        Router.getInstance().registerProvider("chat:public", new BaseAction() {
            @Override
            public RouterResult invoke(RouterRequest routerRequest) {
                RouterResult routerResult=new RouterResult();
                routerResult.setObject(ShareInfoFragment.instance());
                return routerResult;
            }
        });
        Router.getInstance().registerProvider("chat:intent", new BaseAction() {
            @Override
            public RouterResult invoke(RouterRequest routerRequest) {
                Intent intent = (Intent) routerRequest.getObject();
                BaseActivity activity = (BaseActivity) routerRequest.getContext();
                if (intent.getSerializableExtra("url_share_message") != null) {
                    Serializable sharedMessage = intent.getSerializableExtra("url_share_message");
                    if (activity.getCurrentFragment() != null && activity.getCurrentFragment() instanceof HomeFragment) {
                        ((HomeFragment) activity.getCurrentFragment()).notifyUrlSharedMessageAdd(sharedMessage);
                    }
                }
                if (activity.getCurrentFragment() != null && activity.getCurrentFragment() instanceof HomeFragment) {
                    ((HomeFragment) activity.getCurrentFragment()).notifyNewIntentCome(intent);
                }
                return null;
            }
        });
        Router.getInstance().registerProvider("chat:menuOnClick", new BaseAction() {
            @Override
            public RouterResult invoke(RouterRequest routerRequest) {
                Map<String, Object> map = routerRequest.getParamMap();
                Activity activity = (Activity) routerRequest.getContext();
                String title = (String) map.get(ConstantUtil.TITLE);
                switch (title) {
                    case "搜索":
                        Intent intent = new Intent(activity, SearchActivity.class);
                        activity.startActivity(intent);
                        break;
                    case "添加好友":
                        ToastUtils.showShortToast("点击了添加好友");
                        SearchFriendActivity.start(activity);
                        break;
                    case "建群":
                        ToastUtils.showShortToast("点击了创建群");
                        Intent selectIntent = new Intent(activity, SelectedFriendsActivity.class);
                        selectIntent.putExtra("from", "createGroup");
                        activity.startActivity(selectIntent);
                        break;
                    case "背景":
                        ToastUtils.showShortToast("点击了背景");
                        Intent wallPaperIntent = new Intent(activity, WallPaperActivity.class);
                        wallPaperIntent.putExtra("from", "wallpaper");
                        activity.startActivityForResult(wallPaperIntent, Constant.REQUEST_CODE_SELECT_WALLPAPER);
                        break;
                    case "设置":
                        ToastUtils.showShortToast("点击了设置");
                        SettingsActivity.start(activity, Constant.REQUEST_CODE_EDIT_USER_INFO);
                        break;
                    case "开心时刻":
                        HappyActivity.startActivity(activity);
                        break;
                    default:
                        break;
                }
                return null;
            }
        });


    }


    private void initLocationClient() {
//                LocationManager.getInstance().registerLocationListener(this);
        LocationManager.getInstance().startLocation();
    }


    private void initSmallVideo(Application application) {
        LogUtil.e("初始化小视频缓存目录");
        // 设置拍摄视频缓存路径
        File dcim = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (DeviceUtils.isZte()) {
            if (dcim.exists()) {
                VCamera.setVideoCachePath(dcim + "/chen/");
            } else {
                VCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/",
                        "/sdcard-ext/")
                        + "/mabeijianxi/");
            }
        } else {
            VCamera.setVideoCachePath(dcim + "/mabeijianxi/");
        }
        VCamera.setDebugMode(true);
        VCamera.initialize(application);
    }

    private void initOkHttp() {
        OkHttpUtils.initClient(new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).build());
    }

    @Override
    public void onTerminate(Application application) {
        chatMainComponent = null;
    }
}
