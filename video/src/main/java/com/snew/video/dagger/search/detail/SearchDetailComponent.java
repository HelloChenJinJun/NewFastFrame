package com.snew.video.dagger.search.detail;

import com.example.commonlibrary.dagger.scope.PerFragment;
import com.snew.video.dagger.VideoComponent;
import com.snew.video.mvp.search.detail.SearchVideoDetailFragment;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/15     8:58
 */
@PerFragment
@Component(dependencies = VideoComponent.class, modules = SearchDetailModule.class)
public interface SearchDetailComponent {
    public void inject(SearchVideoDetailFragment searchVideoDetailFragment);
}
