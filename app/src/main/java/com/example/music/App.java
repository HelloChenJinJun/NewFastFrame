package com.example.music;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.router.BaseAction;
import com.example.commonlibrary.router.Router;
import com.example.commonlibrary.router.RouterRequest;
import com.example.commonlibrary.router.RouterResult;
import com.example.commonlibrary.rxbus.event.UserInfoEvent;
import com.example.commonlibrary.utils.ConstantUtil;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.tencent.bugly.beta.upgrade.UpgradeStateListener;
//import com.tencent.bugly.Bugly;
//import com.tencent.bugly.BuglyStrategy;
//import com.tencent.bugly.beta.Beta;
//import com.tencent.bugly.beta.interfaces.BetaPatchListener;
//import com.tencent.bugly.beta.upgrade.UpgradeStateListener;

import java.util.Locale;
import java.util.Map;


/**
 * Created by COOTEK on 2017/8/30.
 */

public class App extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        setStrictMode();
        // 设置是否开启热更新能力，默认为true
        Beta.enableHotfix = true;
        // 设置是否自动下载补丁
        Beta.canAutoDownloadPatch = true;
        // 设置是否提示用户重启
        Beta.canNotifyUserRestart = true;
        // 设置是否自动合成补丁
        Beta.canAutoPatch = true;

        /**
         *  全量升级状态回调
         */
        Beta.upgradeStateListener = new UpgradeStateListener() {
            @Override
            public void onUpgradeFailed(boolean b) {

            }

            @Override
            public void onUpgradeSuccess(boolean b) {

            }

            @Override
            public void onUpgradeNoVersion(boolean b) {
                Toast.makeText(getApplicationContext(), "最新版本", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpgrading(boolean b) {
                Toast.makeText(getApplicationContext(), "onUpgrading", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadCompleted(boolean b) {

            }
        };

        /**
         * 补丁回调接口，可以监听补丁接收、下载、合成的回调
         */
        Beta.betaPatchListener = new BetaPatchListener() {
            @Override
            public void onPatchReceived(String patchFileUrl) {
                Toast.makeText(getApplicationContext(), patchFileUrl, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadReceived(long savedLength, long totalLength) {
                Toast.makeText(getApplicationContext(), String.format(Locale.getDefault(),
                        "%s %d%%",
                        Beta.strNotificationDownloading,
                        (int) (totalLength == 0 ? 0 : savedLength * 100 / totalLength)), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadSuccess(String patchFilePath) {
                Toast.makeText(getApplicationContext(), patchFilePath, Toast.LENGTH_SHORT).show();
//                Beta.applyDownloadedPatch();
            }

            @Override
            public void onDownloadFailure(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onApplySuccess(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onApplyFailure(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPatchRollback() {
                Toast.makeText(getApplicationContext(), "onPatchRollback", Toast.LENGTH_SHORT).show();
            }
        };
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId,调试时将第三个参数设置为true
        Bugly.init(this, "2e5309db50", true);
        BaseApplication
                .getAppComponent()
                .getSharedPreferences()
                .edit().putBoolean(ConstantUtil.IS_ALONE, false)
                .apply();
        initRouter();
    }

    private void initRouter() {
        Router.getInstance().registerProvider("app:person"
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
                                } else if (entry.getKey().equals(ConstantUtil.CLASS_NUMBER)) {
                                    userInfoEvent.setClassNumber(((String) entry.getValue()));
                                } else if (entry.getKey().equals(ConstantUtil.SCHOOL)) {
                                    userInfoEvent.setSchool(((String) entry.getValue()));
                                } else if (entry.getKey().equals(ConstantUtil.MAJOR)) {
                                    userInfoEvent.setMajor(((String) entry.getValue()));
                                } else if (entry.getKey().equals(ConstantUtil.COLLEGE)) {
                                    userInfoEvent.setCollege(((String) entry.getValue()));
                                } else if (entry.getKey().equals(ConstantUtil.YEAR)) {
                                    userInfoEvent.setYear(((String) entry.getValue()));
                                } else if (entry.getKey().equals(ConstantUtil.STUDENT_TYPE)) {
                                    userInfoEvent.setStudentType(((String) entry.getValue()));
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
                                .putString(ConstantUtil.NAME, userInfoEvent.getName())
                                .putBoolean(ConstantUtil.SEX, userInfoEvent.getSex())
                                .putString(ConstantUtil.BG_HALF, userInfoEvent.getHalfBg())
                                .putString(ConstantUtil.BG_ALL, userInfoEvent.getAllBg())
                                .putString(ConstantUtil.SCHOOL, userInfoEvent.getSchool())
                                .putString(ConstantUtil.COLLEGE, userInfoEvent.getCollege())
                                .putString(ConstantUtil.CLASS_NUMBER, userInfoEvent.getClassNumber())
                                .putString(ConstantUtil.MAJOR, userInfoEvent.getMajor())
                                .putString(ConstantUtil.STUDENT_TYPE, userInfoEvent.getStudentType())
                                .putString(ConstantUtil.YEAR, userInfoEvent.getYear())
                                .putString(ConstantUtil.NICK, userInfoEvent.getNick()).apply();
                        Activity activity = (Activity) routerRequest.getContext();
                        MainActivity.start(activity);
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
//         you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

//         安装tinker
//        Beta.installTinker();
    }


    @TargetApi(9)
    protected void setStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
    }
}
