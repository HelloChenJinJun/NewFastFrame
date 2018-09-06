package com.example.commonlibrary.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by COOTEK on 2017/8/4.
 */

public class PermissionUtil {

    public static final String TAG = "Permission";


    private PermissionUtil() {
    }

    public static void requestTakePhoto(Activity activity, RequestPermissionCallBack callBack) {
        requestPermission(callBack,new RxPermissions(activity),Manifest.permission.CAMERA);
    }

    public interface RequestPermissionCallBack {
        void onRequestPermissionSuccess();

        void onRequestPermissionFailure();
    }


    @SuppressLint("CheckResult")
    public static void requestPermission(final RequestPermissionCallBack requestPermission, RxPermissions rxPermissions, String... permissions) {
        if (permissions == null || permissions.length == 0) return;
        List<String> needRequest = new ArrayList<>();
        for (String permission : permissions) { //过滤调已经申请过的权限
            if (!rxPermissions.isGranted(permission)) {
                needRequest.add(permission);
            }
        }
        if (needRequest.size() == 0) {//全部权限都已经申请过，直接执行操作
            requestPermission.onRequestPermissionSuccess();
        } else {//没有申请过,则开始申请
            rxPermissions
                    .request(needRequest.toArray(new String[needRequest.size()]))
                    .subscribe(aBoolean -> {
                        if (requestPermission != null) {
                            if (aBoolean) {
                                requestPermission.onRequestPermissionSuccess();
                            } else {
                                requestPermission.onRequestPermissionFailure();
                            }
                        }
                    });
        }
    }


    /**
     * 请求摄像头权限
     */
    public static void launchCamera(RequestPermissionCallBack requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
    }


    /**
     * 请求外部存储的权限
     */
    public static void externalStorage(RequestPermissionCallBack requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }


    public static void  requestLocation(RequestPermissionCallBack requestPermissionCallBack, Activity activity){
        RxPermissions rxPermissions=new RxPermissions(activity);
        requestPermission(requestPermissionCallBack, rxPermissions, Manifest.permission.ACCESS_FINE_LOCATION
        ,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
        ,Manifest.permission.LOCATION_HARDWARE,Manifest.permission
        .CONTROL_LOCATION_UPDATES,Manifest.permission.INSTALL_LOCATION_PROVIDER);
    }


    /**
     * 请求发送短信权限
     */
    public static void sendSms(RequestPermissionCallBack requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.SEND_SMS);
    }


    /**
     * 请求打电话权限
     */
    public static void callPhone(RequestPermissionCallBack requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.CALL_PHONE);
    }


    /**
     * 请求获取手机状态的权限
     */
    public static void readPhonestate(RequestPermissionCallBack requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.READ_PHONE_STATE);
    }

}
