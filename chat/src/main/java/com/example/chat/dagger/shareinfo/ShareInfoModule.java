package com.example.chat.dagger.shareinfo;

import com.example.chat.MainRepositoryManager;
import com.example.chat.adapter.ShareInfoAdapter;
import com.example.chat.mvp.shareinfo.ShareInfoModel;
import com.example.chat.mvp.shareinfo.ShareInfoPresenter;
import com.example.chat.mvp.shareinfo.ShareInfoFragment;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/30     17:40
 * QQ:         1981367757
 */
@Module
public class ShareInfoModule {
    private ShareInfoFragment shareInfoFragment;

    public ShareInfoModule(ShareInfoFragment shareInfoFragment) {
        this.shareInfoFragment = shareInfoFragment;
    }



    @Provides
    public ShareInfoAdapter provideAdapter(){
        return new ShareInfoAdapter();
    }


    @Provides
    public ShareInfoPresenter providePresenter(ShareInfoModel shareInfoModel) {
        return new ShareInfoPresenter(shareInfoFragment, shareInfoModel);
    }


    @Provides
    public ShareInfoModel provideModel(MainRepositoryManager mainRepositoryManager) {
        return new ShareInfoModel(mainRepositoryManager);
    }


}
