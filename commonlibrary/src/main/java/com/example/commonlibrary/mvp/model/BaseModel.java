package com.example.commonlibrary.mvp.model;


import com.example.commonlibrary.repository.BaseRepositoryManager;

public class BaseModel<R extends BaseRepositoryManager> implements IModel<R> {


    protected R baseRepositoryManager;


    public BaseModel(R repositoryManager) {
        this.baseRepositoryManager = repositoryManager;
    }

    @Override
    public void onDestroy() {
        baseRepositoryManager.clearAllCache();
    }

    @Override
    public R getRepositoryManager() {
        return baseRepositoryManager;
    }
}
