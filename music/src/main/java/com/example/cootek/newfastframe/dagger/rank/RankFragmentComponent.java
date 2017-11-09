package com.example.cootek.newfastframe.dagger.rank;

import com.example.cootek.newfastframe.dagger.main.MainComponent;
import com.example.cootek.newfastframe.ui.fragment.RankFragment;
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
