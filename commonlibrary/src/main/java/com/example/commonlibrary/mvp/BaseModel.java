package com.example.commonlibrary.mvp;


import com.example.commonlibrary.repository.IRepositoryManager;

public class BaseModel implements IModel {


    protected IRepositoryManager repositoryManager;


    public BaseModel(IRepositoryManager repositoryManager) {
        this.repositoryManager = repositoryManager;
    }

    @Override
    public void onDestroy() {

    }
}
