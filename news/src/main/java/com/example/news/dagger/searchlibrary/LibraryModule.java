package com.example.news.dagger.searchlibrary;

import com.example.news.adapter.LibraryAdapter;
import com.example.news.mvp.searchlibrary.LibraryFragment;
import com.example.news.mvp.searchlibrary.LibraryModel;
import com.example.news.mvp.searchlibrary.LibraryPresenter;
import com.example.news.MainRepositoryManager;

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
    public LibraryPresenter provideLibraryPresenter(LibraryModel libraryModel) {
        return new LibraryPresenter(libraryFragment, libraryModel);
    }

    @Provides
    public LibraryModel provideLibraryModel(MainRepositoryManager mainRepositoryManager) {
        return new LibraryModel(mainRepositoryManager);
    }
}
