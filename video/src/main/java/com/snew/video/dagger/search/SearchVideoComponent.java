package com.snew.video.dagger.search;

import com.example.commonlibrary.dagger.scope.PerActivity;
import com.snew.video.dagger.VideoComponent;
import com.snew.video.mvp.search.SearchVideoActivity;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/15     8:56
 */
@PerActivity
@Component(dependencies = VideoComponent.class, modules = SearchVideoModule.class)
public interface SearchVideoComponent {
    public void inject(SearchVideoActivity searchVideoActivity);
}
