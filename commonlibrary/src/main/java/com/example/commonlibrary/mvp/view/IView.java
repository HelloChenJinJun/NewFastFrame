package com.example.commonlibrary.mvp.view;


import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.trello.rxlifecycle2.LifecycleTransformer;

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



    <Y> LifecycleTransformer<Y> bindLife();





}
