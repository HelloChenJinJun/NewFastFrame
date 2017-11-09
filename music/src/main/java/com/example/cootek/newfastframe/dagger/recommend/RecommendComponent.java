package com.example.cootek.newfastframe.dagger.recommend;

import com.example.commonlibrary.dagger.scope.PerFragment;
import com.example.cootek.newfastframe.dagger.main.MainComponent;
import com.example.cootek.newfastframe.ui.fragment.RecommendFragment;

import dagger.Component;

/**
 * Created by COOTEK on 2017/9/1.
 */
@PerFragment
@Component(dependencies = MainComponent.class, modules = {RecommendModule.class})
public interface RecommendComponent {
    public void inject(RecommendFragment recommendFragment);
}
