package com.example.commonlibrary.mvp;


import com.example.commonlibrary.baseadapter.EmptyLayout;

public interface IView<T> {

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




    /**
     * 展示空布局
     */
    void showEmptyView();




    /**
     * 更新数据
     * @param t
     */
    void updateData(T t);





}
