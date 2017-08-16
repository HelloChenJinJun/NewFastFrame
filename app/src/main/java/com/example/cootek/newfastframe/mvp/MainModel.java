package com.example.cootek.newfastframe.mvp;

import com.example.commonlibrary.mvp.BaseModel;
import com.example.commonlibrary.repository.BaseRepositoryManager;
import com.example.commonlibrary.repository.IRepositoryManager;
import com.example.cootek.newfastframe.MainRepositoryManager;

/**
 * Created by COOTEK on 2017/8/11.
 */

public class MainModel extends BaseModel<MainRepositoryManager> {
    public MainModel(MainRepositoryManager repositoryManager) {
        super(repositoryManager);
    }
}
