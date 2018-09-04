package com.example.chat.dagger.EditShare;

import com.example.chat.adapter.EditShareInfoAdapter;
import com.example.chat.mvp.EditShare.EditShareInfoActivity;
import com.example.chat.mvp.EditShare.EditShareInfoPresenter;
import com.example.commonlibrary.mvp.model.DefaultModel;

import javax.inject.Named;

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
    public EditShareInfoPresenter providerPresenter(DefaultModel model){
        return new EditShareInfoPresenter(editShareInfoActivity,model);
    }




    @Provides
    public EditShareInfoAdapter providerAdapter(){
        return new EditShareInfoAdapter();
    }

}
