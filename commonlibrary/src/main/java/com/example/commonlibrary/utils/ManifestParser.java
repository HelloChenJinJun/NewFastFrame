package com.example.commonlibrary.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.example.commonlibrary.module.IAppLife;

import java.util.ArrayList;
import java.util.List;

public final class ManifestParser {
    private static final String MODULE_VALUE = "IModuleConfig";

    private final Context context;

    public ManifestParser(Context context) {
        this.context = context;
    }

    public List<IAppLife> parse() {
        List<IAppLife> modules = new ArrayList<>();
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                for (String key : appInfo.metaData.keySet()) {
                    if (MODULE_VALUE.equals(appInfo.metaData.get(key))) {
                        modules.add(parseModule(key));
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("解析Application失败", e);
        }
        CommonLogger.e("module:" + modules.size());
        return modules;
    }

    private static IAppLife parseModule(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }

        Object module;
        try {
            module = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return (IAppLife) module;
    }
}