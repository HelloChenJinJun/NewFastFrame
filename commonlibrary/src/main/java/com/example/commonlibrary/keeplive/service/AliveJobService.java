package com.example.commonlibrary.keeplive.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;

import com.example.commonlibrary.utils.AppUtil;
import com.example.commonlibrary.utils.CommonLogger;

import androidx.annotation.RequiresApi;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/10     15:20
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class AliveJobService extends JobService {

    private static AliveJobService sAliveJobService;


    public static boolean isLive() {
        return sAliveJobService != null;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        sAliveJobService = this;
        String className = params.getExtras().getString(JobSchedulerManager.PENDING_CLASS_NAME);
        if (AppUtil.isAppAlive()) {
            CommonLogger.e("app还在运行中");
        } else {
            CommonLogger.e("app已经被杀死了，重启");
            try {
                Intent intent = new Intent(getApplicationContext(), Class.forName(className));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
