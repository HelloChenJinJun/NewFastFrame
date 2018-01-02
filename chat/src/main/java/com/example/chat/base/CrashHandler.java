package com.example.chat.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.os.SystemClock;

import com.example.chat.bean.CrashMessage;
import com.example.chat.db.ChatDB;
import com.example.chat.manager.UserManager;
import com.example.chat.util.LogUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;


public class CrashHandler implements Thread.UncaughtExceptionHandler {

        //文件夹目录
        private static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/crash_log/";
        //文件名
        private static final String FILE_NAME = "crash";
        //文件名后缀
        private static final String FILE_NAME_SUFFIX = ".trace";
        //上下文
        private Context mContext;

        //单例模式
        private static CrashHandler sInstance;

        private CrashHandler() {

        }


        public static CrashHandler getInstance() {
                if (sInstance == null) {
                        sInstance = new CrashHandler();
                }
                return sInstance;
        }

        /**
         * 初始化方法
         *
         * @param context
         */
        public void init(Context context) {
                //将当前实例设为系统默认的异常处理器
                Thread.setDefaultUncaughtExceptionHandler(this);
                //获取Context，方便内部使用
                mContext = context.getApplicationContext();
        }

        /**
         * 捕获异常回掉
         *
         * @param thread 当前线程
         * @param ex     异常信息
         */
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
                //导出异常信息到SD卡
               File file=dumpExceptionToSDCard(ex);
                //上传异常信息到服务器
                uploadExceptionToServer(ex,file);
                //延时1秒杀死进程
                SystemClock.sleep(2000);
                Process.killProcess(Process.myPid());
        }

        /**
         * 导出异常信息到SD卡
         *
         * @param ex
         */
        private File dumpExceptionToSDCard(Throwable ex) {
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        return null;
                }
                //创建文件夹
                File dir = new File(PATH);
                if (!dir.exists()) {
                        dir.mkdirs();
                }
                //获取当前时间
                long current = System.currentTimeMillis();
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));
                //以当前时间创建log文件
                File file = new File(PATH + FILE_NAME + time + FILE_NAME_SUFFIX);
                try {
                        //输出流操作
                        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
                        //导出手机信息和异常信息
                        PackageManager pm = mContext.getPackageManager();
                        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
                        pw.println("发生异常时间：" + time);
                        pw.println("应用版本：" + pi.versionName);
                        pw.println("应用版本号：" + pi.versionCode);
                        pw.println("android版本号：" + Build.VERSION.RELEASE);
                        pw.println("android版本号API：" + Build.VERSION.SDK_INT);
                        pw.println("手机制造商:" + Build.MANUFACTURER);
                        pw.println("手机型号：" + Build.MODEL);
                        ex.printStackTrace(pw);
                        //关闭输出流
                        pw.close();
                } catch (Exception e) {
                }
                if (UserManager.getInstance().getCurrentUser() != null) {
                        ChatDB.create().saveOrUpdateCrashMessage(file.getAbsolutePath(), true);
                }
                return file;
        }

        /**
         * 上传异常信息到服务器
         *
         * @param ex
         * @param file
         */
        private void uploadExceptionToServer(Throwable ex, final File file) {
                PackageManager pm = mContext.getPackageManager();
                StringBuilder stringBuilder = new StringBuilder();
                long current = System.currentTimeMillis();
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));
                PackageInfo pi;
                try {
                        pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
                        stringBuilder.append("发生异常的时间")
                                .append(time).append("\n")
                                .append("应用版本").append(pi.versionName)
                                .append(time).append("\n")
                                .append("应用版本号").append(pi.versionCode)
                                .append(time).append("\n")
                                .append("android版本号:")
                                .append(Build.VERSION.RELEASE)
                                .append(time).append("\n")
                                .append("android版本号API")
                                .append(Build.VERSION.SDK_INT)
                                .append(time).append("\n")
                                .append("手机制造商").append(Build.MANUFACTURER)
                                .append(time).append("\n")
                                .append("手机型号").append(Build.MODEL)
                                .append(time).append("\n");
                } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                }
                stringBuilder.append(ex.getMessage());
                final CrashMessage message = new CrashMessage();
                message.setErrorMessage(stringBuilder.toString());
                if (UserManager.getInstance().getCurrentUser() != null) {
                        final List<String> allErrorMessage = ChatDB.create().getAllErrorMessage(false);
                        if (allErrorMessage != null && allErrorMessage.size() > 0) {
                                List<BmobObject> list = new ArrayList<>();
                                 List<CrashMessage> result = new ArrayList<>();
                                for (String error :
                                        allErrorMessage) {
                                        CrashMessage crashMessage = new CrashMessage();
                                        crashMessage.setErrorMessage(getStringFromFile(error));
                                        result.add(crashMessage);
                                }
                                list.addAll(result);
                                if (mContext!=null) {
                                        new BmobBatch().insertBatch(list).doBatch(new QueryListListener<BatchResult>() {
                                                @Override
                                                public void done(List<BatchResult> list, BmobException e) {
                                                        if (e == null) {
                                                                LogUtil.e("上传群crash信息成功");
                                                                if (UserManager.getInstance().getCurrentUser()!=null) {
                                                                        for (String message :
                                                                                allErrorMessage) {
                                                                                ChatDB.create().saveOrUpdateCrashMessage(message, true);
                                                                        }
                                                                }
                                                        }else {
                                                                LogUtil.e("上传批量crash信息到服务器上失败"+e.toString());
                                                        }
                                                }
                                        });
                                }
                        }
                }

                if (mContext!=null) {
                        message.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                        if (e == null) {
                                                LogUtil.e("上传crash信息到服务器上成功拉拉");
                                        }else {
                                                LogUtil.e("上传crash信息到服务器上失败拉拉"+e.toString());
                                                if (UserManager.getInstance().getCurrentUser() != null&&file!=null&&file.exists()) {
                                                        ChatDB.create().saveOrUpdateCrashMessage(file.getAbsolutePath(), false);
                                                }
                                        }
                                }
                        });
                }
        }

        private String getStringFromFile(String error) {
                try {
                        File file=new File(error);
                        BufferedReader bufferedReader=new BufferedReader(new FileReader(file));
                        LineNumberReader lineNumberReader=new LineNumberReader(bufferedReader);
                        StringBuilder stringBuilder=new StringBuilder();
                        String line;
                        while ((line = lineNumberReader.readLine()) != null) {
                                stringBuilder.append(line).append("\n");
                        }
                        return stringBuilder.toString();
                } catch (IOException e) {
                        e.printStackTrace();
                        LogUtil.e("从文件中获取信息失败"+e.getMessage());
                }
                return null;
        }
}
