package com.example.news.dagger.librarylogin;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.news.mvp.librarylogin.LibraryLoginActivity;
import com.example.news.mvp.librarylogin.LibraryLoginPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/18      16:57
 * QQ:             1981367757
 */
@Module
public class LibraryLoginModule {
    private LibraryLoginActivity libraryLoginActivity;

    public LibraryLoginModule(LibraryLoginActivity libraryLoginActivity) {
        this.libraryLoginActivity = libraryLoginActivity;
    }

    @Provides
    public LibraryLoginPresenter provideLibraryLoginPresenter(DefaultModel libraryLoginModel) {
        return new LibraryLoginPresenter(libraryLoginActivity, libraryLoginModel);
    }




}
