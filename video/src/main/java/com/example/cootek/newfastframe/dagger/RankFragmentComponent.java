package com.example.cootek.newfastframe.dagger;

import com.example.cootek.newfastframe.MainComponent;
import com.example.cootek.newfastframe.RankFragment;
import com.example.commonlibrary.dagger.scope.PerFragment;

import dagger.Component;

/**
 * Created by COOTEK on 2017/8/16.
 */
@PerFragment
@Component(dependencies = MainComponent.class, modules = {RankFragmentModule.class})
public interface RankFragmentComponent {
    public void inject(RankFragment rankFragment);
}
