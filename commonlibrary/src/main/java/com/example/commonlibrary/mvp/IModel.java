package com.example.commonlibrary.mvp;


public interface IModel<R> {
    void onDestroy();

    R getRepositoryManager();
}
