package com.example.news.dagger.librarylogin;

import com.example.commonlibrary.dagger.scope.PerActivity;
import com.example.news.mvp.librarylogin.LibraryLoginActivity;
import com.example.news.dagger.NewsComponent;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/18      16:56
 * QQ:             1981367757
 */
@PerActivity
@Component(dependencies = NewsComponent.class,modules = LibraryLoginModule.class)
public interface LibraryLoginComponent {
    public void inject(LibraryLoginActivity libraryLoginActivity);
}
