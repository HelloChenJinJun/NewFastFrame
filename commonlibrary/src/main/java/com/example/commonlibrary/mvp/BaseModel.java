package com.example.commonlibrary.mvp;


import com.example.commonlibrary.repository.BaseRepositoryManager;
import com.example.commonlibrary.repository.IRepositoryManager;

public class BaseModel implements IModel {


    protected BaseRepositoryManager baseRepositoryManager;


    public BaseModel(BaseRepositoryManager repositoryManager) {
        this.baseRepositoryManager = repositoryManager;
    }


    public BaseRepositoryManager getRepositoryManager() {
        return baseRepositoryManager;
    }

    @Override
    public void onDestroy() {

    }
}
