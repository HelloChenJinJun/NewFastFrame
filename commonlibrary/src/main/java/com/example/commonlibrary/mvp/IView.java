package com.example.commonlibrary.mvp;


import com.example.commonlibrary.baseadapter.EmptyLayout;

public interface IView {

    /**
     * 显示加载
     */
    void showLoading(String loadMessage);

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示信息
     */
    void showError(String message, EmptyLayout.OnRetryListener listener);

}
