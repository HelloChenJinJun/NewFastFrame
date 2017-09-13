package com.example.commonlibrary.mvp.model;


public interface IModel<R> {
    void onDestroy();

    R getRepositoryManager();
}
