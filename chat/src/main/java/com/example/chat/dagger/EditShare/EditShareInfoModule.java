package com.example.chat.dagger.EditShare;

import com.example.chat.MainRepositoryManager;
import com.example.chat.adapter.EditShareInfoAdapter;
import com.example.chat.mvp.EditShare.EditShareInfoActivity;
import com.example.chat.mvp.EditShare.EditShareInfoModel;
import com.example.chat.mvp.EditShare.EditShareInfoPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/25     21:25
 * QQ:         1981367757
 */
@Module
public class EditShareInfoModule {
private EditShareInfoActivity editShareInfoActivity;

    public EditShareInfoModule(EditShareInfoActivity editShareInfoActivity) {
        this.editShareInfoActivity = editShareInfoActivity;
    }

    @Provides
    public EditShareInfoPresenter providerPresenter(EditShareInfoModel model){
        return new EditShareInfoPresenter(editShareInfoActivity,model);
    }

    @Provides
    public EditShareInfoModel providerModel(MainRepositoryManager mainRepositoryManager){
        return new EditShareInfoModel( mainRepositoryManager);
    }


    @Provides
    public EditShareInfoAdapter providerAdapter(){
        return new EditShareInfoAdapter();
    }

}
