package com.example.chat.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.example.chat.base.ConstantUtil;
import com.example.chat.base.RandomData;
import com.example.chat.bean.CustomInstallation;
import com.example.chat.dagger.ChatMainComponent;
import com.example.chat.dagger.ChatMainModule;
import com.example.chat.dagger.DaggerChatMainComponent;
import com.example.chat.mvp.preview.PhotoPreViewActivity;
import com.example.chat.util.LogUtil;
import com.example.chat.util.TimeUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.module.IAppLife;
import com.example.commonlibrary.router.BaseAction;
import com.example.commonlibrary.router.Router;
import com.example.commonlibrary.router.RouterRequest;
import com.example.commonlibrary.router.RouterResult;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.rxbus.event.NetStatusEvent;
import com.example.commonlibrary.utils.Constant;
import com.example.commonlibrary.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/10/9      15:32
 * QQ:             1981367757
 */

public class ChatApplication implements IAppLife {


    private static ChatMainComponent chatMainComponent;


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
        Bmob.initialize(application, ConstantUtil.KEY);
        //        AppStat.i(ConstantUtil.KEY, null);
        LogUtil.e("1服务器端初始化完成");
        CustomInstallation customInstallation = new CustomInstallation();
        customInstallation.save();
        LogUtil.e("设备ID在这里上传了" + customInstallation.getInstallationId());
        BmobPush.startWork(application);
        LogUtil.e("推送服务初始化完成");
        RandomData.initAllRanDomData();
        initRouter();
        initRxBus();
    }

    private void initRxBus() {
        RxBusManager.getInstance().registerEvent(NetStatusEvent.class, netStatusEvent -> {
            if (netStatusEvent.isConnected()) {
                if (BaseApplication.getAppComponent().getSharedPreferences().getLong(ConstantUtil.DELTA_TIME, 0L) == 0L) {
                    TimeUtil.getServerTime();
                }
            }
        });
    }

    private void initRouter() {
        Router.getInstance().registerProvider("chat", "preview", new BaseAction() {
            @Override
            public RouterResult invoke(RouterRequest routerRequest) {
                Map<String, Object> map = routerRequest.getParamMap();
                List<String> imageList = (List<String>) routerRequest.getObject();
                if (imageList != null && imageList.size() > 0) {
                    ArrayList<SystemUtil.ImageItem> list = new ArrayList<>();
                    for (String item :
                            imageList
                            ) {
                        SystemUtil.ImageItem imageItem = new SystemUtil.ImageItem();
                        imageItem.setPath(item);
                        list.add(imageItem);
                    }
                    PhotoPreViewActivity.start(((Activity) routerRequest.getContext()), (Integer) map.get(Constant.POSITION), list, false
                    );
                }
                return null;
            }
        });
    }

    @Override
    public void onTerminate(Application application) {
        chatMainComponent = null;
    }
}
