package com.example.commonlibrary.adaptScreen;

/**
 * 项目名称:    android
 * 创建人:      陈锦军
 * 创建时间:    2018/8/22     15:07
 */
public interface IAdaptScreen {
     public boolean isBaseOnWidth();
    public int getScreenSize();
    public boolean cancelAdapt();
}
