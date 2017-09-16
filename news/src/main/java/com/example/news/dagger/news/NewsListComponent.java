package com.example.news.dagger.news;

import com.example.news.dagger.NewsComponent;
import com.example.news.mvp.NewsListModel;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/16      16:52
 * QQ:             1981367757
 */
@Component(dependencies = NewsComponent.class,modules = NewsListModule.class)
public class NewsListComponent {
}
