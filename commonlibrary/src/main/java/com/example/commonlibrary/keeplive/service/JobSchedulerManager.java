package com.example.commonlibrary.keeplive.service;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.PersistableBundle;

import com.example.commonlibrary.BaseApplication;

import androidx.annotation.RequiresApi;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/10     15:18
 */
public class JobSchedulerManager {
    public static final String PENDING_CLASS_NAME = "className";
    private static final int JOB_ID = 20;
    private static JobSchedulerManager sJobSchedulerManager;
    private final JobScheduler jobScheduleer;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static JobSchedulerManager getJobSchedulerManager() {
        if (sJobSchedulerManager == null) {
            sJobSchedulerManager = new JobSchedulerManager();
        }
        return sJobSchedulerManager;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private JobSchedulerManager() {
        jobScheduleer = (JobScheduler) BaseApplication.getInstance().getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void startJobScheduler(String className) {
        if (AliveJobService.isLive() || isBelowLOLLIPOP()) {
            return;
        }
        jobScheduleer.cancel(JOB_ID);
        // 构建JobInfo对象，传递给JobSchedulerService
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(BaseApplication.getInstance(), AliveJobService.class));
        if (Build.VERSION.SDK_INT >= 24) {
            builder.setMinimumLatency(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS); //执行的最小延迟时间
            builder.setOverrideDeadline(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);  //执行的最长延时时间
            builder.setMinimumLatency(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);
            builder.setBackoffCriteria(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS, JobInfo.BACKOFF_POLICY_LINEAR);//线性重试方案
        } else {
            builder.setPeriodic(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);
        }
        // 设置每3秒执行一下任务
        builder.setPeriodic(3000);
        // 设置设备重启时，执行该任务
        builder.setPersisted(true);
        // 当插入充电器，执行该任务
        builder.setRequiresCharging(true);
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putString(PENDING_CLASS_NAME, className);
        builder.setExtras(persistableBundle);
        JobInfo info = builder.build();
        //开始定时执行该系统任务
        jobScheduleer.schedule(info);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void stopJobScheduler() {
        if (isBelowLOLLIPOP())
            return;
        jobScheduleer.cancelAll();
    }

    private boolean isBelowLOLLIPOP() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }


}
