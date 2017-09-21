package com.example.news.dagger.booklist;

import com.example.commonlibrary.dagger.scope.PerFragment;
import com.example.news.mvp.booklist.BookInfoListFragment;
import com.example.news.dagger.NewsComponent;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/19      11:02
 * QQ:             1981367757
 */
@PerFragment
@Component(dependencies = NewsComponent.class, modules = BookInfoListModule.class)
public interface BookInfoListComponent {
    public void inject(BookInfoListFragment bookInfoListFragment);
}
