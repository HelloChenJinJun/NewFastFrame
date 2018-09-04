package com.example.commonlibrary.mvp.model;

import com.example.commonlibrary.repository.DefaultRepositoryManager;

import javax.inject.Inject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/8/26     14:48
 */

public class DefaultModel extends BaseModel<DefaultRepositoryManager> {
    @Inject
    public DefaultModel(DefaultRepositoryManager repositoryManager) {
        super(repositoryManager);
    }
}
