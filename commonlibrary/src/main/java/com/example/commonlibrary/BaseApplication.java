package com.example.commonlibrary;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.commonlibrary.adaptScreen.ScreenAdaptManager;
import com.example.commonlibrary.cusotomview.CommonDialog;
import com.example.commonlibrary.dagger.component.AppComponent;
import com.example.commonlibrary.dagger.component.DaggerAppComponent;
import com.example.commonlibrary.dagger.module.GlobalConfigModule;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.Constant;
import com.example.commonlibrary.utils.DataCleanUtil;
import com.example.commonlibrary.utils.TimeUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.meituan.android.walle.WalleChannelReader;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.download.DownloadListener;
import com.tencent.bugly.beta.download.DownloadTask;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.tencent.bugly.beta.upgrade.UpgradeStateListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import androidx.multidex.MultiDex;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by COOTEK on 2017/7/28.
 */

public class BaseApplication extends Application implements View.OnClickListener {


    private static AppComponent appComponent;
    private static BaseApplication instance;
    private ApplicationDelegate applicationDelegate;
    private TextView start;
    private CommonDialog dialog;

    public static BaseApplication getInstance() {
        return instance;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        applicationDelegate = new ApplicationDelegate(base);
        applicationDelegate.attachBaseContext(base);
        MultiDex.install(this);
        Beta.installTinker();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initDagger();
        initFont();
        initUM();
        initScreenAdapt();
        initBugly();
        applicationDelegate.onCreate(this);
    }


    private void initBugly() {
        // 设置是否开启热更新能力，默认为true
        Beta.enableHotfix = true;
        // 设置是否自动下载补丁
        Beta.canAutoDownloadPatch = true;
        // 设置是否提示用户重启
        Beta.canNotifyUserRestart = true;
        // 设置是否自动合成补丁
        Beta.canAutoPatch = true;
        Beta.autoInit = true;
        Beta.upgradeCheckPeriod = 30 * 1000;
        Beta.autoCheckUpgrade = true;
        //        Beta.canNotShowUpgradeActs.add(LoginActivity.class);
        //        Beta.canNotShowUpgradeActs.add(SplashActivity.class);
        //        Beta.canNotShowUpgradeActs.add(GuideActivity.class);
        //        Beta.canNotShowUpgradeActs.add(ForgetActivity.class);
        Beta.upgradeListener = (ret, strategy, isManual, isSilence) -> {
            if (strategy != null) {
                if (getAppComponent().getActivityManager().getCurrentActivity() != null) {
                    (dialog = CommonDialog.newBuild(getAppComponent().getActivityManager().getCurrentActivity()).setContentView(getContentView())
                            .setLeftButton("以后再说", this)
                            .setRightButton("立即升级", this).build()).show();
                    start = dialog.getRight();
                    updateBtn(Beta.getStrategyTask());
                }
            } else {
                ToastUtils.showShortToast("当前为最新版本");
            }
        };
        Beta.canShowApkInfo = true;
        /**
         *  全量升级状态回调
         */
        Beta.upgradeStateListener = new UpgradeStateListener() {
            @Override
            public void onUpgradeSuccess(boolean isManual) {
            }

            @Override
            public void onUpgradeFailed(boolean isManual) {
            }

            @Override
            public void onUpgrading(boolean isManual) {
            }

            @Override
            public void onDownloadCompleted(boolean b) {
            }

            @Override
            public void onUpgradeNoVersion(boolean isManual) {
            }
        };
        /**
         * 补丁回调接口，可以监听补丁接收、下载、合成的回调
         */
        Beta.betaPatchListener = new BetaPatchListener() {
            @Override
            public void onPatchReceived(String patchFileUrl) {
                ToastUtils.showShortToast(patchFileUrl);
            }

            @Override
            public void onDownloadReceived(long savedLength, long totalLength) {
            }

            @Override
            public void onDownloadSuccess(String patchFilePath) {
            }

            @Override
            public void onDownloadFailure(String msg) {
            }

            @Override
            public void onApplySuccess(String msg) {
                ToastUtils.showShortToast("应用成功");
            }

            @Override
            public void onApplyFailure(String msg) {
                ToastUtils.showShortToast("应用失败");
            }

            @Override
            public void onPatchRollback() {
                ToastUtils.showShortToast("应用回滚");

            }
        };
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId,调试时将第三个参数设置为true
        Bugly.setIsDevelopmentDevice(this, BuildConfig.DEBUG);
        Bugly.setAppChannel(this, WalleChannelReader.getChannel(this.getApplicationContext()));
        Bugly.init(this, Constant.BUGLY_ID, BuildConfig.DEBUG);
    }


    private View getContentView() {
        Activity activity = getAppComponent().getActivityManager().getCurrentActivity();
        View view = activity.getLayoutInflater().inflate(R.layout.activity_upgrade, null, false);
        TextView info;
        TextView feature;
        info = view.findViewById(R.id.tv_activity_upgrade_info);
        feature = view.findViewById(R.id.tv_activity_upgrade_feature);
        UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();
        Beta.registerDownloadListener(new DownloadListener() {
            @Override
            public void onReceive(DownloadTask downloadTask) {
                updateBtn(downloadTask);
                int value = (int) (downloadTask.getSavedLength() / downloadTask.getTotalLength() * 1.0f);
                CommonLogger.e("下载百分比::" + value);
            }

            @Override
            public void onCompleted(DownloadTask downloadTask) {
                updateBtn(downloadTask);
                CommonLogger.e("下载完成");
            }

            @Override
            public void onFailed(DownloadTask downloadTask, int i, String s) {
                updateBtn(downloadTask);
                CommonLogger.e("下载失败" + s);
            }
        });
        String stringBuilder = "版本：" + upgradeInfo.versionName +
                "\n" + "包大小：" + DataCleanUtil.getFormatSize(upgradeInfo.fileSize) +
                "\n" + "更新时间：" + TimeUtil.getTime(upgradeInfo.publishTime, "yyyy-MM-dd");
        info.setText(stringBuilder);
        feature.setText(upgradeInfo.newFeature);
        return view;
    }

    public void updateBtn(DownloadTask task) {

        switch (task.getStatus()) {
            case DownloadTask.INIT:
            case DownloadTask.DELETED:
            case DownloadTask.FAILED: {
                start.setText("立即更新");
            }
            break;
            case DownloadTask.COMPLETE: {
                start.setText("安装");
            }
            break;
            case DownloadTask.DOWNLOADING: {
                start.setText("暂停");
            }
            break;
            case DownloadTask.PAUSED: {
                start.setText("继续下载");
            }
            break;
        }
    }


    private void initScreenAdapt() {
        ScreenAdaptManager.newBuild().designedHeight(445)
                .designedWidth(250).build();
    }

    private void initUM() {
        String channel = WalleChannelReader.getChannel(this.getApplicationContext());
        UMConfigure.init(this, Constant.UM_KEY, channel, UMConfigure.DEVICE_TYPE_PHONE, null);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_GAME);
        MobclickAgent.openActivityDurationTrack(false);
    }

    private void initFont() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/PingFang_Medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }


    private void initDagger() {
        appComponent = DaggerAppComponent.builder().globalConfigModule(new GlobalConfigModule(this))
                .build();

    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        applicationDelegate.onTerminate(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_view_dialog_common_left) {
            Beta.cancelDownload();
            dialog.dismiss();
        } else if (i == R.id.tv_view_dialog_common_right) {
            DownloadTask downloadTask = Beta.startDownload();
            if (downloadTask.getStatus() == DownloadTask.DOWNLOADING) {
                dialog.dismiss();
            }

        }
    }
}
