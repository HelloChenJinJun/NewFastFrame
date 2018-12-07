package com.example.commonlibrary.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.R;
import com.example.commonlibrary.adaptScreen.ScreenAdaptManager;


/**
 * 避免同样的信息多次触发重复弹出的问题
 */
public class ToastUtils {

    private static String oldMsg;
    private static long time;
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public static void showShortToast(final String msg) {
        String message = getMessage(msg);
        sHandler.post(() -> {
            if (message != null) {
                Context context = BaseApplication.getInstance();
                if (!message.equals(oldMsg)) { // 当显示的内容不一样时，即断定为不是同一个Toast
                    if (BaseApplication.getAppComponent().getSharedPreferences()
                            .getInt(Constant.DESIGNED_HEIGHT, 0) != 0) {
                        Toast toast = new Toast(context);
                        toast.setView(LayoutInflater.from(context).inflate(R.layout.view_toast, null, false));
                        TextView tv = toast.getView().findViewById(R.id.tv_view_toast_message);
                        tv.setText(message);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                    time = System.currentTimeMillis();
                } else {
                    //                     显示内容一样时，只有间隔时间大于Toast.LENGTH_SHORT时才显示
                    if (System.currentTimeMillis() - time > 3000) {
                        if (BaseApplication.getAppComponent().getSharedPreferences()
                                .getInt(Constant.DESIGNED_HEIGHT, 0) != 0) {
                            Toast toast = new Toast(context);
                            toast.setView(LayoutInflater.from(context).inflate(R.layout.view_toast, null, false));
                            TextView tv = toast.getView().findViewById(R.id.tv_view_toast_message);
                            tv.setText(message);
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                oldMsg = message;
            }
        });
    }

    private static String getMessage(String msg) {
        if (msg != null) {
            if (msg.startsWith("java.Net") || msg.startsWith("java.net") || msg.startsWith("Failed")) {
                return "网络连接失败，请检查网络配置";
            } else {
                return msg;
            }
        }
        return null;
    }

    public static void showLongToast(final String msg) {
        sHandler.post(() -> {
            Context context = BaseApplication.getInstance();
            if (!msg.equals(oldMsg)) { // 当显示的内容不一样时，即断定为不是同一个Toast
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                time = System.currentTimeMillis();
            } else {
                // 显示内容一样时，只有间隔时间大于Toast.LENGTH_SHORT时才显示
                if (System.currentTimeMillis() - time > Toast.LENGTH_LONG) {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                    time = System.currentTimeMillis();
                }
            }
            oldMsg = msg;
        });
    }
}