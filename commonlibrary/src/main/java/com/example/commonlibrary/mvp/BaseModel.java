package com.example.commonlibrary.mvp;


import com.example.commonlibrary.repository.BaseRepositoryManager;
import com.example.commonlibrary.repository.IRepositoryManager;

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
