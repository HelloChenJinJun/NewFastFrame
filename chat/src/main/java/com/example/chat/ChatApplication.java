package com.example.chat;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.example.chat.base.Constant;
import com.example.chat.base.RandomData;
import com.example.chat.bean.CustomInstallation;
import com.example.chat.bean.ImageItem;
import com.example.chat.dagger.ChatMainComponent;
import com.example.chat.dagger.ChatMainModule;
import com.example.chat.dagger.DaggerChatMainComponent;
import com.example.chat.manager.NewLocationManager;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.notify.SystemNotifyActivity;
import com.example.chat.mvp.person.PersonFragment;
import com.example.chat.mvp.photoSelect.PhotoSelectActivity;
import com.example.chat.mvp.preview.PhotoPreViewActivity;
import com.example.chat.mvp.editInfo.EditUserInfoActivity;
import com.example.chat.mvp.login.LoginActivity;
import com.example.chat.mvp.search.SearchActivity;
import com.example.chat.mvp.searchFriend.SearchFriendActivity;
import com.example.chat.mvp.selectFriend.SelectedFriendsActivity;
import com.example.chat.mvp.settings.SettingsActivity;
import com.example.chat.mvp.UserDetail.UserDetailActivity;
import com.example.chat.mvp.wallpaper.WallPaperActivity;
import com.example.chat.mvp.main.HomeFragment;
import com.example.chat.mvp.shareinfo.ShareInfoFragment;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.module.IAppLife;
import com.example.commonlibrary.module.IModuleConfig;
import com.example.commonlibrary.router.BaseAction;
import com.example.commonlibrary.router.Router;
import com.example.commonlibrary.router.RouterRequest;
import com.example.commonlibrary.router.RouterResult;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
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
        RandomData.initAllRanDomData();
        initRouter();
    }

    private void initRouter() {
        Router.getInstance().registerProvider("chat:preview", new BaseAction() {
            @Override
            public RouterResult invoke(RouterRequest routerRequest) {
                Map<String,Object> map=routerRequest.getParamMap();
                List<String>  imageList= (List<String>) routerRequest.getObject();
                if (imageList != null&&imageList.size()>0) {
                    ArrayList<ImageItem>  list=new ArrayList<>();
                    for (String item:
                            imageList
                         ) {
                        ImageItem imageItem=new ImageItem();
                        imageItem.setPath(item);
                        list.add(imageItem);
                    }
                    PhotoPreViewActivity.start(((Activity) routerRequest.getContext()),(Integer) map.get(ConstantUtil.POSITION),list,false
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
