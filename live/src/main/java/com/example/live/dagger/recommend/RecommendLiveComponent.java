package com.example.live.dagger.recommend;

import com.example.commonlibrary.dagger.scope.PerFragment;
import com.example.live.ui.fragment.RecommendLiveFragment;
import com.example.live.dagger.MainComponent;

import dagger.Component;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/14      20:29
 * QQ:             1981367757
 */
@PerFragment
@Component(dependencies = MainComponent.class, modules = RecommendLiveModule.class)
public interface RecommendLiveComponent {

    public void inject(RecommendLiveFragment recommendLiveFragment);

}
