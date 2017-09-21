package com.example.news.dagger.searchlibrary;

import com.example.commonlibrary.dagger.scope.PerFragment;
import com.example.news.dagger.NewsComponent;
import com.example.news.mvp.searchlibrary.LibraryFragment;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/17      18:06
 * QQ:             1981367757
 */
@PerFragment
@Component(dependencies = NewsComponent.class, modules = LibraryModule.class)
public interface LibraryComponent {
    public void inject(LibraryFragment libraryFragment);
}
