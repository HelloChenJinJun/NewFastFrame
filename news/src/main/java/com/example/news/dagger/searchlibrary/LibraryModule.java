package com.example.news.dagger.searchlibrary;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.news.adapter.LibraryAdapter;
import com.example.news.mvp.searchlibrary.LibraryFragment;
import com.example.news.mvp.searchlibrary.LibraryPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/17      18:07
 * QQ:             1981367757
 */
@Module
public class LibraryModule {
    private LibraryFragment libraryFragment;

    public LibraryModule(LibraryFragment libraryFragment) {
        this.libraryFragment = libraryFragment;
    }

    @Provides
    public LibraryAdapter provideLibraryAdapter() {
        return new LibraryAdapter();
    }

    @Provides
    public LibraryPresenter provideLibraryPresenter(DefaultModel libraryModel) {
        return new LibraryPresenter(libraryFragment, libraryModel);
    }


}
